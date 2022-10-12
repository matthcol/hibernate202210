package reflix.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import reflix.entity.Movie;
import reflix.entity.People;
import reflix.repository.MovieRepository;

import java.util.Collection;

// @Transactional // all methods are handled in transactional method
public class MovieServiceImpl {

    @Autowired
    MovieRepository movieRepository;

    @Transactional
    public void addMovie(Movie m) {}

    @Transactional(timeout = 300) // seconds
    public void addMovieWithTeam(
            Movie m,
            People d,
            Collection<? extends People> actors)
    {
        // insert Movie, insert Play, update ...
    }

    // no transactional
    public void readMovieByTitle() {}
}
