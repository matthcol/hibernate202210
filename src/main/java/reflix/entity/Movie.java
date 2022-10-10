package reflix.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter @Setter
@Entity
public class Movie {

    @Id
    private Integer id;

    private String title;

    private short year;
}
