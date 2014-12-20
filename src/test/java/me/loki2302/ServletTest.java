package me.loki2302;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;

public class ServletTest {
    @Test
    public void minimalGuiceWebAppWorks() throws Exception {
        ServletContextHandler servletContextHandler = new ServletContextHandler();
        servletContextHandler.addEventListener(new DummyGuiceServletContextListener());
        servletContextHandler.addFilter(GuiceFilter.class, "/*", null);

        Server server = new Server(8080);
        server.setHandler(servletContextHandler);
        server.start();
        try {
            HttpResponse<String> response = Unirest.get("http://localhost:8080/123").asString();
            assertEquals("hello", response.getBody());
        } finally {
            server.stop();
        }
    }

    public static class DummyGuiceServletContextListener extends GuiceServletContextListener {
        @Override
        protected Injector getInjector() {
            return Guice.createInjector(new ServletModule() {
                @Override
                protected void configureServlets() {
                    bind(DummyServlet.class).asEagerSingleton();

                    serve("/*").with(DummyServlet.class);
                }
            });
        }
    }

    public static class DummyServlet extends HttpServlet {
        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter printWriter = resp.getWriter();
            try {
                printWriter.write("hello");
            } finally {
                printWriter.close();
            }
        }
    }
}
