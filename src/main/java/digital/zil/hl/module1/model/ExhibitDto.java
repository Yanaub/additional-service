package digital.zil.hl.module1.model;

import java.util.UUID;

public class ExhibitDto {
    private UUID identifier;
    private String name;
    private String epoch;
    private String description;

    public ExhibitDto() {}

    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier) { this.identifier = identifier; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEpoch() { return epoch; }
    public void setEpoch(String epoch) { this.epoch = epoch; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}