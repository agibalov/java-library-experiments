package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Illustrates how Dozer maps one class hierarchy to another.
 */
public class ParallelHierarchiesTest {
    @Test
    public void dummy() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        //language=yaml
        String yaml = "operation:\n" +
                "  type: sequence\n" +
                "  children:\n" +
                "  - type: append\n" +
                "    suffix: \"!!!1one\"\n" +
                "  - type: if\n" +
                "    regex: \"^cat.+\"\n" +
                "    then:\n" +
                "      type: uppercase";

        Config config = objectMapper.readValue(yaml, Config.class);
        System.out.println(config);

        OperationImplementation operationImplementation = makeOperation(config);
        System.out.println(operationImplementation);

        assertEquals("CATS!!!1ONE", operationImplementation.apply("cats"));
        assertEquals("beer!!!1one", operationImplementation.apply("beer"));
    }

    private static OperationImplementation makeOperation(Config config) {
        DozerBeanMapper mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(AppendOperationDescription.class, AppendOperationImplementation.class);
                mapping(ToUpperCaseOperationDescription.class, ToUpperCaseOperationImplementation.class);
                mapping(SequenceOperationDescription.class, SequenceOperationImplementation.class);
                mapping(IfOperationDescription.class, IfOperationImplementation.class);
            }
        });

        OperationImplementation operationImplementation = mapper.map(
                config.operation,
                OperationImplementation.class);

        return operationImplementation;
    }

    @Data
    @NoArgsConstructor
    public static class Config {
        private OperationDescription operation;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AppendOperationDescription.class, name = "append"),
            @JsonSubTypes.Type(value = ToUpperCaseOperationDescription.class, name = "uppercase"),
            @JsonSubTypes.Type(value = SequenceOperationDescription.class, name = "sequence"),
            @JsonSubTypes.Type(value = IfOperationDescription.class, name = "if")
    })
    public interface OperationDescription {
    }

    @Data
    @NoArgsConstructor
    public static class AppendOperationDescription implements OperationDescription {
        private String suffix;
    }

    @Data
    @NoArgsConstructor
    public static class ToUpperCaseOperationDescription implements OperationDescription {
    }

    @Data
    @NoArgsConstructor
    public static class SequenceOperationDescription implements OperationDescription {
        private List<OperationDescription> children;
    }

    @Data
    @NoArgsConstructor
    public static class IfOperationDescription implements OperationDescription {
        private String regex;
        private OperationDescription then;
    }

    public interface OperationImplementation {
        String apply(String s);
    }

    @Data
    @NoArgsConstructor
    public static class AppendOperationImplementation implements OperationImplementation {
        private String suffix;

        @Override
        public String apply(String s) {
            return s + suffix;
        }
    }

    @Data
    @NoArgsConstructor
    public static class ToUpperCaseOperationImplementation implements OperationImplementation {
        @Override
        public String apply(String s) {
            return s.toUpperCase();
        }
    }

    @Data
    @NoArgsConstructor
    public static class SequenceOperationImplementation implements OperationImplementation {
        private List<OperationImplementation> children;

        @Override
        public String apply(String s) {
            for(OperationImplementation operation : children) {
                s = operation.apply(s);
            }
            return s;
        }
    }

    @Data
    @NoArgsConstructor
    public static class IfOperationImplementation implements OperationImplementation {
        private String regex;
        private OperationImplementation then;

        @Override
        public String apply(String s) {
            if(s.matches(regex)) {
                s = then.apply(s);
            }
            return s;
        }
    }
}
