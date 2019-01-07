package me.loki2302;

import com.mashape.unirest.http.Unirest;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class AppTest {
    @Test
    public void testWebAppContextHandler() throws Exception {
        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setContextPath("/");
        webAppContext.setResourceBase(".");
        webAppContext.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent servletContextEvent) {
                ServletContext servletContext = servletContextEvent.getServletContext();
                ServletRegistration.Dynamic servletRegistration =
                        servletContext.addServlet("MyDummyServlet1", new DummyServlet("helloDynamicRegistration"));
                servletRegistration.addMapping("/1");
            }

            @Override
            public void contextDestroyed(ServletContextEvent servletContextEvent) {
            }
        });

        Server server = new Server(8080);
        server.setHandler(webAppContext);
        server.start();
        try {
            String responseString = Unirest.get("http://localhost:8080/1").asString().getBody();
            assertEquals("helloDynamicRegistration", responseString);
        } finally {
            server.stop();
        }
    }

    public static class DummyServlet extends HttpServlet {
        private final String message;

        public DummyServlet(String message) {
            this.message = message;
        }

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter printWriter = resp.getWriter();
            printWriter.print(message);
            printWriter.close();
        }
    }

    @Test
    public void testServletContextHandler() throws Exception {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(new HttpServlet() {
            @Override
            public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                PrintWriter printWriter = res.getWriter();
                printWriter.print("hello");
                printWriter.close();
            }
        }), "/*");

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();

        try {
            String responseString = Unirest.get("http://localhost:8080/").asString().getBody();
            assertEquals("hello", responseString);
        } finally {
            server.stop();
        }
    }

    @Test
    public void testCustomHandler() throws Exception {
        Server server = new Server(8080);
        server.setHandler(new AbstractHandler() {
            @Override
            public void handle(
                    String target, 
                    Request baseRequest, 
                    HttpServletRequest request, 
                    HttpServletResponse response) throws IOException, ServletException {
                
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().print("hello there");
                ((Request)request).setHandled(true);
            }
        });
        server.start();
                
        try {
            String responseString = Unirest.get("http://localhost:8080/").asString().getBody();
            assertEquals("hello there", responseString);
        } finally {
            server.stop();            
        }       
    }
}
