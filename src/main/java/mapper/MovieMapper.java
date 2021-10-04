package mapper;

import dto.MovieDTO;
import entity.Movie;
import entity.Person;
import exceptions.EntityIsNotValidException;
import repository.CoordinatesRepository;
import repository.PersonRepository;
import utils.FieldValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class MovieMapper {
    private CoordinatesMapper coordinatesMapper;
    private PersonMapper personMapper;
    private CoordinatesRepository coordinatesRepository;
    private PersonRepository personRepository;

    public MovieMapper() {
        coordinatesMapper = new CoordinatesMapper();
        personMapper = new PersonMapper();
        coordinatesRepository = new CoordinatesRepository();
        personRepository = new PersonRepository();
    }

    public Movie mapMovieDTOToMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setId(FieldValidationUtil.getIntegerFieldValue(movieDTO.getId()));
        movie.setCoordinates(coordinatesMapper.mapCoordinatesDTOToCoordinates(movieDTO.getCoordinates()));
        if (movie.getCoordinates().getId() == null) throw new EntityIsNotValidException("coordinates must not be null");
        if (movie.getCoordinates() != null && !coordinatesRepository.existsById(movie.getCoordinates().getId()))
            throw new EntityIsNotValidException("coordinates with id = " + movie.getCoordinates().getId() + " does not exist");
        movie.setDuration(FieldValidationUtil.getLongFieldValue(movieDTO.getDuration()));
        movie.setGenre(FieldValidationUtil.getMovieGenreValue(movieDTO.getGenre()));
        movie.setMpaaRating(FieldValidationUtil.getMpaaRatingValue(movieDTO.getMpaaRating()));
        movie.setName(FieldValidationUtil.getStringValue(movieDTO.getName()));
        movie.setOscarsCount(FieldValidationUtil.getLongFieldValue(movieDTO.getOscarsCount()));
        Person screenwriter;
        if (!movieDTO.getScreenWriter().getId().equals("")){
            screenwriter = personRepository.findById(Integer.parseInt(movieDTO.getScreenWriter().getId()));
        }else {
            throw new EntityIsNotValidException("screenwriter must not be null");
        }
        movie.setScreenWriter(screenwriter);
        return movie;
    }

    public MovieDTO mapMovieToMovieDTO(Movie movie) {
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setId(String.valueOf(movie.getId()));
        movieDTO.setName(movie.getName());
        movieDTO.setCoordinates(coordinatesMapper.mapCoordinatesToCoordinatesDTO(movie.getCoordinates()));
        movieDTO.setDuration(String.valueOf(movie.getDuration()));
        movieDTO.setCreationDate(String.valueOf(movie.getCreationDate()));
        movieDTO.setScreenWriter(personMapper.mapPersonToPersonDTO(movie.getScreenWriter()));
        movieDTO.setMpaaRating(String.valueOf(movie.getMpaaRating()));
        movieDTO.setGenre(String.valueOf(movie.getGenre()));
        movieDTO.setOscarsCount(String.valueOf(movie.getOscarsCount()));
        return movieDTO;
    }

    public List<MovieDTO> mapMovieListToMovieDTOList(List<Movie> movieList) {
        ArrayList<MovieDTO> movieDTOArrayList = new ArrayList<>();
        for (Movie movie : movieList) {
            movieDTOArrayList.add(mapMovieToMovieDTO(movie));
        }
        return movieDTOArrayList;
    }


}
