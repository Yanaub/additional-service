package digital.zil.hl.additional.service;

import digital.zil.hl.additional.client.ExhibitClient;
import digital.zil.hl.additional.model.ExhibitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ExhibitCache {

    private static final Logger log = LoggerFactory.getLogger(ExhibitCache.class);

    private final ExhibitClient exhibitClient;

    private final Map<UUID, ExhibitDto> cache = new HashMap<>();

    public ExhibitCache(ExhibitClient exhibitClient) {
        this.exhibitClient = exhibitClient;
    }


    public ExhibitDto getExhibit(UUID id) {
        if (!cache.containsKey(id)) {
            ExhibitDto exhibit = exhibitClient.getExhibitById(id);
            cache.put(id, exhibit);
        }
        return cache.get(id);
    }


    public void invalidate() {
        cache.clear();
    }


    @Scheduled(fixedRate = 60000)
    public void printCacheStats() {
        log.info("ExhibitCache size: {}", cache.size());
    }
}