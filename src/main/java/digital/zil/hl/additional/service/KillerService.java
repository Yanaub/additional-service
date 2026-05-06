package digital.zil.hl.additional.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KillerService {

    private final RestTemplate restTemplate;
    private final String coreServiceUrl;
    private final boolean killerEnabled;

    @Autowired
    public KillerService(RestTemplate restTemplate,
                         @Value("${crud.service.url:http://museum-app:8080}") String coreServiceUrl,
                         @Value("${killer.enabled:true}") boolean killerEnabled) {
        this.restTemplate = restTemplate;
        this.coreServiceUrl = coreServiceUrl;
        this.killerEnabled = killerEnabled;
    }

    @Scheduled(fixedDelay = 60_000)
    public void killCoreService() {
        if (!killerEnabled) return;

        String url = coreServiceUrl + "/internal/crash";
        try {
            org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KillerService.class);
            log.info("Killer: Sending crash request to {}", url);
            restTemplate.postForEntity(url, null, String.class);
        } catch (Exception e) {
            org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(KillerService.class);
            log.info("Killer: Core Service crashed (expected): {}", e.getMessage());
        }
    }
}