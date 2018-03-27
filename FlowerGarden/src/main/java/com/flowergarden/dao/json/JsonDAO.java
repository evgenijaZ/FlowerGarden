package com.flowergarden.dao.json;

import com.flowergarden.bouquet.MarriedBouquet;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.codehaus.jettison.mapped.MappedXMLStreamWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author Yevheniia Zubrych on 27.03.2018.
 */
public class JsonDAO {

    private String filename;
    private JAXBContext jaxbContext;


    public JsonDAO(String filename, Class entityClass) {
        this.filename = filename;
        try {
            jaxbContext = JAXBContext.newInstance(entityClass);
        } catch (JAXBException e) {
            System.err.println("Cannot create context. " + e.getMessage());
        }
    }

    public MarriedBouquet getBouquet() throws JSONException, XMLStreamException {
        if (jaxbContext == null) {
            System.err.println("Context is null.");
            return null;
        }
        String content = null;
        try {

            content = new String(Files.readAllBytes(Paths.get(filename)));

            JSONObject json = new JSONObject(content);
            Configuration config = new Configuration();
            MappedNamespaceConvention con = new MappedNamespaceConvention(config);
            XMLStreamReader xmlStreamReader = new MappedXMLStreamReader(json, con);

            Unmarshaller unmarshaller = null;

            unmarshaller = jaxbContext.createUnmarshaller();
            return (MarriedBouquet) unmarshaller.unmarshal(xmlStreamReader);

        } catch (IOException e) {
            System.err.println("Invalid path to file:" + filename + ". " + e.getMessage());

        } catch (JAXBException e) {
            System.err.println("Cannot create unmarshaller. " + e.getMessage());
        }
        return null;
    }


    public void create(MarriedBouquet bouquet) {
        if (jaxbContext == null) {
            System.err.println("Context is null.");
            return;
        }
        Configuration config = new Configuration();
        MappedNamespaceConvention con = new MappedNamespaceConvention(config);
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(filename);
            Writer writer = new OutputStreamWriter(outputStream);
            XMLStreamWriter xmlStreamWriter = new MappedXMLStreamWriter(con, writer);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.marshal(bouquet, xmlStreamWriter);
        } catch (FileNotFoundException e) {
            System.err.println("JSON file not found." + e.getMessage());
        } catch (JAXBException e) {
            System.err.println("Cannot create marshaller. " + e.getMessage());
        }

    }
}
