package ru.practicum.shareit.request;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class RequestJsonTest {
    @Autowired
    JacksonTester<RequestDto> jacksonTester;
    static RequestDto requestDto;
    static Request request;

    @BeforeAll
    static void beforeAll() {
        request = new Request(1L, "test", LocalDateTime.MIN,
                new User(1L, "test", "test@test.com"),
                List.of(new Item(1L, "test name", "test desc", true, 2L)));
        requestDto = RequestMapper.toItemRequestDto(request);
    }

    @Test
    void testJson() throws IOException {
        JsonContent<RequestDto> json = jacksonTester.write(requestDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(requestDto.getId()));
        assertThat(json).extractingJsonPathStringValue("$.description")
                .isEqualTo(requestDto.getDescription());
        assertThat(json).extractingJsonPathValue("$.requester").isNotNull();
        assertThat(json).extractingJsonPathArrayValue("$.items").isNotEmpty();
    }

    @Test
    void requestSetterTest() {
        Item item = new Item(1L, "test name", "test desc", true, 2L);
        Request newRequest = new Request(1L, "test", LocalDateTime.MIN,
                new User(1L, "test", "test@test.com"));
        newRequest.setItems(List.of(item));
        MatcherAssert.assertThat(newRequest.getItems(), equalTo(List.of(item)));
    }
}