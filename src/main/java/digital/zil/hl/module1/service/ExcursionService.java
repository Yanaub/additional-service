package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.repository.ExcursionRepository;
import java.util.List;
import java.util.UUID;

public class ExcursionService {
    private final ExcursionRepository excursionRepository;

    public ExcursionService(ExcursionRepository excursionRepository) {
        this.excursionRepository = excursionRepository;
    }

    public List<Excursion> getAllExcursions() { return excursionRepository.findAll(); }
    public Excursion getExcursionById(String id) { return excursionRepository.findById(UUID.fromString(id)); }
    public Excursion saveExcursion(Excursion excursion) { return excursionRepository.save(excursion); }
    public void deleteExcursion(String id) {
        excursionRepository.delete(UUID.fromString(id));


    }
    public Excursion updateExcursion(String id, Excursion excursion) {
        excursion.setIdentifier(UUID.fromString(id));
        return excursionRepository.put(excursion);
    }
}