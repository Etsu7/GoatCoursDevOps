package devops.tp1;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class HealthRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "goat_id")
    private Goat goat;

    private String vaccines; // e.g., "Rabies,Clostridium"
    private String medicalHistory;
    private LocalDate lastVaccineDate;

    // Constructors
    public HealthRecord() {}

    public HealthRecord(Goat goat, String vaccines, String medicalHistory, LocalDate lastVaccineDate) {
        this.goat = goat;
        this.vaccines = vaccines;
        this.medicalHistory = medicalHistory;
        this.lastVaccineDate = lastVaccineDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Goat getGoat() { return goat; }
    public void setGoat(Goat goat) { this.goat = goat; }

    public String getVaccines() { return vaccines; }
    public void setVaccines(String vaccines) { this.vaccines = vaccines; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public LocalDate getLastVaccineDate() { return lastVaccineDate; }
    public void setLastVaccineDate(LocalDate lastVaccineDate) { this.lastVaccineDate = lastVaccineDate; }
}