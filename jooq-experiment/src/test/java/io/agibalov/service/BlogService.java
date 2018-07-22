package io.agibalov.service;

import io.agibalov.db.tables.records.CommentsRecord;
import io.agibalov.db.tables.records.PostsRecord;
import io.agibalov.db.tables.records.UsersRecord;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Record;
import org.jooq.SelectWhereStep;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.agibalov.db.Tables.COMMENTS;
import static io.agibalov.db.Tables.POSTS;
import static io.agibalov.db.Tables.USERS;

public class BlogService {
    @Autowired
    private DSLContext dslContext;

    public void putUser(String id, Instant createdAt, String name) {
        UsersRecord usersRecord = dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOne();
        if(usersRecord == null) {
            usersRecord = dslContext.newRecord(USERS);
            usersRecord.setId(id);
            usersRecord.setName(name);
            usersRecord.setCreatedat(Timestamp.from(createdAt));
            usersRecord.setPostcount(0);
            usersRecord.setCommentcount(0);
            dslContext.executeInsert(usersRecord);
        } else {
            dslContext.update(USERS)
                    .set(USERS.NAME, name)
                    .where(USERS.ID.eq(id))
                    .execute();
        }
    }

    public UserDto getUser(String id) {
        UsersRecord usersRecord = dslContext.selectFrom(USERS)
                .where(USERS.ID.eq(id))
                .fetchOne();
        if(usersRecord == null) {
            return null;
        }

        return UserDto.builder()
                .id(usersRecord.getId())
                .name(usersRecord.getName())
                .createdAt(usersRecord.getCreatedat().toInstant())
                .postCount(usersRecord.getPostcount())
                .commentCount(usersRecord.getCommentcount())
                .build();
    }

    public void putPost(String id, Instant createdAt, String title, String text, String authorId) {
        PostsRecord postsRecord = dslContext.selectFrom(POSTS)
                .where(POSTS.ID.eq(id))
                .fetchOne();
        if(postsRecord == null) {
            postsRecord = dslContext.newRecord(POSTS);
            postsRecord.setId(id);
            postsRecord.setTitle(title);
            postsRecord.setText(text);
            postsRecord.setAuthorid(authorId);
            postsRecord.setCreatedat(Timestamp.from(createdAt));
            postsRecord.setCommentcount(0);
            dslContext.executeInsert(postsRecord);

            dslContext.update(USERS)
                    .set(USERS.POSTCOUNT, USERS.POSTCOUNT.add(1))
                    .where(USERS.ID.eq(authorId))
                    .execute();
        } else {
            dslContext.update(POSTS)
                    .set(POSTS.TITLE, title)
                    .set(POSTS.TEXT, text)
                    .where(POSTS.ID.eq(id))
                    .execute();
        }
    }

    public PostDto getPost(String id) {
        return selectPostsWithAuthors(dslContext)
                .where(POSTS.ID.eq(id))
                .fetchOptional()
                .map(BlogService::postDtoFromRecord)
                .orElse(null);
    }

    public List<PostDto> getAllPosts(OrderField<?> orderBy) {
        return selectPostsWithAuthors(dslContext)
                .orderBy(orderBy)
                .fetchStream()
                .map(BlogService::postDtoFromRecord)
                .collect(Collectors.toList());
    }

    public static OrderField orderByPostCreatedAtAsc() {
        return POSTS.CREATEDAT.asc();
    }

    public static OrderField orderByPostCreatedAtDesc() {
        return POSTS.CREATEDAT.desc();
    }

    private static SelectWhereStep<Record> selectPostsWithAuthors(DSLContext dslContext) {
        return dslContext.select()
                .from(POSTS)
                .join(USERS).on(USERS.ID.eq(POSTS.AUTHORID));
    }

    private static PostDto postDtoFromRecord(Record record) {
        PostsRecord postsRecord = record.into(POSTS);
        UsersRecord usersRecord = record.into(USERS);

        return PostDto.builder()
                .id(postsRecord.getId())
                .title(postsRecord.getTitle())
                .text(postsRecord.getText())
                .createdAt(postsRecord.getCreatedat().toInstant())
                .commentCount(postsRecord.getCommentcount())
                .author(BriefUserDto.builder()
                        .id(usersRecord.getId())
                        .name(usersRecord.getName())
                        .build())
                .build();
    }

    public void putComment(String id, Instant createdAt, String text, String postId, String authorId) {
        CommentsRecord commentsRecord = dslContext.selectFrom(COMMENTS)
                .where(COMMENTS.ID.eq(id))
                .fetchOne();
        if(commentsRecord == null) {
            commentsRecord = dslContext.newRecord(COMMENTS);
            commentsRecord.setId(id);
            commentsRecord.setText(text);
            commentsRecord.setPostid(postId);
            commentsRecord.setAuthorid(authorId);
            commentsRecord.setCreatedat(Timestamp.from(createdAt));
            dslContext.executeInsert(commentsRecord);

            dslContext.update(USERS)
                    .set(USERS.COMMENTCOUNT, USERS.COMMENTCOUNT.add(1))
                    .where(USERS.ID.eq(authorId))
                    .execute();

            dslContext.update(POSTS)
                    .set(POSTS.COMMENTCOUNT, POSTS.COMMENTCOUNT.add(1))
                    .where(POSTS.ID.eq(postId))
                    .execute();
        } else {
            dslContext.update(COMMENTS)
                    .set(COMMENTS.TEXT, text)
                    .where(COMMENTS.ID.eq(id))
                    .execute();
        }
    }
}
