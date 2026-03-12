package digital.zil.hl.module1.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import digital.zil.hl.module1.model.Exhibit;
import digital.zil.hl.module1.model.Visitor;
import digital.zil.hl.module1.model.Excursion;
import digital.zil.hl.module1.repository.ExhibitRepository;
import digital.zil.hl.module1.repository.VisitorRepository;
import digital.zil.hl.module1.repository.ExcursionRepository;
import digital.zil.hl.module1.service.ExhibitService;
import digital.zil.hl.module1.service.VisitorService;
import digital.zil.hl.module1.service.ExcursionService;
import digital.zil.hl.module1.service.StatisticsService;

import java.time.LocalDate;
import java.util.UUID;

@Configuration
public class ServicesConfig {

    @Bean
    ExhibitService exhibitService(ExhibitRepository exhibitRepository) {
        ExhibitService exhibitService = new ExhibitService(exhibitRepository);

        // Добавляем тестовые данные
        for (int i = 0; i < 5; i++) {
            Exhibit exhibit = new Exhibit();
            exhibit.setIdentifier(UUID.randomUUID());
            exhibit.setName("Exhibit " + i);
            exhibit.setEpoch("Epoch " + i);
            exhibit.setDescription("Description for exhibit " + i);
            exhibitRepository.save(exhibit);
        }

        return exhibitService;
    }

    @Bean
    VisitorService visitorService(VisitorRepository visitorRepository) {
        VisitorService visitorService = new VisitorService(visitorRepository);

        // Добавляем тестовые данные
        for (int i = 0; i < 3; i++) {
            Visitor visitor = new Visitor();
            visitor.setIdentifier(UUID.randomUUID());
            visitor.setFullName("Visitor " + i);
            visitor.setAge(20 + i);
            visitor.setTicketType(i % 2 == 0 ? Visitor.TicketType.FULL : Visitor.TicketType.DISCOUNTED);
            visitorRepository.save(visitor);
        }

        return visitorService;
    }

    @Bean
    ExcursionService excursionService(ExcursionRepository excursionRepository) {
        ExcursionService excursionService = new ExcursionService(excursionRepository);
        return excursionService;
    }

    @Bean
    @DependsOn({"exhibitService", "visitorService"})
    public Object initExcursions(ExcursionRepository excursionRepository,
                                 ExhibitService exhibitService,
                                 VisitorService visitorService) {

        var exhibits = exhibitService.getAllExhibits();
        var visitors = visitorService.getAllVisitors();

        if (!exhibits.isEmpty() && !visitors.isEmpty()) {
            for (int i = 0; i < Math.min(3, exhibits.size()); i++) {
                Excursion excursion = new Excursion();
                excursion.setIdentifier(UUID.randomUUID());
                excursion.setExhibitId(exhibits.get(i).getIdentifier());
                excursion.setVisitorId(visitors.get(i % visitors.size()).getIdentifier());
                excursion.setDate(LocalDate.now().plusDays(i));
                excursion.setGuide("Guide " + (i + 1));
                excursionRepository.save(excursion);
            }
        }

        return null;
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console2000")
    StatisticsService statisticsService2000(ExhibitService exhibitService,
                                            VisitorService visitorService,
                                            ExcursionService excursionService) {
        return new StatisticsService(2000, exhibitService, visitorService, excursionService);
    }

    @Bean
    @ConditionalOnProperty(prefix = "statistics", name = "service", havingValue = "console1000")
    StatisticsService statisticsService1000(ExhibitService exhibitService,
                                            VisitorService visitorService,
                                            ExcursionService excursionService) {
        return new StatisticsService(1000, exhibitService, visitorService, excursionService);
    }
}