package digital.zil.hl.module1.repository;

import digital.zil.hl.module1.model.Exhibit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExhibitRepository extends JpaRepository<Exhibit, UUID> {
    Optional<Exhibit> findByName(String name);
    boolean existsByName(String name);
    boolean existsByNameAndIdentifierNot(String name, UUID identifier);
}