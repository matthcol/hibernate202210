package reflix.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // same db as app
class MovieTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void createMovie(){
        var movie = new Movie();
        movie.setTitle("Prey");
        movie.setYear((short) 2022);
        entityManager.persist(movie);

    }

}