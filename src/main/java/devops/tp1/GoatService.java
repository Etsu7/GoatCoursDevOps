package devops.tp1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.Locale;

@Service
public class GoatService {

    @Autowired
    private GoatRepository goatRepository;

    @Autowired
    private HealthRecordRepository healthRecordRepository;

    public String calculateRation(double weight) {
        double ration = weight * 0.025;
        return String.format(Locale.US, "%.2f kg of hay per day", ration);
    }

    public boolean isReproductiveAge(LocalDate birthDate) {
        Period age = Period.between(birthDate, LocalDate.now());
        return age.getMonths() >= 6 || age.getYears() > 0;
    }

    public double averageMilkProduction() {
        // Assume average 2 liters per day per goat
        List<Goat> goats = goatRepository.findAll();
        if (goats.isEmpty()) return 0;
        return 2.0 * goats.size(); // total production
    }

    public List<Goat> getGoatsToVaccinateThisMonth() {
        LocalDate oneMonthAgo = LocalDate.now().minusMonths(1);
        List<HealthRecord> records = healthRecordRepository.findByLastVaccineDateBefore(oneMonthAgo);
        return records.stream().map(HealthRecord::getGoat).distinct().toList();
    }

    public Goat saveGoat(Goat goat) {
        return goatRepository.save(goat);
    }

    public Optional<Goat> findGoatById(Long id) {
        return goatRepository.findById(id);
    }

    public void transferGoat(Long id, String newEnclosure) {
        Optional<Goat> goat = goatRepository.findById(id);
        if (goat.isPresent()) {
            goat.get().setEnclosure(newEnclosure);
            goatRepository.save(goat.get());
        }
    }

    public List<Goat> getAllGoats() {
        return goatRepository.findAll();
    }
}