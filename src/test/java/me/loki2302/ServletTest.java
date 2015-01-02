package me.loki2302;

import com.google.inject.*;
import com.google.inject.servlet.*;
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
import java.util.Map;

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
            HttpResponse<String> response = Unirest.get("http://localhost:8080/123?a=222").asString();
            assertEquals("hello-/123-222", response.getBody());
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
                    bind(DummyServlet.class).in(Singleton.class);
                    bind(DummyPojo.class).in(ServletScopes.REQUEST);

                    serve("/*").with(DummyServlet.class);
                }
            });
        }
    }

    public static class DummyServlet extends HttpServlet {
        @Inject
        private Provider<DummyPojo> dummyPojoProvider;

        @Override
        protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            DummyPojo dummyPojo = dummyPojoProvider.get();

            PrintWriter printWriter = resp.getWriter();
            try {
                printWriter.write(
                        "hello-" +
                        dummyPojo.request.getRequestURI() +
                        "-" +
                        dummyPojo.params.get("a")[0]);
            } finally {
                printWriter.close();
            }
        }
    }

    public static class DummyPojo {
        @Inject
        public HttpServletRequest request;

        @Inject
        public HttpServletResponse response;

        @Inject
        @RequestParameters
        public Map<String, String[]> params;
    }
}
