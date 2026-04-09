package digital.zil.hl.module1.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;
import java.util.UUID;

@Entity
@Table(name = "visitors")
public class Visitor {

    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID identifier;

    @NonNull
    @Column(nullable = false)
    private String fullName;

    private int age;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    public enum TicketType {
        FULL, DISCOUNTED
    }

    public Visitor(@NonNull UUID identifier, @NonNull String fullName,
                   int age, @NonNull TicketType ticketType) {
        this.identifier = identifier;
        this.fullName = fullName;
        this.age = age;
        this.ticketType = ticketType;
    }

    public Visitor() {}

    // геттеры/сеттеры без изменений
    @NonNull public UUID getIdentifier() { return identifier; }
    public void setIdentifier(@NonNull UUID identifier) { this.identifier = identifier; }
    @NonNull public String getFullName() { return fullName; }
    public void setFullName(@NonNull String fullName) { this.fullName = fullName; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    @NonNull public TicketType getTicketType() { return ticketType; }
    public void setTicketType(@NonNull TicketType ticketType) { this.ticketType = ticketType; }

    @Override
    public String toString() {
        return "Visitor{identifier=" + identifier + ", fullName='" + fullName +
                "', age=" + age + ", ticketType=" + ticketType + "}";
    }
}