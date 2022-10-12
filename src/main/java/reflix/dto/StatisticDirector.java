package reflix.dto;

public interface StatisticDirector {
    Integer getDirectorId();
    String getName();
    Long getMovieCount();
    Short getMinYear();
    Short getMaxYear();
    Long getTotalDuration();
}
