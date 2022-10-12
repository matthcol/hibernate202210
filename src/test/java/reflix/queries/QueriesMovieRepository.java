package reflix.queries;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import reflix.repository.MovieRepository;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("query")
class QueriesMovieRepository {

    @Autowired
    MovieRepository movieRepository;

    @ParameterizedTest
    @ValueSource(ints = {113492, 1})
    void findById(int id) {
        var res = movieRepository.findById(id);
        System.out.println(res);
    }

    @Test
    void findAll() {
        var res1 = movieRepository.findAll();
        System.out.println(res1);
    }

    @Test
    void findAllSort() {
        var res1 = movieRepository.findAll(
                Sort.by("year")
        );
        System.out.println(res1);
    }

    @Test
    void findAllPageable() {
        var page = movieRepository.findAll(
                Pageable.ofSize(50)
        );
        for (var m: page) {
            System.out.println("\t-" + m);
        }
        // Suite:
        // page.nextPageable();
        // page.hasNext()
    }

    @Test
    void testFindByDirectorNameEndingWithOrderByYearDesc() {
        String name = "Eastwood";
        movieRepository.findByDirectorNameEndingWithOrderByYearDesc(name)
                .forEach(m -> System.out.println("\t-" + m));
    }

    @Test
    void testStatisticsByDirector() {
        long threshold = 15;
        movieRepository.statisticsByDirector(threshold)
                .forEach(s -> System.out.println("\t- " +
                        s.getDirectorId()
                        + ", " + s.getName()
                        + "\n\t\t* count: " + s.getMovieCount()
                        + "\n\t\t* first year: " + s.getMinYear()
                        + "\n\t\t* last year: " + s.getMaxYear()
                        + "\n\t\t* total duration: " + s.getTotalDuration()
                ));
    }

    @Test
    void testFindByActor() {
        movieRepository.findByActor("Steve McQueen")
                .forEach(m -> System.out.println("\t - " + m));

    }

    @Test
    void testFindTitlesWithNoDurationOrNoSynopsis() {
        movieRepository.findTitlesWithNoDurationOrNoSynopsis()
                .forEach(t -> System.out.println("\t-"
                        + t.get("title", String.class)
                        + ", " + t.get("DuRation", BigDecimal.class)
                        + ", " + t.get("synopsis", String.class)
                ));
    }

    @Test
    void testMovieTeam() {
        int id = 113492;
        movieRepository.findById(id)
                .ifPresent(m -> {
                    System.out.println(m);
                    System.out.println("\t- director: " +m.getDirector());
                    System.out.println("\t- cast: ");
                    m.getActors().forEach(
                            a -> System.out.println("\t\t* " + a)
                    );
                });

    }

}



