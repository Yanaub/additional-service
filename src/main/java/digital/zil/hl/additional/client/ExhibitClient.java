package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExhibitDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ExhibitClient {

    private final RestTemplate restTemplate;

    @Value("${crud.service.url}")
    private String crudUrl;

    public ExhibitClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExhibitDto> getAllExhibits() {
        ResponseEntity<List<ExhibitDto>> response = restTemplate.exchange(
                crudUrl + "/exhibits",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ExhibitDto>>() {}
        );
        return response.getBody();
    }
}