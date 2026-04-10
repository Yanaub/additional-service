package digital.zil.hl.module1.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "exhibits")
public class Exhibit {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID identifier;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    @NonNull
    @Column(nullable = false)
    private String epoch;

    @NonNull
    @Column(nullable = false)
    private String description;

    @ManyToMany(mappedBy = "exhibits")
    private Set<Excursion> excursions = new HashSet<>();

    public Exhibit(@NonNull UUID identifier, @NonNull String name,
                   @NonNull String epoch, @NonNull String description) {
        this.identifier = identifier;
        this.name = name;
        this.epoch = epoch;
        this.description = description;
    }

    public Exhibit() {}

    // геттеры/сеттеры без изменений
    @NonNull public UUID getIdentifier() { return identifier; }
    public void setIdentifier(@NonNull UUID identifier) { this.identifier = identifier; }
    @NonNull public String getName() { return name; }
    public void setName(@NonNull String name) { this.name = name; }
    @NonNull public String getEpoch() { return epoch; }
    public void setEpoch(@NonNull String epoch) { this.epoch = epoch; }
    @NonNull public String getDescription() { return description; }
    public void setDescription(@NonNull String description) { this.description = description; }

    @Override
    public String toString() {
        return "Exhibit{identifier=" + identifier + ", name='" + name +
                "', epoch='" + epoch + "', description='" + description + "'}";
    }
}