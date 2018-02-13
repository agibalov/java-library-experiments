package io.agibalov;

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
        assertTrue(FRUIT_MAPPER.fruitFromFruitDto(new AppleDto()) instanceof Apple);
        assertTrue(FRUIT_MAPPER.fruitFromFruitDto(new BananaDto()) instanceof Banana);
    }

    @Test
    public void canMapManyBasedOnConcreteType() {
        List<Fruit> fruits = FRUIT_MAPPER.fruitsFromFruitDtos(Arrays.asList(new AppleDto(), new BananaDto()));
        assertEquals(2, fruits.size());
        assertEquals(Apple.class, fruits.get(0).getClass());
        assertEquals(Banana.class, fruits.get(1).getClass());
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

    public interface Fruit {}
    public static class Apple implements Fruit {}
    public static class Banana implements Fruit {}

    public interface FruitDto {}
    public static class AppleDto implements FruitDto {}
    public static class BananaDto implements FruitDto {}
}
