package me.loki2302;

import org.apache.olingo.commons.api.data.*;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.*;
import org.apache.olingo.commons.api.ex.ODataException;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.*;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public ServletRegistrationBean oDataServletRegistrationBean() {
        return new ServletRegistrationBean(new ODataServlet(), "/TodoService.svc/*");
    }

    public static class ODataServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                OData oData = OData.newInstance();
                ServiceMetadata serviceMetadata = oData.createServiceMetadata(new MyEdmProvider(), new ArrayList<>());
                ODataHttpHandler oDataHttpHandler = oData.createHandler(serviceMetadata);
                oDataHttpHandler.register(new MyEntityCollectionProcessor());
                oDataHttpHandler.process(req, resp);
            } catch(RuntimeException e) {
                throw new ServletException(e);
            }
        }
    }

    public static class MyEdmProvider extends CsdlAbstractEdmProvider {
        public final static String NAMESPACE = "OData.Test";

        public final static String CONTAINER_NAME = "Container";
        public final static FullQualifiedName CONTAINER = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

        public final static String ET_TODO_NAME = "Todo";
        public final static FullQualifiedName ET_TODO_FQN = new FullQualifiedName(NAMESPACE, ET_TODO_NAME);

        public final static String ES_TODOS_NAME = "Todos";

        @Override
        public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) throws ODataException {
            if(entityTypeName.equals(ET_TODO_FQN)) {
                CsdlProperty idProperty = new CsdlProperty().setName("Id").setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
                CsdlProperty textProperty = new CsdlProperty().setName("Text").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

                CsdlPropertyRef idPropertyRef = new CsdlPropertyRef();
                idPropertyRef.setName("Id");

                CsdlEntityType entityType = new CsdlEntityType();
                entityType.setName(ET_TODO_NAME);
                entityType.setProperties(Arrays.asList(idProperty, textProperty));
                entityType.setKey(Arrays.asList(idPropertyRef));

                return entityType;
            }

            return null;
        }

        @Override
        public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) throws ODataException {
            if(entityContainer.equals(CONTAINER)) {
                if(entitySetName.equals(ES_TODOS_NAME)) {
                    CsdlEntitySet entitySet = new CsdlEntitySet();
                    entitySet.setName(ES_TODOS_NAME);
                    entitySet.setType(ET_TODO_FQN);
                    return entitySet;
                }
            }

            return null;
        }

        @Override
        public CsdlEntityContainer getEntityContainer() throws ODataException {
            List<CsdlEntitySet> entitySets = new ArrayList<>();
            entitySets.add(getEntitySet(CONTAINER, ES_TODOS_NAME));

            CsdlEntityContainer entityContainer = new CsdlEntityContainer();
            entityContainer.setName(CONTAINER_NAME);
            entityContainer.setEntitySets(entitySets);

            return entityContainer;
        }

        @Override
        public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) throws ODataException {
            if(entityContainerName == null || entityContainerName.equals(CONTAINER)) {
                CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
                entityContainerInfo.setContainerName(CONTAINER);
                return entityContainerInfo;
            }

            return null;
        }

        @Override
        public List<CsdlSchema> getSchemas() throws ODataException {
            List<CsdlEntityType> entityTypes = new ArrayList<>();
            entityTypes.add(getEntityType(ET_TODO_FQN));

            CsdlSchema schema = new CsdlSchema();
            schema.setNamespace(NAMESPACE);
            schema.setEntityContainer(getEntityContainer());
            schema.setEntityTypes(entityTypes);

            List<CsdlSchema> schemas = new ArrayList<>();
            schemas.add(schema);

            return schemas;
        }
    }

    public static class MyEntityCollectionProcessor implements EntityCollectionProcessor {
        private OData oData;
        private ServiceMetadata serviceMetadata;

        @Override
        public void init(OData odata, ServiceMetadata serviceMetadata) {
            this.oData = odata;
            this.serviceMetadata = serviceMetadata;
        }

        @Override
        public void readEntityCollection(
                ODataRequest request,
                ODataResponse response,
                UriInfo uriInfo,
                ContentType responseFormat) throws ODataApplicationException, ODataLibraryException {

            List<UriResource> resourceParts = uriInfo.getUriResourceParts();
            UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet)resourceParts.get(0);
            EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

            EntityCollection entityCollection = getData(edmEntitySet);
            ODataSerializer oDataSerializer = oData.createSerializer(responseFormat);

            EdmEntityType edmEntityType = edmEntitySet.getEntityType();
            ContextURL contextURL = ContextURL.with().entitySet(edmEntitySet).build();

            String id = request.getRawBaseUri() + "/" + edmEntitySet.getName();
            EntityCollectionSerializerOptions entityCollectionSerializerOptions = EntityCollectionSerializerOptions.with()
                    .id(id).contextURL(contextURL).build();
            SerializerResult serializerResult = oDataSerializer.entityCollection(
                    serviceMetadata, edmEntityType, entityCollection, entityCollectionSerializerOptions);
            InputStream serializedContent = serializerResult.getContent();

            response.setContent(serializedContent);
            response.setStatusCode(HttpStatusCode.OK.getStatusCode());
            response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
        }

        private EntityCollection getData(EdmEntitySet edmEntitySet) {
            EntityCollection todoEntityCollection = new EntityCollection();

            if(edmEntitySet.getName().equals(MyEdmProvider.ES_TODOS_NAME)) {
                List<Entity> todoEntities = todoEntityCollection.getEntities();

                for(int i = 0; i < 5; ++i) {
                    Entity todoEntity = new Entity()
                            .addProperty(new Property(null, "Id", ValueType.PRIMITIVE, i))
                            .addProperty(new Property(null, "Text", ValueType.PRIMITIVE, String.format("Text %d", i)));
                    todoEntity.setId(makeId(MyEdmProvider.ES_TODOS_NAME, i));

                    todoEntities.add(todoEntity);
                }
            }

            return todoEntityCollection;
        }

        private URI makeId(String entitySetName, int id) {
            try {
                return new URI(String.format("%s(%d)", entitySetName, id));
            } catch (URISyntaxException e) {
                throw new ODataRuntimeException("Can't create id for " + entitySetName, e);
            }
        }
    }
}
