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
}