package digital.zil.hl.additional.controller;

import digital.zil.hl.additional.service.RatingService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/additional")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/rating")
    public Map<String, Integer> getRating(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month
    ) {
        return ratingService.getRating(year, month);
    }

    @GetMapping("/rating/now")
    public Map<String, Integer> getRatingNow() {
        return ratingService.getRatingNow();
    }
}