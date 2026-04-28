package digital.zil.hl.additional.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ExcursionDto {
    private UUID identifier;
    private LocalDate date;
    private String guide;
    private Set<ExhibitDto> exhibits = new HashSet<>();
    private Set<VisitorDto> visitors = new HashSet<>();

    public ExcursionDto() {}

    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier) { this.identifier = identifier; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getGuide() { return guide; }
    public void setGuide(String guide) { this.guide = guide; }

    public Set<ExhibitDto> getExhibits() { return exhibits; }
    public void setExhibits(Set<ExhibitDto> exhibits) { this.exhibits = exhibits; }

    public Set<VisitorDto> getVisitors() { return visitors; }
    public void setVisitors(Set<VisitorDto> visitors) { this.visitors = visitors; }
}