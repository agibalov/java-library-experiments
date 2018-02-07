package me.loki2302;

import me.loki2302.jaxb.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class App {
    public static void main(String[] args) throws JAXBException {
        dumpMessage();
        dumpGoodPerson();
        dumpRequestContainer();
    }

    private static void dumpMessage() throws JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();

        Message message = objectFactory.createMessage();
        message.setId(123);
        message.setContent("hello");
        message.setStatus(Status.PROCESSED);

        JAXBContext context = JAXBContext.newInstance(Message.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(objectFactory.createMessage(message), stringWriter);
        System.out.println(stringWriter.toString());
    }

    private static void dumpGoodPerson() throws JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();

        GoodPerson goodPerson = objectFactory.createGoodPerson();
        goodPerson.setName("qwerty");
        goodPerson.setGoodnessLevel(9000);

        JAXBContext context = JAXBContext.newInstance(GoodPerson.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(objectFactory.createGoodPerson(goodPerson), stringWriter);
        System.out.println(stringWriter.toString());
    }

    private static void dumpRequestContainer() throws JAXBException {
        ObjectFactory objectFactory = new ObjectFactory();

        AddNumbersCalculatorRequest request = new AddNumbersCalculatorRequest();
        request.setA(2);
        request.setB(3);

        RequestContainer requestContainer = new RequestContainer();
        requestContainer.setRequest(request);

        JAXBContext context = JAXBContext.newInstance(RequestContainer.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(objectFactory.createRequestContainer(requestContainer), stringWriter);
        System.out.println(stringWriter.toString());
    }
}
