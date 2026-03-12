package digital.zil.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import digital.zil.hl.module1.model.Visitor;
import digital.zil.hl.module1.service.VisitorService;
import java.util.List;

@RestController
public class VisitorController {
    private final VisitorService visitorService;

    @Autowired
    public VisitorController(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @GetMapping("/visitors")
    public List<Visitor> getVisitors() { return visitorService.getAllVisitors(); }

    @GetMapping("/visitors/{id}")
    public Visitor getVisitorById(@PathVariable String id) { return visitorService.getVisitorById(id); }

    @DeleteMapping("/visitors/{id}")
    public void deleteVisitor(@PathVariable String id) { visitorService.deleteVisitor(id); }

    @PostMapping("/visitors/")
    public Visitor saveVisitor(@RequestBody Visitor visitor) { return visitorService.saveVisitor(visitor); }

    @PutMapping("/visitors/{id}")
    public Visitor updateVisitor(@PathVariable String id, @RequestBody Visitor visitor) {
        return visitorService.updateVisitor(id, visitor);
    }
}