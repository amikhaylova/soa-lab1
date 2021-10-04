package xml;

import dto.dtoList.CoordinatesDTOList;
import dto.dtoList.LocationDTOList;
import dto.dtoList.MovieDTOList;
import dto.dtoList.PersonDTOList;
import exceptions.XmlParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XmlToJavaConverter {

    public CoordinatesDTOList getCoordinatesDTOFromXml(String xml) {
        CoordinatesDTOList coordinatesDTOList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CoordinatesDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            coordinatesDTOList = (CoordinatesDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return coordinatesDTOList;
    }

    public MovieDTOList getMovieDTOFromXml(String xml) {
        MovieDTOList movieDTOList;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MovieDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            movieDTOList = (MovieDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            throw new XmlParseException(e.getMessage());
        }
        return movieDTOList;
    }

    public LocationDTOList getLocationDTOFromXml(String xml) {
        LocationDTOList movieDTO = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LocationDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            movieDTO = (LocationDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return movieDTO;
    }

    public PersonDTOList getPersonDTOFromXml(String xml) {
        PersonDTOList movieDTO = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            movieDTO = (PersonDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return movieDTO;
    }
}
