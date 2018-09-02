package io.agibalov;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeName;
import graphql.schema.GraphQLSchema;
import graphql.schema.StaticDataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.junit.Test;

import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;
import static org.junit.Assert.assertEquals;

public class DummyTest {
    @Test
    public void dummy() throws URISyntaxException {
        SchemaParser schemaParser = new SchemaParser();
        TypeDefinitionRegistry typeDefinitionRegistry = schemaParser
                .parse(new File(DummyTest.class.getClassLoader().getResource("my.graphqls").toURI()));

        RuntimeWiring runtimeWiring = newRuntimeWiring()
                .type("Query", builder -> builder
                        .dataFetcher("postById", dfe -> {
                            String id = dfe.getArgument("id");
                            Map<String, Object> data = new HashMap<String, Object>() {{
                                put("id", id);
                                put("title", "the title " + id);
                                put("text", "the text " + id);
                            }};
                            return data;
                        })
                )
                .type("Mutation", builder -> builder
                        .dataFetcher("createPost", dfe -> {
                            System.out.printf("createPost: title=%s text=%s\n",
                                    dfe.getArgument("title"),
                                    dfe.getArgument("text"));
                            return "hello";
                        }))
                .build();

        SchemaGenerator schemaGenerator = new SchemaGenerator();
        GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
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
}
