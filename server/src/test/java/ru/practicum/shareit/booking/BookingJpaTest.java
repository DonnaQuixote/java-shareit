package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class BookingJpaTest {
    @Autowired
    BookingRepository repository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ItemRepository itemRepository;
    static User user;
    static User user2;
    static Booking booking;
    static Item item;

    @BeforeAll
    static void beforeAll() {
        user = User.builder().name("test").email("test@test.com").build();
        user2 = User.builder().name("test2").email("test2@test.com").build();
        item = Item.builder().name("test").description("test desc").available(true).build();
        booking = Booking.builder().start(LocalDateTime.now().plus(10, ChronoUnit.HOURS))
                .end(LocalDateTime.now().plus(12, ChronoUnit.HOURS)).status(BookingStatus.WAITING).build();
    }

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        user2 = userRepository.save(user2);
        item.setOwner(user.getId());
        item = itemRepository.save(item);
        booking.setBooker(user2);
        booking.setItem(item);
        booking = repository.save(booking);
    }

    @Test
    void findPastBookingsTest() {
        List<Booking> bookings =
                repository.findPastBookings(user2.getId(), LocalDateTime.now().plus(13, ChronoUnit.HOURS),
                        Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findFutureBookingsTest() {
        List<Booking> bookings = repository.findFutureBookings(user2.getId(),
                LocalDateTime.now(), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findCurrentBookingsTest() {
        List<Booking> bookings = repository.findCurrentBookings(user2.getId(),
                LocalDateTime.now().plus(11, ChronoUnit.HOURS), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findAllByOwnerTest() {
        List<Booking> bookings = repository.findAllByOwner(user.getId(), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findAllByOwnerCurrentTest() {
        List<Booking> bookings = repository.findAllByOwnerCurrent(user.getId(),
                LocalDateTime.now().plus(11, ChronoUnit.HOURS), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findAllByOwnerPastTest() {
        List<Booking> bookings = repository.findAllByOwnerPast(user.getId(),
                LocalDateTime.now().plus(13, ChronoUnit.HOURS), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findAllByOwnerFutureTest() {
        List<Booking> bookings = repository.findAllByOwnerFuture(user.getId(),
                LocalDateTime.now(), Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @Test
    void findAllByOwnerAndStatusTest() {
        List<Booking> bookings = repository.findAllByOwnerAndStatus(user.getId(),
                BookingStatus.WAITING, Pageable.ofSize(10));
        assertThat(bookings.get(0).getId(), notNullValue());
        assertThat(bookings.get(0).getStart(), equalTo(booking.getStart()));
        assertThat(bookings.get(0).getEnd(), equalTo(booking.getEnd()));
        assertThat(bookings.get(0).getItem(), equalTo(booking.getItem()));
        assertThat(bookings.get(0).getBooker(), equalTo(booking.getBooker()));
        assertThat(bookings.get(0).getStatus(), equalTo(booking.getStatus()));
    }

    @AfterEach
    void afterEach() {
        repository.delete(booking);
        itemRepository.delete(item);
        userRepository.delete(user);
        userRepository.delete(user2);
    }
}