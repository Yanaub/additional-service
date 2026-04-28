package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExcursionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExcursionClient {

    private final RestTemplate restTemplate;

    @Value("${crud.service.url}")
    private String crudUrl;

    public ExcursionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExcursionDto> getAllExcursions() {
        ResponseEntity<List<ExcursionDto>> response = restTemplate.exchange(
                crudUrl + "/excursions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExcursionDto>>() {}
        );
        return response.getBody();
    }
}