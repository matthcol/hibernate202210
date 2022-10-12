package reflix.entity;


import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import reflix.enumeration.Pg;

import javax.persistence.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@ToString(exclude = {"director", "actors", "genres"})
@Entity
@Check(constraints = "year >= 1850")
// @Check(constraints = "LENGTH(title) > 0") // TODO: AND or ???
// NB: better here for all RDBMS
@Table(
        name = "movies",
        uniqueConstraints = @UniqueConstraint(
                name = "uniq_movie_title_year",
                columnNames = {"title", "year"}
        )
)
@DynamicUpdate // HBN not JPA => update fin field by field
@NamedQuery(name="Movie.findByActor", query= """
        select m 
        from Movie m 
            join m.actors a 
            left join fetch m.director 
        where a.name = :name
        order by m.year desc""")
@NamedEntityGraph(name = "Movie.team",
        attributeNodes = {
                @NamedAttributeNode("director"),
                @NamedAttributeNode("actors")
        }
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
    private Short duration;
    //private Duration duration;

    // @Type(type = "nstring") // Hibernate not JPA
    // Note: code first only, be cautious with portability
    private String synopsis;

    @Enumerated(EnumType.STRING)  // default ORDINAL
    private Pg pg;

    // @Transient
    @ManyToOne //(fetch = FetchType.LAZY)  // by default Eager
    @JoinColumn(name = "director_id", nullable = true)
    private People director;

    @ManyToMany // (fetch = FetchType.EAGER) // (fetch = FetchType.LAZY)  by default
    @JoinTable(name = "play",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    @Builder.Default
    private Set<People> actors = new HashSet<>();
    // private List<People> actors = new ArrayList<>();
    // NB: List specifities: no primary key, update => delete all + inserts

    @ElementCollection
    @CollectionTable(
            name = "have_genre",
            joinColumns = @JoinColumn(name="movie_id")
    )
    @Column(name="genre", nullable = false)
    @Builder.Default
    private Set<String> genres = new HashSet<>();
}
