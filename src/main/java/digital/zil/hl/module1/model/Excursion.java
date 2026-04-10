package digital.zil.hl.module1.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "excursions")
public class Excursion {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID identifier;

    @NonNull
    @Column(nullable = false)
    private LocalDate date;

    @NonNull
    @Column(nullable = false)
    private String guide;


    @ManyToMany
    @JoinTable(
            name = "excursion_exhibits",
            joinColumns = @JoinColumn(name = "excursion_id", referencedColumnName = "identifier"),
            inverseJoinColumns = @JoinColumn(name = "exhibit_id", referencedColumnName = "identifier")
    )
    private Set<Exhibit> exhibits = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "excursion_visitors",
            joinColumns = @JoinColumn(name = "excursion_id", referencedColumnName = "identifier"),
            inverseJoinColumns = @JoinColumn(name = "visitor_id", referencedColumnName = "identifier")
    )
    private Set<Visitor> visitors = new HashSet<>();

    public Excursion(@NonNull UUID identifier,  @NonNull LocalDate date, @NonNull String guide) {
        this.identifier = identifier;

        this.date = date;
        this.guide = guide;
    }

    public Excursion() {}

    // геттеры/сеттеры без изменений
    @NonNull public UUID getIdentifier() { return identifier; }
    public void setIdentifier(@NonNull UUID identifier) { this.identifier = identifier; }

    @NonNull public LocalDate getDate() { return date; }
    public void setDate(@NonNull LocalDate date) { this.date = date; }
    @NonNull public String getGuide() { return guide; }
    public void setGuide(@NonNull String guide) { this.guide = guide; }
    public Set<Exhibit> getExhibits() {
        return exhibits;
    }

    public void setExhibits(Set<Exhibit> exhibits) {
        this.exhibits = exhibits;
    }

    public Set<Visitor> getVisitors() {
        return visitors;
    }

    public void setVisitors(Set<Visitor> visitors) {
        this.visitors = visitors;
    }

    @Override
    public String toString() {
        return "Excursion{identifier=" + identifier + ", date=" + date + ", guide='" + guide + "'}";
    }
}