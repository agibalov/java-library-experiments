package me.loki2302;

import org.junit.BeforeClass;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import static org.junit.Assert.assertEquals;

public class Dummy {
    private static ModelMapper modelMapper;
    private static PersonEntity personEntity;

    @BeforeClass
    public static void setUpModelMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @BeforeClass
    public static void buildPersonEntity() {
        personEntity = new PersonEntity();
        personEntity.id = 123L;
        personEntity.name = "loki2302";
    }

    @Test
    public void canMapLongToLong() {
        PersonDTOWithLongId personDTOWithLongId = modelMapper.map(personEntity, PersonDTOWithLongId.class);
        assertEquals(123L, (long)personDTOWithLongId.id);
        assertEquals("loki2302", personDTOWithLongId.name);
    }

    @Test
    public void canMapLongToInt() {
        PersonDTOWithIntId personDTOWithIntId = modelMapper.map(personEntity, PersonDTOWithIntId.class);
        assertEquals(123, personDTOWithIntId.id);
        assertEquals("loki2302", personDTOWithIntId.name);
    }

    @Test
    public void canMapLongToString() {
        PersonDTOWithStringId personDTOWithStringId = modelMapper.map(personEntity, PersonDTOWithStringId.class);
        assertEquals("123", personDTOWithStringId.id);
        assertEquals("loki2302", personDTOWithStringId.name);
    }

    private static class PersonEntity {
        public Long id;
        public String name;
    }

    private static class PersonDTOWithLongId {
        public Long id;
        public String name;
    }

    private static class PersonDTOWithIntId {
        public int id;
        public String name;
    }

    private static class PersonDTOWithStringId {
        public String id;
        public String name;
    }
}
