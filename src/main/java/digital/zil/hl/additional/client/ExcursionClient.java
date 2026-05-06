package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExcursionDto;
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


@Component
public class ExcursionClient {

    private static final Logger log = LoggerFactory.getLogger(ExcursionClient.class);

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry; 

    @Value("${crud.service.url}")
    private String crudUrl;

    @Autowired
    public ExcursionClient(RestTemplate restTemplate, CircuitBreakerRegistry circuitBreakerRegistry) { 
        this.restTemplate = restTemplate;
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public List<ExcursionDto> getAllExcursions() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("callCoreServiceCB"); 

        java.util.function.Supplier<List<ExcursionDto>> excursionSupplier = () -> {
             String url = crudUrl + "/excursions";
             log.debug("Executing call to Core Service for all excursions at URL: {}", url);
             ResponseEntity<ExcursionDto[]> response = restTemplate.getForEntity(url, ExcursionDto[].class);
             return Arrays.asList(response.getBody());
        };

        try {
            List<ExcursionDto> result = circuitBreaker.executeSupplier(excursionSupplier); 
            log.debug("Successfully fetched excursions.");
            return result;
        } catch (Exception e) {
             log.error("Call to Core Service for excursions failed or was prevented by Circuit Breaker: {}", e.getMessage(), e);
             throw new RuntimeException("Could not retrieve excursions from Core Service, possibly due to Circuit Breaker or underlying error", e);
        }
    }
}