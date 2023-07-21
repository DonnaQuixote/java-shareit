package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RequestServiceTest {
    @Autowired
    RequestService requestService;
    @Autowired
    UserService userService;
    UserDto userDto;
    UserDto userDto2;
    ItemDto itemDto;
    RequestDto requestDto;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().name("test").email("test@test.com").build();
        userDto2 = UserDto.builder().name("test2").email("test2@test.com").build();
        itemDto = ItemDto.builder().name("test name").description("test desc").available(true).build();
        userDto = userService.postUser(userDto);
        userDto2 = userService.postUser(userDto2);
        requestDto = RequestDto.builder().description("test desc").build();
        requestDto = requestService.postRequest(userDto.getId(), requestDto);
    }

    @Test
    void postRequestTest() {
        assertThat(requestDto.getRequester(), equalTo(userDto));
        assertThat(requestDto.getDescription(), equalTo("test desc"));
        assertThat(requestDto.getCreated(), notNullValue());
        assertThat(requestDto.getItems(), nullValue());
    }

    @Test
    void getRequestsTest() {
        List<RequestDto> requestsDto = requestService.getRequests(userDto.getId());
        assertThat(requestsDto.get(0).getRequester(), equalTo(userDto));
        assertThat(requestsDto.get(0).getDescription(), equalTo("test desc"));
        assertThat(requestsDto.get(0).getCreated(), notNullValue());
        assertThat(requestsDto.get(0).getItems(), equalTo(new ArrayList<>()));
        assertThat(requestsDto.size(), equalTo(1));
    }

    @Test
    void getAllRequestsTest() {
        List<RequestDto> requestsDto = requestService.getAllRequests(2L, 0, 10);
        assertThat(requestsDto.get(0).getRequester(), equalTo(userDto));
        assertThat(requestsDto.get(0).getDescription(), equalTo("test desc"));
        assertThat(requestsDto.get(0).getCreated(), notNullValue());
        assertThat(requestsDto.get(0).getItems(), equalTo(new ArrayList<>()));
        assertThat(requestsDto.size(), equalTo(1));
        List<RequestDto> otherRequestsDto = requestService.getAllRequests(1L, 0, 10);
        assertThat(otherRequestsDto.size(), equalTo(0));
    }

    @Test
    void getRequestTest() {
        requestDto = requestService.getRequest(1L, 1L);
        assertThat(requestDto.getRequester(), equalTo(userDto));
        assertThat(requestDto.getDescription(), equalTo("test desc"));
        assertThat(requestDto.getCreated(), notNullValue());
        assertThat(requestDto.getItems(), equalTo(new ArrayList<>()));
    }
}
