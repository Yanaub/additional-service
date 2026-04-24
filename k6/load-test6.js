import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const VUS = Number(__ENV.VUS || 40);
const DURATION = __ENV.DURATION || '2m';
const WRITE_SHARE = Number(__ENV.WRITE_SHARE || 50); // 5, 50, 95
const THINK_TIME = Number(__ENV.THINK_TIME || 0.3);

export const options = {
  scenarios: {
    mixed_profile: {
      executor: 'constant-vus',
      vus: VUS,
      duration: DURATION,
      gracefulStop: '10s',
      exec: 'mixedFlow',
      tags: {
        profile: `${WRITE_SHARE}/${100 - WRITE_SHARE}`,
      },
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)'],
  thresholds: {
    'http_req_duration{operation:create}': ['p(95)<500'],
    'http_req_duration{operation:read}': ['p(95)<300'],
    'http_req_failed': ['rate<0.01'],
  },
};

export function mixedFlow() {
  if (Math.random() * 100 < WRITE_SHARE) {
    createVisitor();
  } else {
    readRating();
  }
  sleep(THINK_TIME);
}

function createVisitor() {
  const payload = JSON.stringify({
    identifier: generateUUID(),
    fullName: `User-${Date.now()}`,
    age: Math.floor(Math.random() * 60) + 18,
    ticketType: Math.random() > 0.5 ? 'FULL' : 'DISCOUNTED',
  });

  const res = http.post(`${BASE_URL}/visitors/`, payload, {
    headers: { 'Content-Type': 'application/json' },
    tags: { operation: 'create', entity: 'visitor' },
  });

  check(res, {
    'create: status 200': (r) => r.status === 200,
    'create: response time < 500ms': (r) => r.timings.duration < 500,
  });
}

function readRating() {
  const res = http.get(`${BASE_URL}/exhibits/rating`, {
    tags: { operation: 'read', entity: 'rating' },
  });

  check(res, {
    'read: status 200': (r) => r.status === 200,
    'read: has body': (r) => r.body && r.body.length > 0,
    'read: response time < 300ms': (r) => r.timings.duration < 300,
  });
}

function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    const v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}