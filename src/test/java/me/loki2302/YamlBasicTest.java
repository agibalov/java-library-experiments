package me.loki2302;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class YamlBasicTest {
    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    @Test
    public void canSerializeToYaml() throws JsonProcessingException {
        Person person = new Person();
        person.id = 123;
        person.name = "loki2302";
        String yamlString = OBJECT_MAPPER.writeValueAsString(person);

        assertEquals("---\nid: 123\nname: \"loki2302\"\n", yamlString);
    }

    @Test
    public void canDeserializeFromYaml() throws IOException {
        String yamlString = String.join("\n",
                "title: test post",
                "text: >",
                " line1",
                " line2",
                " line3");
        Post post = OBJECT_MAPPER.readValue(yamlString, Post.class);
        assertEquals("test post", post.title);
        assertEquals("line1 line2 line3", post.text);
    }

    @Test
    public void canHandleLineBreaksAsSpaces() throws IOException {
        String yamlString = String.join("\n",
                "title: test post",
                "text: >",
                " line1",
                " line2",
                " line3");
        Post post = OBJECT_MAPPER.readValue(yamlString, Post.class);
        assertEquals("test post", post.title);
        assertEquals("line1 line2 line3", post.text);
    }

    @Test
    public void canHandleLineBreaksAsCarriageReturn() throws IOException {
        String yamlString = String.join("\n",
                "title: test post",
                "text: |",
                " line1",
                " line2",
                " line3");
        Post post = OBJECT_MAPPER.readValue(yamlString, Post.class);
        assertEquals("test post", post.title);
        assertEquals("line1\nline2\nline3", post.text);
    }

    @Test
    public void canLoadMultipleDocuments() throws IOException {
        String yamlString = "title: the title here\n---\nthe content here\n";

        MappingIterator<JsonNode> it = OBJECT_MAPPER.readerFor(JsonNode.class)
                .readValues(yamlString);

        JsonNode metaDataNode = it.next();
        assertTrue(metaDataNode.isObject());
        assertTrue(metaDataNode.has("title"));
        assertEquals("the title here", metaDataNode.get("title").asText());

        JsonNode contentNode = it.next();
        assertTrue(contentNode.isTextual());
        assertEquals("the content here", contentNode.asText());
    }

    /**
     * TODO: didn't manage to make this work with ObjectMapper, give it another try
     */
    @Test
    public void canUseReferences() {
        String yamlString = "something: &xxx hi there\nsomethingElse: *xxx\n";
        Map<String, Object> m = new Yaml().loadAs(yamlString, Map.class);
        assertEquals("hi there", m.get("something"));
        assertEquals("hi there", m.get("somethingElse"));
    }

    public static class Person {
        public int id;
        public String name;
    }

    public static class Post {
        public String title;
        public String text;
    }
}
