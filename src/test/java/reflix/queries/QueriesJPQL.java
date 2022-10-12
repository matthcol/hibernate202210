package reflix.queries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import reflix.dto.TitleYearActorDirector;
import reflix.entity.Movie;
import reflix.entity.People;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("query")
class QueriesJPQL {

    @Autowired
    EntityManager entityManager;

    @Test
    void testFindById() {
        var movie = entityManager.find(Movie.class, 111503);
        System.out.println(movie);
    }


    @ParameterizedTest
    @ValueSource(strings = {"True Lies", "The Man Who Knew Too Much"})
    void testFindMovieByTitle(String title) {
        String sql = "from Movie m where m.title = ?1";
        var movies = entityManager.createQuery(sql, Movie.class)  // TypedQuery<Movie>
                .setParameter(1, title)
                .getResultList();  // execute query + store results in list
        System.out.println(movies);
    }

    @Test
    void testFindMoviesByDirectorName(){
        String name = "Clint Eastwood";
        String sql = "select m from Movie m join fetch m.director d where d.name = :name";
        entityManager.createQuery(sql, Movie.class)
                .setParameter("name", name)
                .getResultStream()
                .forEach(m -> System.out.println("\t- " + m));
    }

    @Test
    void testFindMoviesByActorName(){
        String name = "Clint Eastwood";
        // sol 1. query ok if director in lazy mode
        // String sql = "select m from Movie m join m.actors a where a.name = :name";
        // sol 2. query with  director in eager mode
        String sql = """
            select m 
            from Movie m 
                join m.actors a 
                left join fetch m.director 
            where a.name = :name
            order by m.year desc""";
        entityManager.createQuery(sql, Movie.class)
                .setParameter("name", name)
                .getResultStream()
                .forEach(m -> System.out.println("\t- " + m));
    }


    @Test
    void testFindTitleYearActorNameDirectorNameByActorNameToObjectArray(){
        String name = "Clint Eastwood";
        String sql = """
            select 
                m.title, m.year,
                a.name as actor_name,
                d.name as director_name
            from Movie m 
                join m.actors a 
                left join m.director d 
            where a.name = :name
            order by m.year desc""";
        entityManager.createQuery(sql, Object[].class)
                .setParameter("name", name)
                .getResultStream()
                .forEach(m -> System.out.println("\t- " + Arrays.toString(m)));
    }

    @Test
    void testFindTitleYearActorNameDirectorNameByActorNameToTuple(){
        String name = "Clint Eastwood";
        String sql = """
            select 
                m.title,
                m.year as year,
                a.name as actor_name,
                d.name as director_name
            from Movie m 
                join m.actors a 
                left join m.director d 
            where a.name = :name
            order by m.year desc""";
        entityManager.createQuery(sql, Tuple.class)
                .setParameter("name", name)
                .getResultStream()
                .forEach(m -> System.out.println("\t- "
                        + m.get(0, String.class)
                        + ", " + m.get("year", Short.class)
                        + ", a: " + m.get("actor_name", String.class)
                        + ", d: " + m.get("director_name", String.class)
                ));
    }

    @Test
    void testFindTitleYearActorNameDirectorNameByActorNameToDto(){
        String name = "Clint Eastwood";
        String sql = """
            select 
                new reflix.dto.TitleYearActorDirector(
                    m.title,
                    m.year,
                    a.name,
                    d.name
                )
            from Movie m 
                join m.actors a 
                left join m.director d 
            where a.name = :name
            order by m.year desc""";
        entityManager.createQuery(sql, TitleYearActorDirector.class)
                .setParameter("name", name)
                .getResultStream()
                .forEach(m -> System.out.println("\t- "
                        + m.getTitle()
                        + ", " + m.getYear()
                        + ", a: " + m.getActorName()
                        + ", d: " + m.getDirectorName()
                ));
    }

    @Test
    void testStatsByDirector() {
        // nb de films, année min, année max, durée totale
        // cut: nb films >= seuil
        long movieCountThreshold = 15;
        String sql = """
                select 
                    d.id as director_id, 
                    d.name as name,
                    COUNT(m.id) as movie_count,
                    MIN(m.year) as min_year,
                    MAX(m.year) as max_year,
                    SUM(m.duration) as total_duration
                from 
                    Movie m
                    join m.director d
                group by d.id, d.name
                having count(m.id) >= :threshold
                order by movie_count desc
                """;
        entityManager.createQuery(sql, Tuple.class)
                .setParameter("threshold", movieCountThreshold)
                .getResultStream()
                .forEach(s -> System.out.println("\t- " +
                        s.get("director_id", Integer.class)
                        + ", " + s.get("name", String.class)
                        + "\n\t\t* count: " + s.get("movie_count", Long.class)
                        + "\n\t\t* first year: " + s.get("min_year", Short.class)
                        + "\n\t\t* last year: " + s.get("max_year", Short.class)
                        + "\n\t\t* total duration: " + s.get("total_duration", Long.class)
                ));

    }

    @Test
    void testFindPeopleBornYear() {
        String sql = """
                select  p from People p where YEAR(p.birthdate) = :birthyear
                """;
        entityManager.createQuery(sql, People.class)
                .setParameter("birthyear", 1930)
                .getResultStream()
                .forEach(System.out::println);
    }



}