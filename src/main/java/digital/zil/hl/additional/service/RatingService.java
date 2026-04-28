package digital.zil.hl.additional.service;

import digital.zil.hl.additional.client.ExhibitClient;
import digital.zil.hl.additional.client.ExcursionClient;
import digital.zil.hl.additional.model.ExcursionDto;
import digital.zil.hl.additional.model.ExhibitDto;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final ExhibitClient exhibitClient;
    private final ExcursionClient excursionClient;

    public RatingService(ExhibitClient exhibitClient, ExcursionClient excursionClient) {
        this.exhibitClient = exhibitClient;
        this.excursionClient = excursionClient;
    }

    public Map<String, Integer> getRating(Integer year, Integer month) {

        List<ExhibitDto> exhibits = exhibitClient.getAllExhibits();
        List<ExcursionDto> excursions = excursionClient.getAllExcursions();


        Map<String, Integer> rating = exhibits.stream()
                .collect(Collectors.toMap(
                        ExhibitDto::getName,
                        exhibit -> 0,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));


        List<ExcursionDto> filteredExcursions;

        if (year == null && month == null) {

            filteredExcursions = excursions;
        } else if (year != null && month != null) {

            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

            filteredExcursions = excursions.stream()
                    .filter(excursion ->
                            !excursion.getDate().isBefore(startDate) &&
                                    !excursion.getDate().isAfter(endDate))
                    .collect(Collectors.toList());
        } else {
            throw new IllegalArgumentException("Both year and month must be provided or none of them");
        }


        filteredExcursions.stream()
                .flatMap(excursion -> excursion.getExhibits().stream())
                .forEach(exhibit -> rating.merge(exhibit.getName(), 1, Integer::sum));


        return rating.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Integer> getRatingNow() {
        LocalDate now = LocalDate.now();
        return getRating(now.getYear(), now.getMonthValue());
    }
}