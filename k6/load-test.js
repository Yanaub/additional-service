import http from 'k6/http';
import { check, sleep } from 'k6';

const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080';
const WRITE_SHARE = Number(__ENV.WRITE_SHARE || 50);
const READ_SHARE = Number(__ENV.READ_SHARE || 50);
const LEVELS = [10, 20, 40, 80];

function splitVus(total, share) {
  return Math.max(1, Math.round(total * share / 100));
}

function makeStages(share) {
  const stages = [];
  for (const total of LEVELS) {
    stages.push({ duration: '10s', target: splitVus(total, share)*2 });
    stages.push({ duration: '60s', target: splitVus(total, share)*2 });
  }
  stages.push({ duration: '30s', target: 0 });
  return stages;
}

export const options = {
  scenarios: {
    create_visitors: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: makeStages(WRITE_SHARE),
      gracefulRampDown: '10s',
      exec: 'createVisitor',
    },

    read_rating: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: makeStages(READ_SHARE),
      gracefulRampDown: '10s',
      exec: 'readRating',
    },
  },
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)'],
  thresholds: {
    'http_req_duration{scenario:create_visitors}': ['p(95)<500'],
    'http_req_duration{scenario:read_rating}': ['p(95)<300'],
    'http_req_failed': ['rate<0.01'],
  },
};

export function createVisitor() {
  const payload = JSON.stringify({
    identifier: generateUUID(),
    fullName: `User-${Date.now()}`,
    age: Math.floor(Math.random() * 60) + 18,
    ticketType: Math.random() > 0.5 ? 'FULL' : 'DISCOUNTED',
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
    tags: {
          operation: 'create',
          entity: 'visitor'
        },
  };

  const res = http.post(`${BASE_URL}/visitors/`, payload, params);

  check(res, {
    'create: status 200': (r) => r.status === 200,
    'create: response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(0.5);
}

export function readRating() {
  const params = {
      tags: {
        operation: 'read',
        entity: 'rating'
      },
    };
  const res = http.get(`${BASE_URL}/exhibits/rating`);

  check(res, {
    'read: status 200': (r) => r.status === 200,
    'read: has body': (r) => r.body.length > 0,
    'read: response time < 300ms': (r) => r.timings.duration < 300,
  });

  sleep(1);
}

function generateUUID() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, (c) => {
    const r = (Math.random() * 16) | 0;
    const v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}