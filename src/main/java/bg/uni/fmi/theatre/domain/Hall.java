package bg.uni.fmi.theatre.domain;

import bg.uni.fmi.theatre.validation.Validator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "hall")
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int capacity;

    protected Hall() {}

    public Hall(String name, int capacity) {
        Validator.validateString(name, "name cannot be null or empty");
        Validator.validatePositiveNumber(capacity, "capacity should be a positive number");
        this.name = name;
        this.capacity = capacity;
    }
}