package digital.zil.hl.module1.model;

import java.util.UUID;

public class VisitorDto {
    private UUID identifier;
    private String fullName;
    private Integer age;
    private String ticketType;

    public VisitorDto() {}

    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(UUID identifier) { this.identifier = identifier; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getTicketType() { return ticketType; }
    public void setTicketType(String ticketType) { this.ticketType = ticketType; }
}