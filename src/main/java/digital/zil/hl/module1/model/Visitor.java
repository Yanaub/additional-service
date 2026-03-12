package digital.zil.hl.module1.model;


import org.springframework.lang.NonNull;
import java.util.UUID;

public class Visitor {
    @NonNull
    private UUID identifier;
    @NonNull
    private String fullName;
    private int age;
    @NonNull
    private TicketType ticketType;

    public enum TicketType {
        FULL, DISCOUNTED
    }

    public Visitor(@NonNull UUID identifier, @NonNull String fullName, int age, @NonNull TicketType ticketType) {
        this.identifier = identifier;
        this.fullName = fullName;
        this.age = age;
        this.ticketType = ticketType;
    }

    public Visitor() {}

    @NonNull
    public UUID getIdentifier() { return identifier; }
    public void setIdentifier(@NonNull UUID identifier) { this.identifier = identifier; }
    @NonNull
    public String getFullName() { return fullName; }
    public void setFullName(@NonNull String fullName) { this.fullName = fullName; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    @NonNull
    public TicketType getTicketType() { return ticketType; }
    public void setTicketType(@NonNull TicketType ticketType) { this.ticketType = ticketType; }

    @Override
    public String toString() {
        return "Visitor{" +
                "identifier=" + identifier +
                ", fullName='" + fullName  +
                ", age=" + age +
                ", ticketType=" + ticketType + '\''+
                '}';
    }
}