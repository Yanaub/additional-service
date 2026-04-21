package digital.zil.hl.module1.service;

import digital.zil.hl.module1.controller.exeption.ExcursionException;
import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.model.Exhibit;
import digital.zil.hl.module1.model.Visitor;
import digital.zil.hl.module1.repository.ExcursionRepository;
import digital.zil.hl.module1.repository.ExhibitRepository;
import digital.zil.hl.module1.repository.VisitorRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
public class ExcursionService {

    public static final String EXCURSION_NOT_FOUND_MSG = "Excursion with ID %s not found";
    public static final String EXCURSION_EXISTS_MSG = "Excursion with ID %s already exists";
    public static final String VISITOR_NOT_FOUND_MSG = "Visitor with ID %s not found";
    public static final String EXHIBIT_NOT_FOUND_MSG = "Exhibit with ID %s not found";

    private final ExcursionRepository excursionRepository;
    private final ExhibitRepository exhibitRepository;
    private final VisitorRepository visitorRepository;

    public ExcursionService(ExcursionRepository excursionRepository,
                            ExhibitRepository exhibitRepository,
                            VisitorRepository visitorRepository) {
        this.excursionRepository = excursionRepository;
        this.exhibitRepository = exhibitRepository;
        this.visitorRepository = visitorRepository;
    }


    public List<Excursion> getAllExcursions() {
        return excursionRepository.findAll();
    }

    public Excursion getExcursionById(String id) {
        return excursionRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, id)));
    }

    public Excursion saveExcursion(Excursion excursion) {
        if (excursion.getIdentifier() == null) {
            excursion.setIdentifier(UUID.randomUUID());
        }
        if (excursionRepository.existsById(excursion.getIdentifier())) {
            throw new ExcursionException(format(EXCURSION_EXISTS_MSG, excursion.getIdentifier()));
        }
        return excursionRepository.save(excursion);
    }

    public void deleteExcursion(String id) {
        UUID uuid = UUID.fromString(id);
        if (!excursionRepository.existsById(uuid)) {
            throw new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, id));
        }
        excursionRepository.deleteById(uuid);
    }

    public Excursion updateExcursion(String id, Excursion excursion) {
        UUID uuid = UUID.fromString(id);
        if (!excursionRepository.existsById(uuid)) {
            throw new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, id));
        }
        excursion.setIdentifier(uuid);
        return excursionRepository.save(excursion);
    }

    public Excursion addVisitor(String excursionId, String visitorId) {
        Excursion excursion = excursionRepository.findById(UUID.fromString(excursionId))
                .orElseThrow(() -> new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, excursionId)));
        Visitor visitor = visitorRepository.findById(UUID.fromString(visitorId))
                .orElseThrow(() -> new ExcursionException(format(VISITOR_NOT_FOUND_MSG, visitorId)));
        excursion.getVisitors().add(visitor);
        return excursionRepository.save(excursion);
    }

    public Excursion removeVisitor(String excursionId, String visitorId) {
        Excursion excursion = excursionRepository.findById(UUID.fromString(excursionId))
                .orElseThrow(() -> new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, excursionId)));
        Visitor visitor = visitorRepository.findById(UUID.fromString(visitorId))
                .orElseThrow(() -> new ExcursionException(format(VISITOR_NOT_FOUND_MSG, visitorId)));
        excursion.getVisitors().remove(visitor);
        return excursionRepository.save(excursion);
    }

    public Excursion addExhibit(String excursionId, String exhibitId) {
        Excursion excursion = excursionRepository.findById(UUID.fromString(excursionId))
                .orElseThrow(() -> new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, excursionId)));
        Exhibit exhibit = exhibitRepository.findById(UUID.fromString(exhibitId))
                .orElseThrow(() -> new ExcursionException(format(EXHIBIT_NOT_FOUND_MSG, exhibitId)));
        excursion.getExhibits().add(exhibit);
        return excursionRepository.save(excursion);
    }

    public Excursion removeExhibit(String excursionId, String exhibitId) {
        Excursion excursion = excursionRepository.findById(UUID.fromString(excursionId))
                .orElseThrow(() -> new ExcursionException(format(EXCURSION_NOT_FOUND_MSG, excursionId)));
        Exhibit exhibit = exhibitRepository.findById(UUID.fromString(exhibitId))
                .orElseThrow(() -> new ExcursionException(format(EXHIBIT_NOT_FOUND_MSG, exhibitId)));
        excursion.getExhibits().remove(exhibit);
        return excursionRepository.save(excursion);
    }
}