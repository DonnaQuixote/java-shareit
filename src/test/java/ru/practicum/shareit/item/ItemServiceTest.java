package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemServiceTest {
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    @Autowired
    RequestService requestService;
    @Autowired
    BookingService bookingService;
    UserDto userDto;
    UserDto userDto2;
    ItemDto itemDto;
    ItemDto itemDto2;
    RequestDto requestDto;
    BookingDto bookingDto;
    BookingDto bookingDto2;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().name("test").email("test@test.com").build();
        userDto2 = UserDto.builder().name("test2").email("test2@test.com").build();
        itemDto = ItemDto.builder().name("test name").description("test desc").available(true).build();
        userDto = userService.postUser(userDto);
        userDto2 = userService.postUser(userDto2);
        requestDto = RequestDto.builder().description("test desc").build();
        requestDto = requestService.postRequest(userDto.getId(), requestDto);
        itemDto2 = ItemDto.builder().name("test name2").description("test desc2")
                .available(false).requestId(requestDto.getId()).build();
        itemDto = itemService.postItem(userDto.getId(), itemDto);
        bookingService.postBooking(2L,
                BookingDto.builder().itemId(itemDto.getId())
                        .start(LocalDateTime.of(2022,1,1,1,1,1,1))
                        .end(LocalDateTime.now()).build());
        bookingDto = bookingService.patchBooking(userDto.getId(), 1L, true);
        bookingService.postBooking(2L,
                BookingDto.builder().itemId(itemDto.getId())
                        .start(LocalDateTime.of(2024,1,1,1,1,1,1))
                        .end(LocalDateTime.of(2025,1,1,1,1,1,1))
                        .build());
        bookingDto2 = bookingService.patchBooking(userDto.getId(), 2L, true);
        itemDto.setLastBooking(bookingDto);
        itemDto.setNextBooking(bookingDto2);

    }

    @Test
    void postItem() {
        itemDto2 = itemService.postItem(userDto2.getId(), itemDto2);
        assertThat(itemDto2.getId(), equalTo(2L));
        assertThat(itemDto2.getRequestId(), equalTo(1L));
        assertThat(itemDto2.getName(), equalTo("test name2"));
        assertThat(itemDto2.getAvailable(), equalTo(false));
        assertThat(itemDto2.getDescription(), equalTo("test desc2"));
        assertThat(itemDto2.getLastBooking(), nullValue());
        assertThat(itemDto2.getNextBooking(), nullValue());
        assertThat(itemDto2.getComments(), equalTo(new ArrayList<>()));
    }

    @Test
    void patchItemTest() {
        itemDto.setDescription("new description");
        itemDto.setAvailable(false);
        itemDto.setName("new name");
        itemDto = itemService.patchItem(userDto.getId(), itemDto.getId(), itemDto);
        assertThat(itemDto.getName(), equalTo("new name"));
        assertThat(itemDto.getAvailable(), equalTo(false));
        assertThat(itemDto.getDescription(), equalTo("new description"));

        assertThrows(NoSuchElementException.class,
                () -> itemService.patchItem(userDto2.getId(), itemDto.getId(), itemDto));
    }

    @Test
    void getItemTest() {
        itemDto = itemService.getItem(itemDto.getId(), userDto.getId());
        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("test name"));
        assertThat(itemDto.getAvailable(), equalTo(true));
        assertThat(itemDto.getDescription(), equalTo("test desc"));
        assertThat(itemDto.getLastBooking(), notNullValue());
        assertThat(itemDto.getNextBooking(), notNullValue());
        assertThat(itemDto.getComments(), equalTo(new ArrayList<>()));

        itemDto = itemService.getItem(itemDto.getId(), userDto2.getId());
        assertThat(itemDto.getId(), equalTo(1L));
        assertThat(itemDto.getName(), equalTo("test name"));
        assertThat(itemDto.getAvailable(), equalTo(true));
        assertThat(itemDto.getDescription(), equalTo("test desc"));
        assertThat(itemDto.getLastBooking(), nullValue());
        assertThat(itemDto.getNextBooking(), nullValue());
        assertThat(itemDto.getComments(), equalTo(new ArrayList<>()));
    }

    @Test
    void getItemsTest() {
        List<ItemDto> itemsDto = itemService.getItems(userDto.getId(), 0, 10);
        assertThat(itemsDto.get(0).getId(), equalTo(itemDto.getId()));
        assertThat(itemsDto.get(0).getName(), equalTo(itemDto.getName()));
        assertThat(itemsDto.get(0).getAvailable(), equalTo(itemDto.getAvailable()));
        assertThat(itemsDto.get(0).getDescription(), equalTo(itemDto.getDescription()));
        assertThat(itemsDto.get(0).getLastBooking(), notNullValue());
        assertThat(itemsDto.get(0).getNextBooking(), notNullValue());
        assertThat(itemsDto.get(0).getComments(), equalTo(new ArrayList<>()));
        assertThat(itemsDto.size(), equalTo(1));
    }

    @Test
    void searchItemsTest() {
        List<ItemDto> itemsDto = itemService.searchItems("test", 0, 10);
        itemDto.setLastBooking(null);
        itemDto.setNextBooking(null);
        assertThat(itemsDto, equalTo(List.of(itemDto)));
    }

    @Test
    void postCommentTest() {
        CommentDto commentDto = CommentDto.builder().text("test comment").build();
        commentDto = itemService.postComment(itemDto.getId(), userDto2.getId(), commentDto);
        assertThat(commentDto.getId(),equalTo(1L));
        assertThat(commentDto.getItem(), equalTo(itemDto.getId()));
        assertThat(commentDto.getText(), equalTo("test comment"));
        assertThat(commentDto.getAuthor(), equalTo(userDto2.getId()));
        assertThat(commentDto.getAuthorName(), equalTo(userDto2.getName()));
        assertThat(commentDto.getCreated(), notNullValue());

        UserDto newUserDto = userService.postUser(UserDto.builder().name("test3").email("test3@test.com").build());
        CommentDto finalCommentDto = commentDto;
        assertThrows(UnsupportedOperationException.class,
                () -> itemService.postComment(itemDto.getId(), newUserDto.getId(), finalCommentDto));
    }
}