package digital.zil.hl.module1.service;

import digital.zil.hl.module1.client.ExhibitClient;
import digital.zil.hl.module1.client.ExcursionClient;
import digital.zil.hl.module1.model.ExcursionDto;
import digital.zil.hl.module1.model.ExhibitDto;
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
        // Шаг 1: Запрашиваем данные из основного сервиса (два отдельных HTTP-запроса)
        List<ExhibitDto> exhibits = exhibitClient.getAllExhibits();
        List<ExcursionDto> excursions = excursionClient.getAllExcursions();

        // Шаг 2: Java JOIN - инициализируем рейтинг всех экспонатов нулями
        Map<String, Integer> rating = exhibits.stream()
                .collect(Collectors.toMap(
                        ExhibitDto::getName,
                        exhibit -> 0,
                        (existing, replacement) -> existing,
                        LinkedHashMap::new
                ));

        // Шаг 3: Фильтрация экскурсий по периоду (логика из ExhibitService.ratingExhibits)
        List<ExcursionDto> filteredExcursions;

        if (year == null && month == null) {
            // Без фильтра - все экскурсии
            filteredExcursions = excursions;
        } else if (year != null && month != null) {
            // Фильтруем по конкретному месяцу
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

        // Шаг 4: Подсчёт посещаемости - JOIN на стороне Java (не в БД)
        filteredExcursions.stream()
                .flatMap(excursion -> excursion.getExhibits().stream())
                .forEach(exhibit -> rating.merge(exhibit.getName(), 1, Integer::sum));

        // Шаг 5: Сортировка по убыванию посещаемости
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