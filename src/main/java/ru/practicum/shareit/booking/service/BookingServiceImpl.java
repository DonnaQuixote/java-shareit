package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository repository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public BookingDto postBooking(Long userId, BookingDto bookingDto) {
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow();
        if (userId.equals(item.getOwner()))
            throw new NoSuchElementException("Вы не можете арендовать собственную вещь");
        if (bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            if (!item.getAvailable()) throw new UnsupportedOperationException("Вещь недоступна");
            bookingDto.setBooker(userRepository.findById(userId).orElseThrow(
                    () -> new NoSuchElementException("Пользователь не найден")));
            bookingDto.setItem(item);
            bookingDto.setStatus(BookingStatus.WAITING);
            return BookingMapper.toBookingDto(repository.save(BookingMapper.toBooking(bookingDto)));
        } else throw new DateTimeException("Некорректное время");
    }

    @Override
    public BookingDto patchBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        if (BookingStatus.APPROVED.equals(booking.getStatus()) || BookingStatus.REJECTED.equals(booking.getStatus()))
            throw new UnsupportedOperationException("Невозможно повторно изменить статус");
        if (userId.equals(booking.getItem().getOwner())) {
            if (approved) booking.setStatus(BookingStatus.APPROVED);
            else booking.setStatus(BookingStatus.REJECTED);
            repository.save(booking);
            return BookingMapper.toBookingDto(booking);
        } else throw new NoSuchElementException("Вы не являетесь владельцем вещи");
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        if (userId.equals(booking.getBooker().getId()) ||
                userId.equals(booking.getItem().getOwner())) {
            return BookingMapper.toBookingDto(booking);
        } else throw new NoSuchElementException("Вы не являетесь владельцем/арендатором вещи");
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state) {
            User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
            List<BookingDto> bookingsDto = new ArrayList<>();
            List<Booking> bookings;
            switch (BookingState.valueOf(state)) {
                case ALL:
                    bookings = repository.getBookingsByBookerOrderByStartDesc(user);
                    break;
                case CURRENT:
                    bookings = repository.findCurrentBookings(userId, LocalDateTime.now());
                    break;
                case PAST:
                    bookings = repository.findPastBookings(userId, LocalDateTime.now());
                    break;
                case FUTURE:
                    bookings = repository.findFutureBookings(userId, LocalDateTime.now());
                    break;
                case WAITING:
                    bookings = repository.getBookingsByBookerAndStatus(user, BookingStatus.WAITING);
                    break;
                case REJECTED:
                    bookings = repository.getBookingsByBookerAndStatus(user, BookingStatus.REJECTED);
                    break;
                default: throw new IllegalArgumentException();
            }
            bookings.forEach(booking -> bookingsDto.add(BookingMapper.toBookingDto(booking)));
            return bookingsDto;
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, String state) {
        if (userRepository.findById(userId).isPresent()) {
            List<BookingDto> bookingsDto = new ArrayList<>();
            List<Booking> bookings;
            switch (BookingState.valueOf(state)) {
                case ALL:
                    bookings = repository.findAllByOwner(userId);
                    break;
                case CURRENT:
                    bookings = repository.findAllByOwnerCurrent(userId, LocalDateTime.now());
                    break;
                case PAST:
                    bookings = repository.findAllByOwnerPast(userId, LocalDateTime.now());
                    break;
                case FUTURE:
                    bookings = repository.findAllByOwnerFuture(userId, LocalDateTime.now());
                    break;
                case WAITING:
                    bookings = repository.findAllByOwnerAndStatus(userId, BookingStatus.WAITING);
                    break;
                case REJECTED:
                    bookings = repository.findAllByOwnerAndStatus(userId, BookingStatus.REJECTED);
                    break;
                default: throw new IllegalArgumentException();
            }
            bookings.forEach(booking -> bookingsDto.add(BookingMapper.toBookingDto(booking)));
            return bookingsDto;
        } else throw new NoSuchElementException();
    }
}