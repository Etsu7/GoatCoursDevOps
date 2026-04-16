package devops.tp1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goats")
public class GoatController {

    @Autowired
    private GoatService goatService;

    @PostMapping("/birth")
    public ResponseEntity<Goat> registerBirth(@RequestBody Goat goat) {
        Goat saved = goatService.saveGoat(goat);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/to-vaccinate")
    public ResponseEntity<List<Goat>> getGoatsToVaccinate() {
        List<Goat> goats = goatService.getGoatsToVaccinateThisMonth();
        return ResponseEntity.ok(goats);
    }

    @PutMapping("/{id}/transfer")
    public ResponseEntity<String> transferGoat(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String newEnclosure = body.get("enclosure");
        if (newEnclosure == null) {
            return ResponseEntity.badRequest().body("Enclosure required");
        }
        goatService.transferGoat(id, newEnclosure);
        return ResponseEntity.ok("Transferred");
    }

    @GetMapping
    public ResponseEntity<List<Goat>> getAllGoats() {
        List<Goat> goats = goatService.getAllGoats();
        return ResponseEntity.ok(goats);
    }

    @GetMapping("/ration/{weight}")
    public ResponseEntity<String> getRation(@PathVariable double weight) {
        String ration = goatService.calculateRation(weight);
        return ResponseEntity.ok(ration);
    }

    @GetMapping("/reproductive/{id}")
    public ResponseEntity<Boolean> isReproductive(@PathVariable Long id) {
        var goat = goatService.findGoatById(id);
        if (goat.isPresent()) {
            boolean isRepro = goatService.isReproductiveAge(goat.get().getBirthDate());
            return ResponseEntity.ok(isRepro);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/milk-production")
    public ResponseEntity<Double> getAverageMilkProduction() {
        double production = goatService.averageMilkProduction();
        return ResponseEntity.ok(production);
    }
}