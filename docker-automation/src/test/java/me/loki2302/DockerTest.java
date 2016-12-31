package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.RemoteApiVersion;
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

    @Test
    public void canAttachContainersToANetwork() throws InterruptedException {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

        Network.Ipam.Config networkIpamConfig = new Network.Ipam.Config()
                .withSubnet("10.100.102.0/24");
        Network.Ipam networkIpam = new Network.Ipam().withConfig(networkIpamConfig);
        String networkId = dockerClient.createNetworkCmd()
                .withName("dummyNetwork")
                .withIpam(networkIpam)
                .exec()
                .getId();

        System.out.printf("dummyNetwork network has id %s\n", networkId);

        try {
            List<Network> networks = dockerClient.listNetworksCmd().exec();
            for (Network network : networks) {
                System.out.printf("id=%s, name=%s\n", network.getId(), network.getName());
            }

            dockerClient.pullImageCmd("ubuntu:14.04")
                    .exec(new PullImageResultCallback())
                    .awaitSuccess();

            String containerId = dockerClient.createContainerCmd("ubuntu:14.04")
                    .withCmd("ifconfig")
                    .exec()
                    .getId();
            try {
                ContainerNetwork.Ipam containerNetworkIpam = new ContainerNetwork.Ipam()
                        .withIpv4Address("10.100.102.11");
                ContainerNetwork containerNetwork = new ContainerNetwork()
                        .withNetworkID(networkId)
                        .withIpamConfig(containerNetworkIpam);
                dockerClient.connectToNetworkCmd()
                        .withContainerId(containerId)
                        .withNetworkId(networkId)
                        .withContainerNetwork(containerNetwork)
                        .exec();

                dockerClient.startContainerCmd(containerId).exec();

                Network updatedNetwork = dockerClient.inspectNetworkCmd().withNetworkId(networkId).exec();
                System.out.println(updatedNetwork.getContainers().entrySet().stream()
                        .map(e -> String.format("%s: %s", e.getKey(), e.getValue().getIpv4Address()))
                        .collect(Collectors.joining("\n")));

                try {
                    dockerClient.stopContainerCmd(containerId).exec();
                } catch (NotModifiedException e) {
                    // https://github.com/docker-java/docker-java/issues/98
                    // This exception is OK. It just means that container had already been stopped when I made this call.
                }

                int result = dockerClient.waitContainerCmd(containerId)
                        .exec(new WaitContainerResultCallback())
                        .awaitStatusCode();
                assertEquals(0, result);

                List<Frame> frames = new ArrayList<>();
                dockerClient.logContainerCmd(containerId)
                        .withStdOut(true)
                        .exec(new LogContainerResultCallback() {
                            @Override
                            public void onNext(Frame frame) {
                                frames.add(frame);
                            }
                        })
                        .awaitCompletion();

                List<String> stdoutFrames = frames.stream()
                        .filter(f -> f.getStreamType().equals(StreamType.STDOUT))
                        .map(f -> new String(f.getPayload(), Charset.forName("UTF-8")))
                        .collect(Collectors.toList());
                System.out.println(stdoutFrames.stream().collect(Collectors.joining()));

                assertTrue(stdoutFrames.stream().anyMatch(f -> f.contains("inet addr:10.100.102.11")));
            } finally {
                dockerClient.removeContainerCmd(containerId).exec();
            }
        } finally {
            dockerClient.removeNetworkCmd("dummyNetwork").exec();
        }
    }
}
