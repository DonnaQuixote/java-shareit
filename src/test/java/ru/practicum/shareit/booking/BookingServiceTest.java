package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceTest {
    @Autowired
    BookingService bookingService;
    @Autowired
    ItemService itemService;
    @Autowired
    UserService userService;
    UserDto userDto;
    UserDto userDto2;
    ItemDto itemDto;
    ItemDto itemDto2;
    BookingDto bookingDto;
    BookingDto bookingDto2;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().name("test").email("test@test.com").build();
        userDto = userService.postUser(userDto);
        userDto2 = UserDto.builder().name("test2").email("test2@test.com").build();
        userDto2 = userService.postUser(userDto2);
        itemDto = ItemDto.builder().name("test name").description("test desc").available(true).build();
        itemDto = itemService.postItem(userDto.getId(), itemDto);
        bookingDto = BookingDto.builder().itemId(itemDto.getId())
                .start(LocalDateTime.of(2023, 10, 10, 10, 10, 10, 10))
                .end(LocalDateTime.of(2023, 12, 12, 12, 12, 12, 12))
                .build();
        bookingDto = bookingService.postBooking(userDto2.getId(), bookingDto);
        itemDto2 = ItemDto.builder().name("test name2").description("test desc2").available(true).build();
        itemDto2 = itemService.postItem(userDto2.getId(), itemDto2);
        bookingDto2 = BookingDto.builder().itemId(itemDto2.getId())
                .start(LocalDateTime.of(2022, 10, 10, 10, 10, 10, 10))
                .end(LocalDateTime.of(2022, 12, 12, 12, 12, 12, 12))
                .build();
        bookingDto2 = bookingService.postBooking(userDto.getId(), bookingDto2);
    }

    @Test
    void postBooking() {
        assertThat(bookingDto.getId(), equalTo(1L));
        assertThat(bookingDto.getStart(),
                equalTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10, 10)));
        assertThat(bookingDto.getEnd(),
                equalTo(LocalDateTime.of(2023, 12, 12, 12, 12, 12, 12)));
        assertThat(bookingDto.getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingDto.getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingDto.getStatus(), equalTo(BookingStatus.WAITING));

        assertThrows(NoSuchElementException.class,
                () -> bookingService.postBooking(userDto.getId(), BookingDto.builder()
                        .itemId(itemDto.getId()).start(LocalDateTime.MIN).end(LocalDateTime.MAX).build()));

        assertThrows(DateTimeException.class, () -> bookingService.postBooking(userDto2.getId(), BookingDto.builder()
                .itemId(itemDto.getId()).start(LocalDateTime.MAX).end(LocalDateTime.MIN).build()));

        assertThrows(NoSuchElementException.class, () -> bookingService.postBooking(100L, BookingDto.builder()
                .itemId(itemDto.getId()).start(LocalDateTime.MIN).end(LocalDateTime.MAX).build()));
    }

    @Test
    void patchBookingTest() {
        assertThrows(NoSuchElementException.class,
                () -> bookingService.patchBooking(userDto2.getId(), bookingDto.getId(), true));

        bookingDto = bookingService.patchBooking(userDto.getId(), bookingDto.getId(), true);
        assertThat(bookingDto.getId(), equalTo(1L));
        assertThat(bookingDto.getStart(),
                equalTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10)));
        assertThat(bookingDto.getEnd(),
                equalTo(LocalDateTime.of(2023, 12, 12, 12, 12, 12)));
        assertThat(bookingDto.getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingDto.getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingDto.getStatus(), equalTo(BookingStatus.APPROVED));

        bookingDto2 = bookingService.patchBooking(userDto2.getId(), bookingDto2.getId(), false);
        assertThat(bookingDto2.getId(), equalTo(2L));
        assertThat(bookingDto2.getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingDto2.getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingDto2.getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingDto2.getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingDto2.getStatus(), equalTo(BookingStatus.REJECTED));

        assertThrows(UnsupportedOperationException.class,
                () -> bookingService.patchBooking(userDto2.getId(), bookingDto2.getId(), true));
    }

    @Test
    void getBookingTest() {
        BookingDto booking = bookingService.getBooking(userDto.getId(), bookingDto.getId());
        assertThat(booking.getId(), equalTo(bookingDto.getId()));
        assertThat(booking.getStart(),
                equalTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10)));
        assertThat(booking.getEnd(),
                equalTo(LocalDateTime.of(2023, 12, 12, 12, 12, 12)));
        assertThat(booking.getItemId(), equalTo(itemDto.getId()));
        assertThat(booking.getBookerId(), equalTo(userDto2.getId()));
        assertThat(booking.getStatus(), equalTo(BookingStatus.WAITING));

        assertThrows(NoSuchElementException.class,
                () -> bookingService.getBooking(10L, bookingDto.getId()));
    }

    @Test
    void getBookingsTest() {
        List<BookingDto> bookingsDto = bookingService.getBookings(userDto.getId(), "ALL", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookings(userDto.getId(), "WAITING", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookings(userDto.getId(), "PAST", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingDto = bookingService.patchBooking(userDto2.getId(), bookingDto2.getId(), false);
        bookingsDto = bookingService.getBookings(userDto.getId(), "REJECTED", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.REJECTED));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookings(userDto2.getId(), "FUTURE", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(1L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2023, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingService.postBooking(userDto2.getId(), BookingDto.builder().itemId(itemDto.getId())
                .start(LocalDateTime.now().minus(30, ChronoUnit.HOURS))
                .end(LocalDateTime.now().plus(30, ChronoUnit.HOURS)).build());

        bookingsDto = bookingService.getBookings(userDto2.getId(), "CURRENT", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(3L));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookings(userDto2.getId(), "TEST", 0, 10));
    }

    @Test
    void getBookingsByOwnerTest() {
        List<BookingDto> bookingsDto = bookingService.getBookingsByOwner(userDto2.getId(), "ALL", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookingsByOwner(userDto2.getId(), "WAITING", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookingsByOwner(userDto2.getId(), "PAST", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingDto = bookingService.patchBooking(userDto2.getId(), bookingDto2.getId(), false);
        bookingsDto = bookingService.getBookingsByOwner(userDto2.getId(), "REJECTED", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(2L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2022, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2022, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto2.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.REJECTED));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingsDto = bookingService.getBookingsByOwner(userDto.getId(), "FUTURE", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(1L));
        assertThat(bookingsDto.get(0).getStart(),
                equalTo(LocalDateTime.of(2023, 10, 10, 10, 10, 10)));
        assertThat(bookingsDto.get(0).getEnd(),
                equalTo(LocalDateTime.of(2023, 12, 12, 12, 12, 12)));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        bookingService.postBooking(userDto2.getId(), BookingDto.builder().itemId(itemDto.getId())
                .start(LocalDateTime.now().minus(30, ChronoUnit.HOURS))
                .end(LocalDateTime.now().plus(30, ChronoUnit.HOURS)).build());

        bookingsDto = bookingService.getBookingsByOwner(userDto.getId(), "CURRENT", 0, 10);
        assertThat(bookingsDto.get(0).getId(), equalTo(3L));
        assertThat(bookingsDto.get(0).getItemId(), equalTo(itemDto.getId()));
        assertThat(bookingsDto.get(0).getBookerId(), equalTo(userDto2.getId()));
        assertThat(bookingsDto.get(0).getStatus(), equalTo(BookingStatus.WAITING));
        assertThat(bookingsDto.size(), equalTo(1));

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.getBookingsByOwner(userDto2.getId(), "TEST", 0, 10));
    }
}