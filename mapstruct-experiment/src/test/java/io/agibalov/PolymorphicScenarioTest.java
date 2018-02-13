package io.agibalov;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PolymorphicScenarioTest {
    private final FruitMapper FRUIT_MAPPER = Mappers.getMapper(FruitMapper.class);

    @Test
    public void canMapBasedOnConcreteType() {
        Fruit fruit1 = FRUIT_MAPPER.fruitFromFruitDto(new AppleDto("f", "a"));
        assertTrue(fruit1 instanceof Apple);
        Apple apple = (Apple)fruit1;
        assertEquals("f", apple.getFruitDescriptor());
        assertEquals("a", apple.getAppleDescriptor());

        Fruit fruit2 = FRUIT_MAPPER.fruitFromFruitDto(new BananaDto("f", "b"));
        assertTrue(fruit2 instanceof Banana);
        Banana banana = (Banana)fruit2;
        assertEquals("f", banana.getFruitDescriptor());
        assertEquals("b", banana.getBananaDescriptor());
    }

    @Test
    public void canMapManyBasedOnConcreteType() {
        List<Fruit> fruits = FRUIT_MAPPER.fruitsFromFruitDtos(Arrays.asList(
                new AppleDto("f1", "a"),
                new BananaDto("f2", "b")));

        assertEquals(2, fruits.size());

        Fruit fruit1 = fruits.get(0);
        assertTrue(fruit1 instanceof Apple);
        Apple apple = (Apple)fruit1;
        assertEquals("f1", apple.getFruitDescriptor());
        assertEquals("a", apple.getAppleDescriptor());

        Fruit fruit2 = fruits.get(1);
        assertTrue(fruit2 instanceof Banana);
        Banana banana = (Banana)fruit2;
        assertEquals("f2", banana.getFruitDescriptor());
        assertEquals("b", banana.getBananaDescriptor());
    }

    @Mapper
    public static abstract class FruitMapper {
        abstract Apple appleFromAppleDto(AppleDto appleDto);
        abstract Banana bananaFromBananaDto(BananaDto bananaDto);
        abstract List<Fruit> fruitsFromFruitDtos(List<FruitDto> fruitDtos);

        public Fruit fruitFromFruitDto(FruitDto fruitDto) {
            if(fruitDto instanceof AppleDto) {
                return appleFromAppleDto((AppleDto)fruitDto);
            } else if(fruitDto instanceof BananaDto) {
                return bananaFromBananaDto((BananaDto)fruitDto);
            }
            throw new RuntimeException("Unknown FruitDto subclass " + fruitDto.getClass());
        }
    }

    public interface Fruit {
        void setFruitDescriptor(String value);
        String getFruitDescriptor();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Apple implements Fruit {
        private String fruitDescriptor;
        private String appleDescriptor;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Banana implements Fruit {
        private String fruitDescriptor;
        private String bananaDescriptor;
    }

    public interface FruitDto {
        void setFruitDescriptor(String value);
        String getFruitDescriptor();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppleDto implements FruitDto {
        private String fruitDescriptor;
        private String appleDescriptor;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BananaDto implements FruitDto {
        private String fruitDescriptor;
        private String bananaDescriptor;
    }
}
