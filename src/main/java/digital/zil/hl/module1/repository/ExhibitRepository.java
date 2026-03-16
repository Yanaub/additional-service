package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Excursion;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import digital.zil.hl.module1.controller.exeption.ExhibitException;
import digital.zil.hl.module1.model.Exhibit;

import java.time.LocalDate;
import java.util.*;

import static java.lang.String.format;

@Repository
public class ExhibitRepository {

    public static final String EXHIBIT_NOT_FOUND_MSG = "Exhibit with ID %s not found";
    public static final String EXHIBIT_EXISTS_MSG = "Exhibit with ID %s is already exists";
    public static final String EXHIBIT_NAME_EXISTS_MSG = "Exhibit with name '%s' already exists";
    public static final String EXHIBIT_HAS_EXCURSIONS_MSG = "Cannot delete exhibit with ID %s because it is linked to existing excursions";

    private final Map<UUID, Exhibit> exhibits = new HashMap<>();
    private final Set<String> exhibitNames = new HashSet<>();

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
        exhibitNames.remove(removed.getName());
    }

    public Exhibit save(Exhibit exhibit) {
        if (ObjectUtils.isEmpty(exhibit.getIdentifier())) {
            exhibit.setIdentifier(UUID.randomUUID());
        }
        final var exhibitData = exhibits.get(exhibit.getIdentifier());
        if (exhibitData != null) {
            throw new ExhibitException(format(EXHIBIT_EXISTS_MSG, exhibit.getIdentifier()));
        }
        if (exhibitNames.contains(exhibit.getName())) {
            throw new ExhibitException(format(EXHIBIT_NAME_EXISTS_MSG, exhibit.getName()));
        }
        exhibits.put(exhibit.getIdentifier(), exhibit);
        exhibitNames.add(exhibit.getName());

        return exhibit;
    }

    public Exhibit put(Exhibit exhibit) {
        final var existingExhibit = exhibits.get(exhibit.getIdentifier());
        if (existingExhibit == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, exhibit.getIdentifier()));
        }

        if (!existingExhibit.getName().equals(exhibit.getName())) {
            if (exhibitNames.contains(exhibit.getName())) {
                throw new ExhibitException(format(EXHIBIT_NAME_EXISTS_MSG, exhibit.getName()));
            }

            exhibitNames.remove(existingExhibit.getName());
            exhibitNames.add(exhibit.getName());
        }

        exhibits.put(exhibit.getIdentifier(), exhibit);
        return exhibit;
    }



    public Map<String, Integer> getExhibitsRating(List<Excursion> excursions) {
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Map<String, Integer> visitCount = new HashMap<>();

        for (Excursion excursion : excursions) {
            LocalDate excursionDate = excursion.getDate();
            if (!excursionDate.isBefore(startDate) && !excursionDate.isAfter(endDate)) {
                UUID exhibitId = excursion.getExhibitId();

                Exhibit exhibit = exhibits.get(exhibitId);

                if (exhibit != null) {
                    visitCount.put(exhibit.getName(), visitCount.getOrDefault(exhibit.getName(), 0) + 1);
                }
            }
        }

        List<Exhibit> allExhibits = new ArrayList<>(exhibits.values());
        Map<String, Integer> result = new HashMap<>();
        for (Exhibit exhibit : allExhibits) {
            result.put(exhibit.getName(), visitCount.getOrDefault(exhibit.getName(), 0));
        }

        return result;
    }



    public void clear(){
        exhibits.clear();
        exhibitNames.clear();
    }

    public boolean existsByName(String name) {
        return exhibitNames.contains(name);
    }
}