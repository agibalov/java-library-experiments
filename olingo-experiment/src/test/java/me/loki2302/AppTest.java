package me.loki2302;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataServiceDocumentRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientServiceDocument;
import org.apache.olingo.client.core.ODataClientFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@IntegrationTest
@WebAppConfiguration
@SpringApplicationConfiguration(App.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AppTest {
    @Test
    public void dummy() {
        ODataClient oDataClient = ODataClientFactory.getClient();

        ODataServiceDocumentRequest request = oDataClient.getRetrieveRequestFactory()
                .getServiceDocumentRequest("http://localhost:8080/TodoService.svc");
        request.setAccept("application/xml");

        ODataRetrieveResponse<ClientServiceDocument> response = request.execute();

        ClientServiceDocument clientServiceDocument = response.getBody();
        assertEquals(1, clientServiceDocument.getEntitySetNames().size());
        assertTrue(clientServiceDocument.getEntitySetNames().contains("Todos"));
    }
}
