package devops.tp1;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Goat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String breed;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private double weight;
    private String enclosure;

    // Constructors
    public Goat() {}

    public Goat(String name, String breed, LocalDate birthDate, double weight, String enclosure) {
        this.name = name;
        this.breed = breed;
        this.birthDate = birthDate;
        this.weight = weight;
        this.enclosure = enclosure;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getEnclosure() { return enclosure; }
    public void setEnclosure(String enclosure) { this.enclosure = enclosure; }
}