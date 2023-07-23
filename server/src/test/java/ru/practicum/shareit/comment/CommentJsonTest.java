package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class CommentJsonTest {
    @Autowired
    JacksonTester<CommentDto> jacksonTester;
    static CommentDto commentDto;
    static Comment comment;

    @BeforeAll
    static void beforeAll() {
        comment = new Comment(1L, "test",
                new Item(1L, "test name", "test desc", true, 2L),
                new User(3L, "test", "test@test.com"), LocalDateTime.now());
        commentDto = CommentMapper.toCommentDto(comment);
    }

    @Test
    void testJson() throws IOException {
        JsonContent<CommentDto> json = jacksonTester.write(commentDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(commentDto.getId()));
        assertThat(json).extractingJsonPathStringValue("$.text")
                .isEqualTo(commentDto.getText());
        assertThat(json).extractingJsonPathValue("$.item").isEqualTo(Math.toIntExact(commentDto.getItem()));
        assertThat(json).extractingJsonPathValue("$.author")
                .isEqualTo(Math.toIntExact(commentDto.getAuthor()));
        assertThat(json).extractingJsonPathValue("$.authorName").isEqualTo(commentDto.getAuthorName());
    }
}