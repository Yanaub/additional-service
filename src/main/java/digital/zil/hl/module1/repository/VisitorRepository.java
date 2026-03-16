package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.controller.exeption.ExhibitException;
import digital.zil.hl.module1.controller.exeption.VisitorException;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import digital.zil.hl.module1.model.Visitor;
import java.util.*;
import static java.lang.String.format;

@Repository
public class VisitorRepository {
    public static final String VISITOR_NOT_FOUND_MSG = "Visitor with ID %s not found";
    public static final String VISITOR_EXISTS_MSG = "Visitor with ID %s already exists";
    public static final String EXHIBIT_HAS_EXCURSIONS_MSG = "Cannot delete visitor with ID %s because it is linked to existing excursions";
    private final Map<UUID, Visitor> visitors = new HashMap<>();
    private final ExcursionRepository excursionRepository;

    public VisitorRepository(ExcursionRepository excursionRepository) {
        this.excursionRepository = excursionRepository;
    }

    public List<Visitor> findAll() { return new ArrayList<>(visitors.values()); }

    public Visitor findById(UUID id) {
        final var visitor = visitors.get(id);
        if (visitor == null) throw new VisitorException(format(VISITOR_NOT_FOUND_MSG, id));
        return visitor;
    }

    public void delete(UUID id) {
        final var removed = visitors.get(id);
        if (removed == null) {
            throw new ExhibitException(format(VISITOR_NOT_FOUND_MSG, id));

        }
        boolean hasExcursions = excursionRepository.findAll().stream()
                .anyMatch(excursion -> excursion.getVisitorId().equals(id));
        if (hasExcursions) {
            throw new ExhibitException(format(EXHIBIT_HAS_EXCURSIONS_MSG, id));
        }
        visitors.remove(id);
    }

    public Visitor save(Visitor visitor) {
        if (ObjectUtils.isEmpty(visitor.getIdentifier())) visitor.setIdentifier(UUID.randomUUID());
        if (visitors.get(visitor.getIdentifier()) != null)
            throw new VisitorException(format(VISITOR_EXISTS_MSG, visitor.getIdentifier()));
        visitors.put(visitor.getIdentifier(), visitor);
        return visitor;
    }

    public Visitor put(Visitor visitor) {
        if (visitors.get(visitor.getIdentifier()) == null)
            throw new VisitorException(format(VISITOR_NOT_FOUND_MSG, visitor.getIdentifier()));
        visitors.put(visitor.getIdentifier(), visitor);
        return visitor;
    }

    public void clear() { visitors.clear(); }
}