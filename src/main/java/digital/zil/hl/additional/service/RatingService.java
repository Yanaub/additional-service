package digital.zil.hl.additional.service;

import digital.zil.hl.additional.client.ExhibitClient;
import digital.zil.hl.additional.client.ExcursionClient;
import digital.zil.hl.additional.model.ExcursionDto;
import digital.zil.hl.additional.model.ExhibitDto;
import digital.zil.hl.additional.service.ExhibitCache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final ExhibitClient exhibitClient;
    private final ExcursionClient excursionClient;
    private final ExhibitCache exhibitCache;

    public RatingService(ExhibitClient exhibitClient, ExcursionClient excursionClient, ExhibitCache exhibitCache) {
        this.exhibitClient = exhibitClient;
        this.excursionClient = excursionClient;
        this.exhibitCache = exhibitCache;
    }

    public Map<String, Integer> getRating(Integer year, Integer month) {

        List<ExcursionDto> excursions = excursionClient.getAllExcursions();


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


        List<ExhibitDto> allExhibits = filteredExcursions.stream()
                .flatMap(excursion -> excursion.getExhibits().stream())
                .toList();


        Map<UUID, Long> countsById = allExhibits.stream()
                .collect(Collectors.groupingBy(
                        ExhibitDto::getIdentifier,
                        Collectors.counting()
                ));


        List<Map.Entry<UUID, Long>> sortedEntries = countsById.entrySet().stream()
                .sorted(Map.Entry.<UUID, Long>comparingByValue().reversed())
                .collect(Collectors.toList());


        Map<String, Integer> rating = new LinkedHashMap<>();

        for (Map.Entry<UUID, Long> entry : countsById.entrySet()) {
            UUID exhibitId = entry.getKey();
            Integer count = entry.getValue().intValue();


            ExhibitDto exhibit = exhibitCache.getExhibit(exhibitId);
            String exhibitName = exhibit.getName();

            rating.put(exhibitName, count);
        }

        return rating;
    }

    public Map<String, Integer> getRatingNow() {
        LocalDate now = LocalDate.now();
        return getRating(now.getYear(), now.getMonthValue());
    }
}