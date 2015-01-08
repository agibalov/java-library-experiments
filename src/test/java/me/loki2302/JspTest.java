package me.loki2302;

import com.mashape.unirest.http.Unirest;
import org.apache.jasper.servlet.JspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class JspTest {
    @Test
    public void canHostAServletThatDelegatesToJspServlet() throws Exception {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase(JspTest.class.getResource("/webroot").toURI().toASCIIString());
        webAppContext.addServlet(JspServlet.class, "*.jsp");
        webAppContext.addServlet(DummyServlet.class, "/");

        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);
        server.setHandler(webAppContext);

        server.start();
        try {
            String responseString = Unirest.get("http://localhost:8080/123").asString().getBody();
            assertEquals("jsp: hello\nel: hello\nservlet: hello\n", responseString);
        } finally {
            server.stop();
        }
    }

    @Test
    public void canUseTags() throws Exception {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase(JspTest.class.getResource("/webroot").toURI().toASCIIString());
        webAppContext.addServlet(JspServlet.class, "*.jsp");

        Server server = new Server();
        ServerConnector serverConnector = new ServerConnector(server);
        serverConnector.setPort(8080);
        server.addConnector(serverConnector);
        server.setHandler(webAppContext);

        server.start();
        try {
            String responseString = Unirest.get("http://localhost:8080/tag-tester.jsp")
                    .asString()
                    .getBody()
                    .trim();
            assertEquals("<div>hi there</div>", responseString);
        } finally {
            server.stop();
        }
    }

    public static class DummyServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setAttribute("message", "hello");
            getServletContext().getRequestDispatcher("/hello.jsp").forward(req, resp);
        }
    }
}
