package servlets;

import dto.CountDTO;
import dto.dtoList.PersonDTOList;
import entity.Movie;
import entity.Person;
import enums.MovieGenre;
import mapper.PersonMapper;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;
import xml.JavaToXmlConverter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/additional")
public class AdditionalTasksServlet extends HttpServlet {
    private Session session;
    private EntityManager em;
    private JavaToXmlConverter javaToXml;
    private PersonMapper personMapper;

    @Override
    public void init() throws ServletException {
        session = HibernateUtil.getSessionFactory().openSession();
        em = session.getEntityManagerFactory().createEntityManager();
        javaToXml = new JavaToXmlConverter();
        personMapper = new PersonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String genre = request.getParameter("genre");
        String length = request.getParameter("length");
        String screenwriter = request.getParameter("screenwriter");

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        CriteriaQuery<Movie> criteriaQuery = criteriaBuilder.createQuery(Movie.class);
        Root<Movie> from = criteriaQuery.from(Movie.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Movie.class)));
        em.createQuery(countQuery);

        if (genre != null) {
            countQuery.where(criteriaBuilder.equal(from.get("genre"), MovieGenre.valueOf(genre)));
            Long countResult = em.createQuery(countQuery).getSingleResult();
            CountDTO countDTO = new CountDTO();
            countDTO.setCount(countResult);
            response.getWriter().write(javaToXml.countToXML(countDTO));
            return;
        }
        if (length != null) {
            countQuery.where(criteriaBuilder.greaterThan(from.get("duration"), Integer.parseInt(length)));
            Long countResult = em.createQuery(countQuery).getSingleResult();
            CountDTO countDTO = new CountDTO();
            countDTO.setCount(countResult);
            response.getWriter().write(javaToXml.countToXML(countDTO));
            return;
        }
        if (screenwriter != null) {
            javax.persistence.Query singleQuery = em.createQuery("SELECT c FROM Person c WHERE c.id = ?1", Person.class);
            Person person = (Person) singleQuery.setParameter(1, Integer.parseInt(screenwriter)).getSingleResult();
            CriteriaQuery<Person> criteriaQueryPerson = criteriaBuilder.createQuery(Person.class);
            Root<Person> root = criteriaQueryPerson.from(Person.class);
            criteriaQueryPerson.select(root).where(criteriaBuilder.lessThan(root.get("name"), person.getName()));
            Query<Person> query = session.createQuery(criteriaQueryPerson);
            List<Person> personList = query.getResultList();
            PersonDTOList dto = new PersonDTOList(new ArrayList<>());
            dto.setPersonsList(personMapper.mapPersonListToPersonDTOList(personList));
            response.getWriter().write(javaToXml.personsToXML(dto));
        }
    }
}
