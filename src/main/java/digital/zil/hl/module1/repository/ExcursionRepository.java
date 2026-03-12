package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.controller.exeption.ExcursionException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import digital.zil.hl.module1.model.Excursion;
import java.util.*;
import static java.lang.String.format;

@Repository
public class ExcursionRepository {
    public static final String EXCURSION_NOT_FOUND_MSG = "Excursion with ID %s not found";
    public static final String EXCURSION_EXISTS_MSG = "Excursion with ID %s already exists";
    private final Map<UUID, Excursion> excursions = new HashMap<>();

    public List<Excursion> findAll() { return new ArrayList<>(excursions.values()); }

    public Excursion findById(UUID id) {
        final var excursion = excursions.get(id);
        if (excursion == null) throw new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, id));
        return excursion;
    }

    public void delete(UUID id) {
        final var removed = excursions.remove(id);
        if (removed == null) throw new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, id));
    }

    public Excursion save(Excursion excursion) {
        if (ObjectUtils.isEmpty(excursion.getIdentifier())) excursion.setIdentifier(UUID.randomUUID());
        if (excursions.get(excursion.getIdentifier()) != null)
            throw new ExcursionException(format(EXCURSION_EXISTS_MSG, excursion.getIdentifier()));
        excursions.put(excursion.getIdentifier(), excursion);
        return excursion;
    }

    public Excursion put(Excursion excursion) {
        if (excursions.get(excursion.getIdentifier()) == null)
            throw new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, excursion.getIdentifier()));
        excursions.put(excursion.getIdentifier(), excursion);
        return excursion;
    }

    public void clear() { excursions.clear(); }
}