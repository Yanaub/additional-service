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

    public Excursion save2Excursion(Excursion dto) {
        Excursion excursion = new Excursion();
        excursion.setIdentifier(UUID.randomUUID());
        excursion.setDate(dto.getDate());
        excursion.setGuide(dto.getGuide());

        // Экспонаты: берём только ID из dto, подгружаем из БД
        Set<Exhibit> exhibits = dto.getExhibits().stream()
                .map(e -> exhibitRepository.findById(e.getIdentifier())
                        .orElseThrow(() -> new RuntimeException("Exhibit not found: " + e.getIdentifier())))
                .collect(Collectors.toSet());
        excursion.setExhibits(exhibits);

        // Посетители: ТО ЖЕ САМОЕ, по ID из dto
        Set<Visitor> visitors = dto.getVisitors().stream()
                .map(v -> visitorRepository.findById(v.getIdentifier())
                        .orElseThrow(() -> new RuntimeException("Visitor not found: " + v.getIdentifier())))
                .collect(Collectors.toSet());
        excursion.setVisitors(visitors);

        return excursionRepository.save(excursion);
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
}