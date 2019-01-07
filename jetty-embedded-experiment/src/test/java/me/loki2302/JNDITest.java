package me.loki2302;

import com.mashape.unirest.http.Unirest;
import org.eclipse.jetty.plus.jndi.EnvEntry;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

// http://www.eclipse.org/jetty/documentation/9.2.1.v20140609/jndi-embedded.html
public class JNDITest {
    @Test
    public void canUseJNDI() throws Exception {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setConfigurationClasses(new String[] {
                // "org.eclipse.jetty.webapp.FragmentConfiguration", // when do I need it???
                "org.eclipse.jetty.plus.webapp.EnvConfiguration",
                // "org.eclipse.jetty.plus.webapp.PlusConfiguration" // when do I need it???
        });
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase(JNDITest.class.getResource("/webroot").toURI().toASCIIString());
        webAppContext.addServlet(DummyServlet.class, "/");

        // what do I do with it?
        EnvEntry dummyServiceEnvEntry = new EnvEntry(webAppContext, "dummyService", new DummyService("hi there"), false);

        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);
        server.setHandler(webAppContext);

        server.start();
        try {
            String responseString = Unirest.get("http://localhost:8080/").asString().getBody();
            assertEquals("dummyService says: hi there", responseString);
        } finally {
            server.stop();
        }
    }

    public static class DummyServlet extends HttpServlet {
        private DummyService dummyService;

        @Override
        public void init() throws ServletException {
            super.init();

            try {
                InitialContext initialContext = new InitialContext();
                dummyService = (DummyService)initialContext.lookup("java:comp/env/dummyService");
            } catch (NamingException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            resp.getWriter().write("dummyService says: " + dummyService.getData());
        }
    }

    public static class DummyService {
        private final String data;

        public DummyService(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }
}
