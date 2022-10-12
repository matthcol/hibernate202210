package reflix.entity;

import lombok.*;

import javax.persistence.*;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity
@Table(name="persons")
public class People {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    private String name;

    private LocalDate birthdate;

    // old Java temporal types:
    // @Temporal(TemporalType.DATE) // default => datetime/timestamp
    // private Date birthdate;
}
