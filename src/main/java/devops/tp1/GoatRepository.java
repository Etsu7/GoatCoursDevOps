package devops.tp1;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoatRepository extends JpaRepository<Goat, Long> {
    List<Goat> findByEnclosure(String enclosure);
}