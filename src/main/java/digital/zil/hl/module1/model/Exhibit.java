package digital.zil.hl.module1.model;

import org.springframework.lang.NonNull;

import java.util.UUID;
public class Exhibit {
    @NonNull
    private UUID identifier;
    @NonNull
    private String name;
    @NonNull
    private String epoch;
    @NonNull
    private String description;

    public Exhibit(@NonNull UUID identifier, @NonNull String name, @NonNull String epoch, @NonNull String description) {
        this.identifier = identifier;
        this.name = name;
        this.epoch = epoch;
        this.description = description;
    }

    public Exhibit() {
    }

    @NonNull
    public UUID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(@NonNull UUID identifier) {
        this.identifier = identifier;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }
    @NonNull
    public String getEpoch() {
        return epoch;
    }

    public void setEpoch(@NonNull String epoch) {
        this.epoch = epoch;
    }
    @NonNull
    public String getDescription() {
        return description;
    }
    public void setDescription(@NonNull String description) {
        this.description = description;
    }
    @Override
    public String toString() {
        return "Exhibit{" +
                "identifier=" + identifier +
                ", name='" + name  +
                ", epoch='" + epoch +
                ", description='" + description + '\'' +
                '}';
    }
}
