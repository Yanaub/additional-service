package digital.zil.hl.module1.model;

import org.springframework.lang.NonNull;
import java.time.LocalDate;
import java.util.UUID;

public class Excursion {
    @NonNull
    private UUID identifier;
    @NonNull
    private UUID exhibitId;
    @NonNull
    private UUID visitorId;
    @NonNull
    private LocalDate date;
    @NonNull
    private String guide;

    public Excursion(@NonNull UUID identifier, @NonNull UUID exhibitId, @NonNull UUID visitorId,
                     @NonNull LocalDate date, @NonNull String guide) {
        this.identifier = identifier;
        this.exhibitId = exhibitId;
        this.visitorId = visitorId;
        this.date = date;
        this.guide = guide;
    }

    public Excursion() {}

    @NonNull
    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(@NonNull UUID identifier) { this.identifier = identifier; }
    @NonNull
    public UUID getExhibitId() { return exhibitId; }
    public void setExhibitId(@NonNull UUID exhibitId) { this.exhibitId = exhibitId; }
    @NonNull
    public UUID getVisitorId() { return visitorId; }
    public void setVisitorId(@NonNull UUID visitorId) { this.visitorId = visitorId; }
    @NonNull
    public LocalDate getDate() { return date; }
    public void setDate(@NonNull LocalDate date) { this.date = date; }
    @NonNull
    public String getGuide() { return guide; }
    public void setGuide(@NonNull String guide) { this.guide = guide; }

    @Override
    public String toString() {
        return "Excursion{" +
                "identifier=" + identifier +
                ", exhibitId=" + exhibitId +
                ", visitorId=" + visitorId +
                ", date=" + date +
                ", guide='" + guide + '\'' +
                '}';
    }
}