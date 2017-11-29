package me.loki2302;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Illustrates how Jackson maps one class hierarchy to another.
 */
public class ObjectMappingTest {
    @Test
    public void canMapHierarchies() throws IOException {
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

        OperationImplementation operationImplementation = objectMapper.convertValue(
                config.operation,
                OperationImplementation.class);
        System.out.println(operationImplementation);

        assertEquals("CATS!!!1ONE", operationImplementation.apply("cats"));
        assertEquals("beer!!!1one", operationImplementation.apply("beer"));
    }

    public static class Config {
        public OperationDescription operation;
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

    public static class AppendOperationDescription implements OperationDescription {
        public String suffix;
    }

    public static class ToUpperCaseOperationDescription implements OperationDescription {
    }

    public static class SequenceOperationDescription implements OperationDescription {
        public List<OperationDescription> children;
    }

    public static class IfOperationDescription implements OperationDescription {
        public String regex;
        public OperationDescription then;
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = AppendOperationImplementation.class, name = "append"),
            @JsonSubTypes.Type(value = ToUpperCaseOperationImplementation.class, name = "uppercase"),
            @JsonSubTypes.Type(value = SequenceOperationImplementation.class, name = "sequence"),
            @JsonSubTypes.Type(value = IfOperationImplementation.class, name = "if")
    })
    public interface OperationImplementation {
        String apply(String s);
    }

    public static class AppendOperationImplementation implements OperationImplementation {
        public String suffix;

        @Override
        public String apply(String s) {
            return s + suffix;
        }
    }

    public static class ToUpperCaseOperationImplementation implements OperationImplementation {
        @Override
        public String apply(String s) {
            return s.toUpperCase();
        }
    }

    public static class SequenceOperationImplementation implements OperationImplementation {
        public List<OperationImplementation> children;

        @Override
        public String apply(String s) {
            for(OperationImplementation operation : children) {
                s = operation.apply(s);
            }
            return s;
        }
    }

    public static class IfOperationImplementation implements OperationImplementation {
        public String regex;
        public OperationImplementation then;

        @Override
        public String apply(String s) {
            if(s.matches(regex)) {
                s = then.apply(s);
            }
            return s;
        }
    }
}
