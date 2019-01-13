package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.PullImageResultCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.web.client.RestTemplate;

@TestExecutionListeners(value = {
        NginxTest.NginxTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class NginxTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void dummy() throws InterruptedException {
        String s = restTemplate.getForObject("http://localhost:8080/", String.class);
        System.out.println(s);
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    public static class NginxTestExecutionListener extends AbstractTestExecutionListener implements Ordered {
        private DockerClient dockerClient;
        private String containerId;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
            dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

            dockerClient.pullImageCmd("nginx:latest")
                    .exec(new PullImageResultCallback())
                    .awaitSuccess();

            containerId = dockerClient.createContainerCmd("nginx:latest")
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(8080), ExposedPort.tcp(80)))
                    .exec()
                    .getId();

            dockerClient.startContainerCmd(containerId).exec();

            Thread.sleep(1000);
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            dockerClient.stopContainerCmd(containerId).exec();
            dockerClient.removeContainerCmd(containerId).exec();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
