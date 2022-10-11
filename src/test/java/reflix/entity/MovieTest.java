package reflix.entity;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

//@Rollback(false)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // same db as app
class MovieTest {

    @Autowired
    EntityManager entityManager;

    // @Rollback(false)
    @Test
    void createMovieRequiredFields(){
        var movie = Movie.builder()
                .title("Prey")
                .year((short) 2022)
                .build();
        entityManager.persist(movie);
        entityManager.flush(); // synchro SQL: insert if not done
        assertNotNull(movie.getId());
    } // rollback or commit

    @Test
    void createMovieDuration() {
        var movie = Movie.builder()
                .title("Prey")
                .year((short) 2022)
                // .duration(Duration.ofMinutes(99))
                .duration(Duration.parse("PT1H39M"))
                .build();
        entityManager.persist(movie);
        entityManager.flush();
    }

    @Test
    void createMovieTitleNull() {
        var movie = Movie.builder()
                .year((short) 2022)
                .build();
        assertThrows(PersistenceException.class, () -> {
                entityManager.persist(movie);
                entityManager.flush();
        });

    }

    @ParameterizedTest
    @ValueSource(shorts = {1789, 1849})
    void createMovieYearCheckConstraintKo(short year) {
        var movie = Movie.builder()
                .title("Back to before cinema")
                .year(year)
                .build();
        var exception = assertThrows(PersistenceException.class, () -> {
            entityManager.persist(movie);
            entityManager.flush();
        });
        // extra assertions
        // Warning: not JPA
        var cause = exception.getCause();
        assertEquals(ConstraintViolationException.class, cause.getClass());
    }

    @Test
    void testCreateMovieUniqueTitleYearKo() {
        String title = "Prey";
        short year = 2022;
        var movies = IntStream.range(0,2)
                .mapToObj(i ->
                            Movie.builder()
                            .title(title)
                            .year(year)
                            .build())
                .toList(); // Java 17
                // .collect(Collectors.toList()); // Java 8
        assertThrows(PersistenceException.class, () -> {
                movies.forEach(entityManager::persist);
                entityManager.flush();
        });
    }

    @ParameterizedTest
    @ValueSource(strings ={
            "Z",
            "Avatar",
            "Night of the Day of the Dawn of the Son of the Bride of the Return of the Revenge of the Terror of the Attack of the Evil Mutant Hellbound Flesh Eating Crawling Alien Zombified Subhumanoid Living Dead, Part 5"
    })
    void createMovieTitleLengthOk(String title) {
        var movie = Movie.builder()
                .title(title)
                .year((short) 2022)
                .build();
        entityManager.persist(movie);
        entityManager.flush();
    }

    @ParameterizedTest
    @NullSource // already done in another test
    @EmptySource
    @MethodSource("reflix.datatest.Sources#randomTitleTooLong") // 251 chars !!!
    void createMovieTitleLengthKo(String title) {
        var movie = Movie.builder()
                .title(title)
                .year((short) 2022)
                .build();
        var exception = assertThrows(PersistenceException.class, () -> {
            entityManager.persist(movie);
            entityManager.flush();
        });
    }














}