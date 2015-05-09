package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

public class DockerTest {
    @Test
    public void canFindUbuntu() {
        DockerClientConfig dockerClientConfig = DockerClientConfig.createDefaultConfigBuilder()
                .withUri("unix:///var/run/docker.sock")
                .withVersion("1.14")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

        List<SearchItem> searchItems = dockerClient.searchImagesCmd("ubuntu").exec();
        assertFalse(searchItems.isEmpty());

        SearchItem ubuntuSearchItem = null;
        for(SearchItem searchItem : searchItems) {
            if(searchItem.getName().equals("ubuntu") && searchItem.isOfficial()) {
                ubuntuSearchItem = searchItem;
                break;
            }
        }

        System.out.printf("Got ubuntu: %s (%b)\n",
                ubuntuSearchItem.getName(),
                ubuntuSearchItem.isOfficial());

        assertNotNull(ubuntuSearchItem);
    }

    @Test
    public void canCreateStartAndStopContainer() {
        DockerClientConfig dockerClientConfig = DockerClientConfig.createDefaultConfigBuilder()
                .withUri("unix:///var/run/docker.sock")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd("ubuntu")
                .withCmd("echo", "hello world")
                .withCpuset("0")
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        try {
            String containerId = createContainerResponse.getId();

            dockerClient.startContainerCmd(containerId).exec();
            dockerClient.stopContainerCmd(containerId).exec();
            Integer result = dockerClient.waitContainerCmd(containerId).exec();
            assertEquals(0, result.intValue());

            try(InputStream inputStream = dockerClient.logContainerCmd(containerId).withStdOut().exec()) {
                String output = IOUtils.toString(inputStream);
                assertTrue(output.contains("hello world"));
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            dockerClient.removeContainerCmd(createContainerResponse.getId());
        }
    }
}
