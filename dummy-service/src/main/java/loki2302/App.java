package loki2302;

import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @RestController
    public static class DummyController {
        @Autowired
        private HazelcastInstance hazelcastInstance;

        @RequestMapping(value = "/api/data", method = RequestMethod.PUT)
        public void setData(@RequestBody String data) {
            Map<String, String> map = hazelcastInstance.getMap("TheMap");
            map.put("value", data);
        }

        @RequestMapping(value = "/api/data", method = RequestMethod.GET)
        public String getData() {
            Map<String, String> map = hazelcastInstance.getMap("TheMap");
            return map.get("value");
        }
    }
}
