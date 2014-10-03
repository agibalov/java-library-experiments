package me.loki2302;

import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;

import static org.junit.Assert.assertEquals;

public class CamelCaseToUnderscoreTest {
    private static ModelMapper modelMapper;
    private static PersonEntity personEntity;

    @BeforeClass
    public static void setUpModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSourceNameTokenizer(NameTokenizers.CAMEL_CASE)
                .setDestinationNameTokenizer(NameTokenizers.UNDERSCORE);
    }

    @BeforeClass
    public static void buildPersonEntity() {
        personEntity = new PersonEntity();
        personEntity.personEntityId = 123L;
        personEntity.personEntityName = "loki2302";
    }

    @Test
    public void canMapCamelCaseToUnderscore() {
        PersonDTO personDTO = modelMapper.map(personEntity, PersonDTO.class);
        assertEquals(123L, (long)personDTO.person_entity_id);
        assertEquals("loki2302", personDTO.person_entity_name);
    }

    private static class PersonEntity {
        public Long personEntityId;
        public String personEntityName;
    }

    private static class PersonDTO {
        public Long person_entity_id;
        public String person_entity_name;
    }
}
