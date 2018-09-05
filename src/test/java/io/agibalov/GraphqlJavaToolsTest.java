package io.agibalov;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import com.coxautodev.graphql.tools.SchemaParser;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import lombok.Data;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class GraphqlJavaToolsTest {
    @Test
    public void dummy() {
        SchemaParser schemaParser = SchemaParser.newParser()
                .file("my.graphqls")
                .resolvers(new GraphQLQueryResolver() {
                    public Post postById(String id) {
                        Post post = new Post();
                        post.setId(id);
                        post.setTitle("the title " + id);
                        post.setText("the text " + id);
                        return post;
                    }
                }, new GraphQLMutationResolver() {
                    public String createPost(String title, String text) {
                        System.out.printf("createPost: title=%s text=%s\n", title, text);
                        return "hello";
                    }
                })
                .build();
        GraphQLSchema graphQLSchema = schemaParser.makeExecutableSchema();
        GraphQL build = GraphQL.newGraphQL(graphQLSchema).build();

        {
            ExecutionResult executionResult = build.execute("{ postById(id:\"myid1\") { id title text } }");
            Map<String, Map<String, Object>> data = executionResult.getData();
            Map<String, Object> post = data.get("postById");
            assertEquals("myid1", post.get("id"));
            assertEquals("the title myid1", post.get("title"));
            assertEquals("the text myid1", post.get("text"));
        }

        {
            ExecutionResult executionResult = build.execute(builder -> builder
                    .query("mutation CreatePost($title: String!, $text: String!) { createPost(title:$title, text:$text) }")
                    .variables(new HashMap<String, Object>() {{
                        put("title", "the title");
                        put("text", "the text");
                    }}));
            Map<String, String> data = executionResult.getData();
            assertEquals("hello", data.get("createPost"));
        }
    }

    @Data
    public static class Post {
        private String id;
        private String title;
        private String text;
    }
}
