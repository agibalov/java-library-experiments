package me.loki2302;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;

public class AppTest {
    @Test
    public void dummyTest() throws Exception {
        Server server = new Server(8080);
        server.setHandler(new AbstractHandler() {
            public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().println("hello there");
                ((Request)request).setHandled(true);
                
                System.out.printf("request from %s, URL=%s\n", request.getRemoteAddr(), request.getRequestURI());
            }
        });
        server.start();
                
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            try {
                HttpGet httpGet = new HttpGet("http://localhost:8080/");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                assertEquals(200, httpResponse.getStatusLine().getStatusCode());
                
                HttpEntity responseEntity = httpResponse.getEntity();
                assertNotNull(responseEntity);
                InputStream inputStream = responseEntity.getContent();
                try {
                    String responseString = IOUtils.toString(inputStream);
                    assertEquals("hello there\n", responseString);
                } finally {
                    inputStream.close();
                }
            } finally {
                httpClient.close();
            }
        } finally {
            server.stop();            
        }       
    }
}
