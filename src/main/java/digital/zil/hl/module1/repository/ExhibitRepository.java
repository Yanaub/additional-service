package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.model.Exhibit;
import digital.zil.hl.module1.controller.exeption.ExhibitException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Repository
public class ExhibitRepository {

    public static final String EXHIBIT_NOT_FOUND_MSG = "Exhibit with ID %s not found";
    public static final String EXHIBIT_EXISTS_MSG = "Exhibit with ID %s is already exists";
    public static final String EXHIBIT_NAME_EXISTS_MSG = "Exhibit with name '%s' already exists";
    public static final String EXHIBIT_HAS_EXCURSIONS_MSG = "Cannot delete exhibit with ID %s because it is linked to existing excursions";

    private final Map<UUID, Exhibit> exhibits = new HashMap<>();
    private final ExcursionRepository excursionRepository;

    public ExhibitRepository(ExcursionRepository excursionRepository) {
        this.excursionRepository = excursionRepository;
    }


    public List<Exhibit> findAll() {
        return new ArrayList<>(exhibits.values());
    }

    public Exhibit findById(UUID id) {
        final var exhibit = exhibits.get(id);
        if (exhibit == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, id));
        }
        return exhibit;
    }

    public void delete(UUID id) {
        final var removed = exhibits.get(id);
        if (removed == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, id));
        }

        boolean hasExcursions = excursionRepository.findAll().stream()
                .anyMatch(excursion -> excursion.getExhibitId().equals(id));

        if (hasExcursions) {
            throw new ExhibitException(format(EXHIBIT_HAS_EXCURSIONS_MSG, id));
        }

        exhibits.remove(id);

    }

    public Exhibit save(Exhibit exhibit) {
        if (ObjectUtils.isEmpty(exhibit.getIdentifier())) {
            exhibit.setIdentifier(UUID.randomUUID());
        }

        if (exhibits.containsKey(exhibit.getIdentifier())) {
            throw new ExhibitException(format(EXHIBIT_EXISTS_MSG, exhibit.getIdentifier()));
        }


        if (existsByName(exhibit.getName())) {
            throw new ExhibitException(format(EXHIBIT_NAME_EXISTS_MSG, exhibit.getName()));
        }

        exhibits.put(exhibit.getIdentifier(), exhibit);
        return exhibit;
    }

    public Exhibit put(Exhibit exhibit) {
        final var existingExhibit = exhibits.get(exhibit.getIdentifier());
        if (existingExhibit == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, exhibit.getIdentifier()));
        }


        if (!existingExhibit.getName().equals(exhibit.getName())
                && existsByNameExcluding(exhibit.getName(), exhibit.getIdentifier())) {
            throw new ExhibitException(format(EXHIBIT_NAME_EXISTS_MSG, exhibit.getName()));
        }

        exhibits.put(exhibit.getIdentifier(), exhibit);
        return exhibit;
    }

    public Map<String, Integer> getExhibitsRating(List<Excursion> excursions) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Инициализируем результат нулями для всех экспонатов
        Map<String, Integer> result = exhibits.values().stream()
                .collect(Collectors.toMap(Exhibit::getName, e -> 0));

        // Добавляем посещения за месяц
        excursions.stream()
                .filter(e -> !e.getDate().isBefore(startDate) && !e.getDate().isAfter(endDate))
                .map(Excursion::getExhibitId)
                .map(exhibits::get)
                .filter(Objects::nonNull)
                .forEach(exhibit ->
                        result.merge(exhibit.getName(), 1, Integer::sum)
                );

        return result;
    }

    public void clear() {
        exhibits.clear();
    }


    public boolean existsByName(String name) {
        return existsByNameExcluding(name, null);
    }


    private boolean existsByNameExcluding(String name, UUID excludeId) {
        return exhibits.values().stream()
                .filter(e -> excludeId == null || !e.getIdentifier().equals(excludeId))
                .anyMatch(e -> Objects.equals(e.getName(), name));
    }
}