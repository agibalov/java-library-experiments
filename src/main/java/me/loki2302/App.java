package me.loki2302;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.modelmapper.ModelMapper;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        PersonEntity personEntity = new PersonEntity();
        personEntity.id = 123L;
        personEntity.name = "loki2302";

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);
        /*modelMapper.addMappings(new PropertyMap<PersonEntity, PersonDTOWithLongId>() {
            @Override
            protected void configure() {
                map(source.id, destination.id);
                map(source.name, destination.name);
            }
        });*/

        PersonDTOWithLongId personDTOWithLongId = modelMapper.map(personEntity, PersonDTOWithLongId.class);
        System.out.println(personDTOWithLongId);

        PersonDTOWithIntId personDTOWithIntId = modelMapper.map(personEntity, PersonDTOWithIntId.class);
        System.out.println(personDTOWithIntId);

        PersonDTOWithStringId personDTOWithStringId = modelMapper.map(personEntity, PersonDTOWithStringId.class);
        System.out.println(personDTOWithStringId);
    }

    public static class PersonEntity {
        public Long id;
        public String name;
    }

    public static abstract class FancyToString {
        @Override
        public String toString() {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
            try {
                return objectMapper.writeValueAsString(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class PersonDTOWithLongId extends FancyToString {
        public Long id;
        public String name;
    }

    public static class PersonDTOWithIntId extends FancyToString {
        public int id;
        public String name;
    }

    public static class PersonDTOWithStringId extends FancyToString {
        public String id;
        public String name;
    }
}
