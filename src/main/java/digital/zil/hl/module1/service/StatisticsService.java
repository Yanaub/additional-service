package digital.zil.hl.module1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

public class StatisticsService {

    @Value("${statisticsservice.infostring:lines}")
    private String infoString;

    final int delay;

    private final ExhibitService exhibitService;
    private final VisitorService visitorService;
    private final ExcursionService excursionService;

    public StatisticsService(int delay, ExhibitService exhibitService,
                             VisitorService visitorService, ExcursionService excursionService) {
        this.delay = delay;
        this.exhibitService = exhibitService;
        this.visitorService = visitorService;
        this.excursionService = excursionService;
    }

    @Async(value = "applicationTaskExecutor")
    @Scheduled(fixedRateString = "${fixedRate.in.milliseconds}")
    public void scheduleFixedRateTaskAsync() throws InterruptedException {
        System.out.println(
                Thread.currentThread().getName() + " - Fixed rate task async - " + delay + " - " + infoString + " - " +
                        "Exhibits: " + exhibitService.getAllExhibits().size() + ", " +
                        "Visitors: " + visitorService.getAllVisitors().size() + ", " +
                        "Excursions: " + excursionService.getAllExcursions().size());
        Thread.sleep(delay);
    }
}