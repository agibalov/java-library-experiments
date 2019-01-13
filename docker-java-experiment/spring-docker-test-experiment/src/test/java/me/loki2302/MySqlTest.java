package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;

import static org.junit.Assert.assertEquals;

@TestExecutionListeners(value = {
        MySqlTest.MySqlTestExecutionListener.class
}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
public class MySqlTest {
    @Autowired
    private NoteRepository noteRepository;

    @Test
    public void dummy() throws InterruptedException {
        assertEquals(0, noteRepository.count());

        Note note = new Note();
        note.text = "hello";

        noteRepository.save(note);

        assertEquals(1, noteRepository.count());
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    public static class MySqlTestExecutionListener extends AbstractTestExecutionListener implements Ordered {
        private DockerClient dockerClient;
        private String containerId;

        @Override
        public void beforeTestClass(TestContext testContext) throws Exception {
            DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder().build();
            dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

            dockerClient.pullImageCmd("mysql:latest")
                    .exec(new PullImageResultCallback())
                    .awaitSuccess();

            containerId = dockerClient.createContainerCmd("mysql:latest")
                    .withEnv(
                            "MYSQL_ROOT_PASSWORD=qwerty",
                            "MYSQL_DATABASE=xxx")
                    .withPortBindings(new PortBinding(Ports.Binding.bindPort(3306), ExposedPort.tcp(3306)))
                    .exec()
                    .getId();

            dockerClient.startContainerCmd(containerId).exec();

            if(true) {
                DataSource dataSource = DataSourceBuilder.create()
                        .driverClassName("com.mysql.cj.jdbc.Driver")
                        .username("root")
                        .password("qwerty")
                        .url("jdbc:mysql://localhost/xxx")
                        .build();
                JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                while (true) {
                    try {
                        jdbcTemplate.queryForObject("select 1 + 1", Integer.class);
                        break;
                    } catch (CannotGetJdbcConnectionException e) {
                        Thread.sleep(1000);
                    }
                }
            }

            if(false) {
                while (true) {
                    StringBuilder sb = new StringBuilder();
                    dockerClient.logContainerCmd(containerId)
                            .withTail(5)
                            .withStdErr(true)
                            .withStdOut(true)
                            .exec(new LogContainerResultCallback() {
                                @Override
                                public void onNext(Frame item) {
                                    String s = new String(item.getPayload(), Charset.forName("UTF-8"));
                                    sb.append(s);
                                }
                            }).awaitCompletion();

                    if (sb.toString().contains("End of list of non-natively partitioned tables")) {
                        break;
                    }

                    Thread.sleep(1000);
                }

                Thread.sleep(10000);
            }
        }

        @Override
        public void afterTestClass(TestContext testContext) throws Exception {
            dockerClient.stopContainerCmd(containerId).exec();
            dockerClient.waitContainerCmd(containerId).exec(new WaitContainerResultCallback()).awaitStatusCode();
            dockerClient.removeContainerCmd(containerId).exec();
        }

        @Override
        public int getOrder() {
            return 0;
        }
    }
}
