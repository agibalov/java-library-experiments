package me.loki2302;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

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
}
