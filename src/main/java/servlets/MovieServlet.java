package servlets;

import dto.MovieDTO;
import dto.PagedMovieList;
import dto.dtoList.MovieDTOList;
import entity.Movie;
import mapper.MovieMapper;
import repository.MovieRepository;
import utils.UrlParametersUtil;
import validation.EntityValidator;
import xml.JavaToXmlConverter;
import xml.XmlToJavaConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/movies/*", loadOnStartup = 1)
public class MovieServlet extends HttpServlet {
    private MovieRepository repository;
    private JavaToXmlConverter javaToXml;
    private XmlToJavaConverter xmlToJava;
    private EntityValidator entityValidator;
    private MovieMapper movieMapper;

    @Override
    public void init() throws ServletException {
        repository = new MovieRepository();
        javaToXml = new JavaToXmlConverter();
        xmlToJava = new XmlToJavaConverter();
        entityValidator = new EntityValidator();
        movieMapper = new MovieMapper();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        MovieDTOList movieDTOList = xmlToJava.getMovieDTOFromXml(requestBody);
        Movie movieToUpdate = movieMapper.mapMovieDTOToMovie(movieDTOList.getMovieList().get(0));
        Movie existingMovie = repository.findById(movieToUpdate.getId());
        movieToUpdate.setCreationDate(existingMovie.getCreationDate());
        entityValidator.validateMovie(movieToUpdate);
        repository.update(movieToUpdate);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Movie movieToPersist = movieMapper.mapMovieDTOToMovie(xmlToJava.getMovieDTOFromXml(requestBody).getMovieList().get(0));
        movieToPersist.setCreationDate(LocalDate.now());
        entityValidator.validateMovie(movieToPersist);
        repository.create(movieToPersist);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String perPage = UrlParametersUtil.getField(request, "perPage");
        String curPage = UrlParametersUtil.getField(request, "curPage");
        String sortBy = UrlParametersUtil.getField(request, "sortBy");
        String filterBy = UrlParametersUtil.getField(request, "filterBy");

        String pathInfo = request.getPathInfo();

        String movieId = null;
        if (pathInfo != null)
            movieId = pathInfo.substring(1);

        if (movieId != null) {
            Movie movie = repository.findById(Integer.parseInt(movieId));
            MovieDTOList dto = new MovieDTOList(new ArrayList<>(), 1);
            List<MovieDTO> dtoList = dto.getMovieList();
            dtoList.add(movieMapper.mapMovieToMovieDTO(movie));
            response.getWriter().write(javaToXml.moviesToXML(dto));
            return;
        }

        PagedMovieList pagedMovieList = repository.findAll(perPage, curPage, sortBy, filterBy);
        MovieDTOList dto = new MovieDTOList((movieMapper.mapMovieListToMovieDTOList(pagedMovieList.getMovieList())), pagedMovieList.getCount());
        response.getWriter().write(javaToXml.moviesToXML(dto));

        repository.clearEntityManager();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String movieId = null;
        if (pathInfo != null)
            movieId = pathInfo.substring(1);
        Movie movie = repository.findById(Integer.parseInt(movieId));
        repository.delete(movie);
    }

}