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
    ExhibitService exhibitService(ExhibitRepository exhibitRepository, ExcursionRepository excursionRepository) {
        return new ExhibitService(exhibitRepository, excursionRepository);
    }

    @Bean
    VisitorService visitorService(VisitorRepository visitorRepository, ExcursionRepository excursionRepository) {
        return new VisitorService(visitorRepository, excursionRepository);
    }

    @Bean
    ExcursionService excursionService(ExcursionRepository excursionRepository,
                                      ExhibitRepository exhibitRepository,
                                      VisitorRepository visitorRepository) {
        return new ExcursionService(excursionRepository,
                exhibitRepository,
                visitorRepository);
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