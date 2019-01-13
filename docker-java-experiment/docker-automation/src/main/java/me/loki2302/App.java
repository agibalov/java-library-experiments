package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;

import java.util.List;

public class App {
    public static void main(String[] args) {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withApiVersion("1.23")
                .build();
        DockerClient dockerClient = DockerClientBuilder.getInstance(dockerClientConfig).build();

        List<SearchItem> searchItems = dockerClient.searchImagesCmd("ubuntu").exec();
        for(SearchItem searchItem : searchItems) {
            System.out.printf("%s %b\n", searchItem.getName(), searchItem.isOfficial());
        }
    }
}
