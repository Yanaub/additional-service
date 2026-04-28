package digital.zil.hl.additional.client;

import digital.zil.hl.additional.model.ExhibitDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class ExhibitClient {

    private final RestTemplate restTemplate;

    @Value("${crud.service.url}")
    private String crudUrl;

    public ExhibitClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ExhibitDto> getAllExhibits() {
        String url = crudUrl + "/exhibits";

        ResponseEntity<ExhibitDto[]> response = restTemplate.getForEntity(
                url,
                ExhibitDto[].class
        );

        return Arrays.asList(response.getBody());
    }

    public ExhibitDto getExhibitById(UUID id) {
        String url = crudUrl + "/exhibits/" + id;

        ResponseEntity<ExhibitDto> response = restTemplate.getForEntity(
                url,
                ExhibitDto.class
        );

        return response.getBody();
    }
}