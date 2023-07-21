package ru.practicum.shareit.booking;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@JsonTest
public class BookingJsonTest {
    @Autowired
    JacksonTester<BookingDto> jacksonTester;
    static BookingDto bookingDto;
    static Booking booking;

    @BeforeAll
    static void beforeAll() {
        booking = new Booking(1L,
                LocalDateTime.of(2024,1,1,1,1,1,1),
                LocalDateTime.MAX,
                new Item(1L, "test", "test", true, 1L),
                new User(1L, "test", "test@test.com"), BookingStatus.APPROVED);
        bookingDto = BookingMapper.toBookingDto(booking);
    }

    @Test
    void testJson() throws IOException {
        JsonContent<BookingDto> json = jacksonTester.write(bookingDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(bookingDto.getId()));
        assertThat(json).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(Math.toIntExact(bookingDto.getItemId()));
        assertThat(json).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(Math.toIntExact(bookingDto.getBookerId()));
        assertThat(json).extractingJsonPathStringValue("$.status")
                .isEqualTo(bookingDto.getStatus().toString());
    }

    @Test
    void testToString() {
        MatcherAssert.assertThat(booking.toString(), equalTo("Booking{id=1," +
                " start=2024-01-01T01:01:01.000000001, end=+999999999-12-31T23:59:59.999999999," +
                " booker=User(id=1, name=test, email=test@test.com), status=APPROVED}"));
    }
}