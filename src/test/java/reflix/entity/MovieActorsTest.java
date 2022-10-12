package reflix.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MovieActorsTest {

    @Autowired
    EntityManager entityManager;

    @Test
    void addActors() {
        var movie = Movie.builder()
                .title("Gran Torino")
                .year((short) 2008)
                .build();
        var clint = People.builder()
                .name("Clint Eastwood")
                .build();

        var bee = People.builder()
                .name("Bee Vang")
                .build();
        var ahney = People.builder()
                .name("Ahney Her")
                .build();
        Stream.of(movie, clint, bee, ahney)
                .forEach(entityManager::persist);
        entityManager.flush();
        // movie.getActors().addAll(List.of(clint, bee));
        Collections.addAll(movie.getActors(), clint, bee);
        entityManager.flush();
        // clear and read again data
        entityManager.clear();
        var movieRead = entityManager.find(Movie.class, movie.getId());
        var ahneyRead = entityManager.find(People.class, ahney.getId());
        movieRead.getActors().add(ahneyRead);
        entityManager.flush();
    }


}