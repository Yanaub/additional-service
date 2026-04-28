package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExcursionDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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
        String url = crudUrl + "/excursions";

        ResponseEntity<ExcursionDto[]> response = restTemplate.getForEntity(
                url,
                ExcursionDto[].class
        );

        return Arrays.asList(response.getBody());
    }
}