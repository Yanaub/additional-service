package digital.zil.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.service.ExcursionService;
import java.util.List;

@RestController
public class ExcursionController {
    private final ExcursionService excursionService;

    @Autowired
    public ExcursionController(ExcursionService excursionService) {
        this.excursionService = excursionService;
    }

    @GetMapping("/excursions")
    public List<Excursion> getExcursions() { return excursionService.getAllExcursions(); }

    @GetMapping("/excursions/{id}")
    public Excursion getExcursionById(@PathVariable String id) { return excursionService.getExcursionById(id); }

    @DeleteMapping("/excursions/{id}")
    public void deleteExcursion(@PathVariable String id) { excursionService.deleteExcursion(id); }

    @PostMapping("/excursions/")
    public Excursion saveExcursion(@RequestBody Excursion excursion) { return excursionService.saveExcursion(excursion); }

    @PutMapping("/excursions/{id}")
    public Excursion updateExcursion(@PathVariable String id, @RequestBody Excursion excursion) {
        return excursionService.updateExcursion(id, excursion);
    }

    @PostMapping("/excursions/{id}/visitors/{visitorId}")
    public Excursion addVisitor(
            @PathVariable String id,
            @PathVariable String visitorId) {
        return excursionService.addVisitor(id, visitorId);
    }

    @DeleteMapping("/excursions/{id}/visitors/{visitorId}")
    public Excursion removeVisitor(
            @PathVariable String id,
            @PathVariable String visitorId) {
        return excursionService.removeVisitor(id, visitorId);
    }

    @PostMapping("/excursions/{id}/exhibits/{exhibitId}")
    public Excursion addExhibit(
            @PathVariable String id,
            @PathVariable String exhibitId) {
        return excursionService.addExhibit(id, exhibitId);
    }

    @DeleteMapping("/excursions/{id}/exhibits/{exhibitId}")
    public Excursion removeExhibit(
            @PathVariable String id,
            @PathVariable String exhibitId) {
        return excursionService.removeExhibit(id, exhibitId);
    }
}