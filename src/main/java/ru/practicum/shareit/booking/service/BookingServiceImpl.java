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

import javax.validation.ValidationException;
import java.security.InvalidParameterException;
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
        if (userRepository.findById(userId).isEmpty()) throw new InvalidParameterException("Пользователь не найден");
        if (itemRepository.findById(bookingDto.getItemId()).orElseThrow().getOwner().equals(userId))
            throw new NoSuchElementException("Вы не можете арендовать собственную вещь");
        if (bookingDto.getStart().isBefore(bookingDto.getEnd())) {
            bookingDto.setBookerId(userId);
            Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow();
            if (!item.getAvailable()) throw new ValidationException("Вещь недоступна");
            bookingDto.setStatus(BookingStatus.WAITING);
            BookingDto dto = BookingMapper.toBookingDto(repository.save(BookingMapper.toBooking(bookingDto)));
            return fillDto(dto);
        } else throw new DateTimeException("Некорректное время");
    }

    @Override
    public BookingDto patchBooking(Long userId, Long bookingId, Boolean approved) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        if (booking.getStatus().equals(BookingStatus.APPROVED) || booking.getStatus().equals(BookingStatus.REJECTED))
            throw new ValidationException("Невозможно повторно изменить статус");
        if (itemRepository.findById(booking.getItem()).orElseThrow().getOwner().equals(userId)) {
            if (approved) booking.setStatus(BookingStatus.APPROVED);
            else booking.setStatus(BookingStatus.REJECTED);
            repository.save(booking);
            return fillDto(BookingMapper.toBookingDto(booking));
        } else throw new NoSuchElementException("Вы не являетесь владельцем вещи");
    }

    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = repository.findById(bookingId).orElseThrow();
        if (booking.getBooker().equals(userId) ||
                itemRepository.findById(booking.getItem()).orElseThrow().getOwner().equals(userId)) {
            return fillDto(BookingMapper.toBookingDto(booking));
        } else throw new NoSuchElementException("Вы не являетесь владельцем/арендатором вещи");
    }

    @Override
    public List<BookingDto> getBookings(Long userId, String state) {
        if (userRepository.findById(userId).isPresent()) {
            List<BookingDto> bookingsDto = new ArrayList<>();
            List<Booking> bookings = new ArrayList<>();
            switch (BookingState.valueOf(state)) {
                case ALL:
                    bookings = repository.getBookingsByBookerOrderByStartDesc(userId);
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
                    bookings = repository.getBookingsByBookerAndStatus(userId, BookingStatus.WAITING);
                    break;
                case REJECTED:
                    bookings = repository.getBookingsByBookerAndStatus(userId, BookingStatus.REJECTED);
                    break;
            }
            bookings.forEach(booking -> bookingsDto.add(fillDto(BookingMapper.toBookingDto(booking))));
            return bookingsDto;
        } else throw new InvalidParameterException();
    }

    @Override
    public List<BookingDto> getBookingsByOwner(Long userId, String state) {
        if (userRepository.findById(userId).isPresent()) {
            List<BookingDto> bookingsDto = new ArrayList<>();
            List<Booking> bookings = new ArrayList<>();
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
            }
            bookings.forEach(booking -> bookingsDto.add(fillDto(BookingMapper.toBookingDto(booking))));
            return bookingsDto;
        } else throw new InvalidParameterException();
    }

    private BookingDto fillDto(BookingDto bookingDto) {
        bookingDto.setItem(itemRepository.findById(bookingDto.getItemId()).orElseThrow());
        bookingDto.setBooker(userRepository.findById(bookingDto.getBookerId()).orElseThrow());
        return bookingDto;
    }
}