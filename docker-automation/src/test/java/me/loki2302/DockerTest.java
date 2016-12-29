package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.api.model.StreamType;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.LogContainerResultCallback;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.github.dockerjava.core.command.WaitContainerResultCallback;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class DockerTest {
    @Test
    public void canFindUbuntu() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withApiVersion("1.23")
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
    public void canCreateStartAndStopContainer() throws InterruptedException {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

        dockerClient.pullImageCmd("ubuntu:16.04").exec(new PullImageResultCallback()).awaitSuccess();

        CreateContainerResponse createContainerResponse = dockerClient.createContainerCmd("ubuntu:16.04")
                .withCmd("echo", "hello world")
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();
        try {
            String containerId = createContainerResponse.getId();

            dockerClient.startContainerCmd(containerId).exec();
            dockerClient.stopContainerCmd(containerId).exec();
            int result = dockerClient.waitContainerCmd(containerId).exec(new WaitContainerResultCallback()).awaitStatusCode();
            assertEquals(0, result);

            List<Frame> frames = new ArrayList<>();
            dockerClient.logContainerCmd(containerId).withStdOut(true).exec(new LogContainerResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    frames.add(frame);
                }
            }).awaitCompletion();

            List<String> stdoutFrames = frames.stream()
                    .filter(f -> f.getStreamType().equals(StreamType.STDOUT))
                    .map(f -> new String(f.getPayload(), Charset.forName("UTF-8")))
                    .collect(Collectors.toList());
            assertEquals(1, stdoutFrames.size());
            assertEquals("hello world\n", stdoutFrames.get(0));
        } finally {
            dockerClient.removeContainerCmd(createContainerResponse.getId()).exec();
        }
    }
}
