package reflix.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MovieDirectorTest {
    @Autowired
    EntityManager entityManager;

    @Test
    void setDirector() {
        // hypothesis: movie and director already in database
        var movie = Movie.builder()
                .title("Gran Torino")
                .year((short) 2008)
                .build();
        var director = People.builder()
                .name("Clint Eastwood")
                .build();
        entityManager.persist(movie);
        entityManager.persist(director);
        entityManager.flush();  // SQL: 2x INSERT
        entityManager.clear();  // empty hibernate cache
        // read hypothesis from db
        var movieRead = entityManager.find(Movie.class, movie.getId());
        var directorRead = entityManager.find(People.class, director.getId());

        // when
        movieRead.setDirector(directorRead);
        entityManager.flush(); // SQL: UPDATE

        //
        entityManager.clear();
        var movieReadAgain = entityManager.find(Movie.class, movie.getId());
        var directorOfMovie = movieReadAgain.getDirector();
        System.out.println(directorOfMovie);
    }



}