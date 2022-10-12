package reflix.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import reflix.dto.StatisticDirector;
import reflix.entity.Movie;

import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQuery;
import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


public interface MovieRepository extends JpaRepository<Movie, Integer> {

    // keywords spring data jpa repository
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation

    public List<Movie> findByTitle(String title);

    public Set<Movie> findByDirectorNameEndingWithOrderByYearDesc(
            String endName);

    @Query("""
            select 
                    d.id as directorId, 
                    d.name as name,
                    COUNT(m.id) as movieCount,
                    MIN(m.year) as minYear,
                    MAX(m.year) as maxYear,
                    SUM(m.duration) as totalDuration
                from 
                    Movie m
                    join m.director d
                group by d.id, d.name
                having count(m.id) >= :countMovieThreshold
                order by movieCount desc""")
    public List<StatisticDirector> statisticsByDirector(long countMovieThreshold);


    // ref NamedQuery with name Movie.findByActor
    public Stream<Movie> findByActor(String name);

    @Query(nativeQuery = true, value = """
            select 
                m.title as title,
                nvl(duration, 0) as duration,
                nvl(synopsis, 'NO SYNOPSIS') as synopsis
            from movies m
            where 
                duration is null
                or synopsis is null
            """)
    public List<Tuple> findTitlesWithNoDurationOrNoSynopsis();


    @EntityGraph("Movie.team")
    public Optional<Movie> findById(Integer id);

}
