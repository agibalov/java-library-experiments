package me.loki2302;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;

public class LayoutFromXmlAppStax {
	public static void main(String[] args) {
		XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		InputStream inputStream = LayoutFromXmlApp.class.getResourceAsStream("/calculator_view.xml");
		try {
			XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
			while(xmlEventReader.hasNext()) {
				XMLEvent xmlEvent = xmlEventReader.nextEvent();
								
				int eventType = xmlEvent.getEventType();
				if(eventType == XMLStreamConstants.START_ELEMENT) {
					StartElement startElement = xmlEvent.asStartElement();
					String elementName = startElement.getName().getLocalPart();
					System.out.printf("XMLStreamConstants.START_ELEMENT %s\n", elementName);
					
					Iterator<Attribute> attributeIterator = (Iterator<Attribute>)startElement.getAttributes();
					while(attributeIterator.hasNext()) {
						Attribute attribute = attributeIterator.next();
						String attributeName = attribute.getName().getLocalPart();
						String attributeValue = attribute.getValue();
						System.out.printf("ATTR '%s' = '%s'\n", attributeName, attributeValue);
					}					
				} else if(eventType == XMLStreamConstants.END_ELEMENT) {
					EndElement endElement = xmlEvent.asEndElement();
					String elementName = endElement.getName().getLocalPart();
					System.out.printf("XMLStreamConstants.END_ELEMENT %s\n", elementName);
				} else if(eventType == XMLStreamConstants.PROCESSING_INSTRUCTION) {
					System.out.println("XMLStreamConstants.PROCESSING_INSTRUCTION");
				} else if(eventType == XMLStreamConstants.CHARACTERS) {
					System.out.println("XMLStreamConstants.CHARACTERS");
				} else if(eventType == XMLStreamConstants.COMMENT) {
					System.out.println("XMLStreamConstants.COMMENT");
				} else if(eventType == XMLStreamConstants.SPACE) {
					System.out.println("XMLStreamConstants.SPACE");
				} else if(eventType == XMLStreamConstants.START_DOCUMENT) {
					System.out.println("XMLStreamConstants.START_DOCUMENT");
				} else if(eventType == XMLStreamConstants.END_DOCUMENT) {
					System.out.println("XMLStreamConstants.END_DOCUMENT");
				} else if(eventType == XMLStreamConstants.ENTITY_REFERENCE) {
					System.out.println("XMLStreamConstants.ENTITY_REFERENCE");
				} else if(eventType == XMLStreamConstants.ATTRIBUTE) {
					System.out.println("XMLStreamConstants.ATTRIBUTE");
				} else if(eventType == XMLStreamConstants.DTD) {
					System.out.println("XMLStreamConstants.DTD");
				} else if(eventType == XMLStreamConstants.CDATA) {
					System.out.println("XMLStreamConstants.CDATA");
				} else if(eventType == XMLStreamConstants.NAMESPACE) {
					System.out.println("XMLStreamConstants.NAMESPACE");
				} else if(eventType == XMLStreamConstants.NOTATION_DECLARATION) {
					System.out.println("XMLStreamConstants.NOTATION_DECLARATION");
				} else if(eventType == XMLStreamConstants.ENTITY_DECLARATION) {
					System.out.println("XMLStreamConstants.ENTITY_DECLARATION");
				} else {
					throw new RuntimeException("didn't expect to get here");
				}
				
				// System.out.println(xmlEvent);				
			}			
		} catch (XMLStreamException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {}
		}
	}
}