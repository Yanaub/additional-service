package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExhibitDto;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Component
public class ExhibitClient {

    private static final Logger log = LoggerFactory.getLogger(ExhibitClient.class);

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry; 

    @Value("${crud.service.url}")
    private String crudUrl;

    @Autowired
    public ExhibitClient(RestTemplate restTemplate, CircuitBreakerRegistry circuitBreakerRegistry) { 
        this.restTemplate = restTemplate;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public List<ExhibitDto> getAllExhibits() {
        String url = crudUrl + "/exhibits";
        log.debug("Executing call to Core Service for all exhibits at URL: {}", url);
        ResponseEntity<ExhibitDto[]> response = restTemplate.getForEntity(url, ExhibitDto[].class);
        return Arrays.asList(response.getBody());
    }

    public ExhibitDto getExhibitById(UUID id) {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("callCoreServiceCB"); 

        java.util.function.Supplier<ExhibitDto> exhibitSupplier = () -> {
             String url = crudUrl + "/exhibits/" + id;
             log.debug("Executing call to Core Service for exhibit ID: {} at URL: {}", id, url);
             ResponseEntity<ExhibitDto> response = restTemplate.getForEntity(url, ExhibitDto.class);
             return response.getBody(); 
        };

        try {
            ExhibitDto result = circuitBreaker.executeSupplier(exhibitSupplier); 
            log.debug("Successfully fetched exhibit ID: {}", id);
            return result;
        } catch (Exception e) { 
             log.error("Call to Core Service for exhibit ID '{}' failed or was prevented by Circuit Breaker: {}", id, e.getMessage(), e);
             throw new RuntimeException("Could not retrieve exhibit ID '" + id + "' from Core Service, possibly due to Circuit Breaker or underlying error", e);
        }
    }
}