package io.agibalov;

import io.agibalov.service.BlogService;
import io.agibalov.service.PostDto;
import io.agibalov.service.UserDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class BlogServiceTest {
    @Autowired
    private BlogService blogService;

    @Test
    public void itShouldWork() {
        blogService.putUser("user1", Instant.parse("2018-07-22T12:00:00Z"), "User One");
        UserDto userDto = blogService.getUser("user1");
        assertEquals("user1", userDto.getId());
        assertEquals("User One", userDto.getName());
        assertNotNull(userDto.getCreatedAt());
        assertEquals(0, userDto.getPostCount());
        assertEquals(0, userDto.getCommentCount());

        blogService.putPost("post1", Instant.parse("2018-07-22T12:00:01Z"), "Post One", "The Post One", "user1");
        assertEquals(1, blogService.getUser("user1").getPostCount());
        assertEquals(0, blogService.getUser("user1").getCommentCount());
        assertEquals(0, blogService.getPost("post1").getCommentCount());

        blogService.putComment("comment1", Instant.parse("2018-07-22T12:00:02Z"), "Comment One", "post1", "user1");
        assertEquals(1, blogService.getUser("user1").getPostCount());
        assertEquals(1, blogService.getUser("user1").getCommentCount());
        assertEquals(1, blogService.getPost("post1").getCommentCount());

        PostDto postDto = blogService.getPost("post1");
        assertEquals("user1", postDto.getAuthor().getId());
        assertEquals("User One", postDto.getAuthor().getName());
    }

    @Test
    public void itShouldSortPosts() {
        blogService.putUser("user1", Instant.parse("2018-07-22T12:00:00Z"), "User One");
        for(int i = 0; i < 10; ++i) {
            Instant currentTime = Instant.parse("2018-07-22T12:00:00Z").plusSeconds(i);
            blogService.putPost(String.format("post%d", i), currentTime, "Post One", "Post One Text", "user1");
        }

        {
            List<PostDto> posts = blogService.getAllPosts(BlogService.orderByPostCreatedAtAsc());
            assertEquals("post0", posts.get(0).getId());
            assertEquals("post1", posts.get(1).getId());
        }

        {
            List<PostDto> posts = blogService.getAllPosts(BlogService.orderByPostCreatedAtDesc());
            assertEquals("post9", posts.get(0).getId());
            assertEquals("post8", posts.get(1).getId());
        }
    }

    @SpringBootApplication
    public static class Config {
        @Bean
        public BlogService blogService() {
            return new BlogService();
        }
    }
}
