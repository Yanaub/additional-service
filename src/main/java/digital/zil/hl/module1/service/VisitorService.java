package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.VisitorException;
import digital.zil.hl.module1.model.Visitor;
import digital.zil.hl.module1.repository.ExcursionRepository;
import digital.zil.hl.module1.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class VisitorService {

    public static final String VISITOR_NOT_FOUND_MSG = "Visitor with ID %s not found";
    public static final String VISITOR_EXISTS_MSG = "Visitor with ID %s already exists";
    public static final String VISITOR_HAS_EXCURSIONS_MSG = "Cannot delete visitor with ID %s because it has excursions";

    private final VisitorRepository visitorRepository;
    private final ExcursionRepository excursionRepository;

    public VisitorService(VisitorRepository visitorRepository,
                          ExcursionRepository excursionRepository) {
        this.visitorRepository = visitorRepository;
        this.excursionRepository = excursionRepository;
    }

    public List<Visitor> getAllVisitors() {
        return visitorRepository.findAll();
    }

    public Visitor getVisitorById(String id) {
        return visitorRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new VisitorException(format(VISITOR_NOT_FOUND_MSG, id)));
    }

    public Visitor saveVisitor(Visitor visitor) {
        if (visitor.getIdentifier() == null) {
            visitor.setIdentifier(UUID.randomUUID());
        }
        if (visitorRepository.existsById(visitor.getIdentifier())) {
            throw new VisitorException(format(VISITOR_EXISTS_MSG, visitor.getIdentifier()));
        }
        return visitorRepository.save(visitor);
    }
    boolean hasExcursionsForVisitor(UUID visitorId) {
        return excursionRepository.findAll().stream()
                .anyMatch(ex -> ex.getVisitors().stream()
                        .anyMatch(v -> v.getIdentifier().equals(visitorId)));
    }
    public void deleteVisitor(String id) {
        UUID uuid = UUID.fromString(id);
        if (!visitorRepository.existsById(uuid)) {
            throw new VisitorException(format(VISITOR_NOT_FOUND_MSG, id));
        }
        if (hasExcursionsForVisitor(uuid)) {
            throw new VisitorException(format(VISITOR_HAS_EXCURSIONS_MSG, id));
        }
        visitorRepository.deleteById(uuid);
    }

    public Visitor updateVisitor(String id, Visitor visitor) {
        UUID uuid = UUID.fromString(id);
        if (!visitorRepository.existsById(uuid)) {
            throw new VisitorException(format(VISITOR_NOT_FOUND_MSG, id));
        }
        visitor.setIdentifier(uuid);
        return visitorRepository.save(visitor);
    }
    public void deleteAll() {
        visitorRepository.deleteAll();
    }
}