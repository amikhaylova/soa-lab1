package dto;

import entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PagedMovieList {
    private List<Movie> movieList;
    private Long count;
}
