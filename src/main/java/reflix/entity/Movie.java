package reflix.entity;


import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Type;
import reflix.enumeration.Pg;

import javax.persistence.*;
import java.time.Duration;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Check(constraints = "year >= 1850")
// @Check(constraints = "LENGTH(title) > 0") // TODO: AND or ???
// NB: better here for all RDBMS
@Table(
        uniqueConstraints = @UniqueConstraint(
                name = "uniq_movie_title_year",
                columnNames = {"title", "year"}
        )
)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(nullable = false, length = 250)
    private String title;

    // @Transient
    // NB: usefull to debug, to introduce new feature
    // @Column(name = "m_year")
    // @Column(name = "\"year\"")
    @Column(nullable = false) // with primitive short, implicit for the object
    private short year;

    @Column(nullable = true) // by default
    // private Short duration;
    private Duration duration;

    // @Type(type = "nstring") // Hibernate not JPA
    // Note: code first only, be cautious with portability
    private String synopsis;

    @Enumerated(EnumType.STRING)  // default ORDINAL
    private Pg pg;

}
