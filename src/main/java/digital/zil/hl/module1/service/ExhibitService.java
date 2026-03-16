package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.model.Exhibit;
import digital.zil.hl.module1.repository.ExcursionRepository;
import digital.zil.hl.module1.repository.ExhibitRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExhibitService {

    private final ExhibitRepository exhibitRepository;
    private final ExcursionRepository excursionRepository;

    public ExhibitService(ExhibitRepository exhibitRepository, ExcursionRepository excursionRepository) {
        this.exhibitRepository = exhibitRepository;
        this.excursionRepository = excursionRepository;
    }

    public List<Exhibit> getAllExhibits() {
        return exhibitRepository.findAll();
    }
    public Map<String, Integer> ratingExhibits() {
        return exhibitRepository.getExhibitsRating(excursionRepository.findAll());
    }

    public Exhibit getExhibitById(String id) {
        return exhibitRepository.findById(UUID.fromString(id));
    }

    public Exhibit saveExhibit(Exhibit exhibit) {
        return exhibitRepository.save(exhibit);
    }

    public void deleteExhibit(String id) {
       exhibitRepository.delete(UUID.fromString(id));



    }

    public Exhibit updateExhibit(String id, Exhibit exhibit) {
        exhibit.setIdentifier(UUID.fromString(id));
        return exhibitRepository.put(exhibit);
    }


}