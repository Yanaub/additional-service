package digital.zil.hl.module1.repository;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import digital.zil.hl.module1.controller.exeption.ExhibitException;
import digital.zil.hl.module1.model.Exhibit;

import java.util.*;

import static java.lang.String.format;

@Repository
public class ExhibitRepository {

    public static final String EXHIBIT_NOT_FOUND_MSG = "Exhibit with ID %s not found";
    public static final String EXHIBIT_EXISTS_MSG = "Exhibit with ID %s is already exists";

    private final Map<UUID, Exhibit> exhibits = new HashMap<>();

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
        final var removed = exhibits.remove(id);
        if (removed == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, id));
        }
    }

    public Exhibit save(Exhibit exhibit) {
        if (ObjectUtils.isEmpty(exhibit.getIdentifier())) {
            exhibit.setIdentifier(UUID.randomUUID());
        }

        final var exhibitData = exhibits.get(exhibit.getIdentifier());
        if (exhibitData != null) {
            throw new ExhibitException(format(EXHIBIT_EXISTS_MSG, exhibit.getIdentifier()));
        }

        exhibits.put(exhibit.getIdentifier(), exhibit);

        return exhibit;
    }

    public Exhibit put(Exhibit exhibit) {
        final var exhibitData = exhibits.get(exhibit.getIdentifier());
        if (exhibitData == null) {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, exhibit.getIdentifier()));
        }

        final var removed = exhibits.remove(exhibit.getIdentifier());
        if (removed != null) {
            exhibits.put(exhibit.getIdentifier(), exhibit);
        } else {
            throw new ExhibitException(format(EXHIBIT_NOT_FOUND_MSG, exhibit.getIdentifier()));
        }

        return exhibit;
    }

    public void clear(){
        exhibits.clear();
    }

}