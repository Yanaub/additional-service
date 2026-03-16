package digital.zil.hl.module1.service;

import digital.zil.hl.module1.model.Visitor;
import digital.zil.hl.module1.repository.ExcursionRepository;
import digital.zil.hl.module1.repository.VisitorRepository;
import java.util.List;
import java.util.UUID;

public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final ExcursionRepository excursionRepository;
    public VisitorService(VisitorRepository visitorRepository, ExcursionRepository excursionRepository) {
        this.visitorRepository = visitorRepository;
        this.excursionRepository = excursionRepository;
    }

    public List<Visitor> getAllVisitors() { return visitorRepository.findAll(); }

    public Visitor getVisitorById(String id) { return visitorRepository.findById(UUID.fromString(id)); }

    public Visitor saveVisitor(Visitor visitor) { return visitorRepository.save(visitor); }

    public void deleteVisitor(String id) {

        visitorRepository.delete(UUID.fromString(id));
        }

    public Visitor updateVisitor(String id, Visitor visitor) {
        visitor.setIdentifier(UUID.fromString(id));
        return visitorRepository.put(visitor);
    }
}