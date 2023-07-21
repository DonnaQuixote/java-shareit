package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
            bookingDto.setItem(ItemMapper.toItemDto(item));
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
            return BookingMapper.toBookingDto(repository.save(booking));
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
    public List<BookingDto> getBookings(Long userId, String state, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Booking> bookings;
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = repository.getBookingsByBookerOrderByStartDesc(user, page);
                break;
            case CURRENT:
                bookings = repository.findCurrentBookings(userId, LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = repository.findPastBookings(userId, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = repository.findFutureBookings(userId, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = repository.getBookingsByBookerAndStatus(user, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = repository.getBookingsByBookerAndStatus(user, BookingStatus.REJECTED, page);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, String state, Integer from, Integer size) {
        if (userRepository.findById(userId).isEmpty()) throw new NoSuchElementException();
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Booking> bookings;
        switch (BookingState.valueOf(state)) {
            case ALL:
                bookings = repository.findAllByOwner(userId, page);
                break;
            case CURRENT:
                bookings = repository.findAllByOwnerCurrent(userId, LocalDateTime.now(), page);
                break;
            case PAST:
                bookings = repository.findAllByOwnerPast(userId, LocalDateTime.now(), page);
                break;
            case FUTURE:
                bookings = repository.findAllByOwnerFuture(userId, LocalDateTime.now(), page);
                break;
            case WAITING:
                bookings = repository.findAllByOwnerAndStatus(userId, BookingStatus.WAITING, page);
                break;
            case REJECTED:
                bookings = repository.findAllByOwnerAndStatus(userId, BookingStatus.REJECTED, page);
                break;
            default:
                throw new IllegalArgumentException();
        }
        return bookings.stream().map(BookingMapper::toBookingDto).collect(Collectors.toList());
    }
}