package digital.zil.hl.additional.service;

import digital.zil.hl.additional.client.ExhibitClient;
import digital.zil.hl.additional.model.ExhibitDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant; 
import java.time.Duration; 
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ExhibitCache {

    private static final Logger log = LoggerFactory.getLogger(ExhibitCache.class);
    private static final Duration TTL = Duration.ofMinutes(5);

    private final ExhibitClient exhibitClient;
    private final Map<UUID, CachedItem> cache = new HashMap<>();
    private record CachedItem(Instant addedAt, ExhibitDto exhibit) {}

    public ExhibitCache(ExhibitClient exhibitClient) {
        this.exhibitClient = exhibitClient;
    }

    public ExhibitDto getExhibit(UUID id) {
        CachedItem item = cache.get(id);

        if (item != null && !isExpired(item.addedAt())) {
            return item.exhibit();
        }
        ExhibitDto exhibit = exhibitClient.getExhibitById(id);
        cache.put(id, new CachedItem(Instant.now(), exhibit));
        return exhibit;
    }

    private boolean isExpired(Instant addedAt) {
        return addedAt.plus(TTL).isBefore(Instant.now());
    }


    public void invalidate() {
        cache.clear();
    }
    

    @Scheduled(fixedRate = 60000)
    public void printCacheStats() {
        cache.values().removeIf(item -> isExpired(item.addedAt()));
        log.info("ExhibitCache size: {}", cache.size());
    }
}