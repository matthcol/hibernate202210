package reflix.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    // @Transient
    // NB: usefull to debug, to introduce new feature
    // @Column(name = "m_year")
    // @Column(name = "\"year\"")
    private short year;
}
