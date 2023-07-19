package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final RequestRepository requestRepository;

    @Override
    public ItemDto postItem(Long userId, ItemDto itemDto) {
        userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        itemDto.setOwner(userId);
        Item item = ItemMapper.toItem(itemDto);
        if (itemDto.getRequestId() != null)
            item.setRequest(requestRepository.findById(itemDto.getRequestId()).orElseThrow());
        return ItemMapper.toItemDto(repository.save(item));
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, ItemDto itemDto) {
        Item oldItem = repository.findById(itemId).orElseThrow(NoSuchElementException::new);
        if (!Objects.equals(userId, oldItem.getOwner()))
            throw new NoSuchElementException(String.format("Вы не являетесь владельцем вещи c id %d", itemId));
        if (itemDto.getName() != null) oldItem.setName(itemDto.getName());
        if (itemDto.getDescription() != null) oldItem.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) oldItem.setAvailable(itemDto.getAvailable());
        return ItemMapper.toItemDto(repository.save(oldItem));
    }

    @Override
    public ItemDto getItem(Long id, Long userId) {
        Item item = repository.findById(id).orElseThrow(NoSuchElementException::new);
        if (userId.equals(item.getOwner())) return getItemWithBookings(item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItems(Long userId, Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Item> foundItems = repository.findAllByOwnerOrderById(userId, page);
        return foundItems.stream().map(this::getItemWithBookings).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text, Integer from, Integer size) {
        if (text.isBlank()) return new ArrayList<>();
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Item> items = repository.findByNameOrDescription(text, page);
        return items.stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @Override
    public CommentDto postComment(Long itemId, Long userId, CommentDto commentDto) {
        Item item = repository.findById(itemId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        bookingRepository.findFirstByItemAndBookerAndEndBeforeAndStatus(
                item, user, LocalDateTime.now(), BookingStatus.APPROVED).orElseThrow(() -> {
            throw new UnsupportedOperationException("Вы не брали данную вещь в аренду, либо срок аренды не окончен");
        });
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);
        comment.setCreated(LocalDateTime.now());
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private ItemDto getItemWithBookings(Item item) {
        List<Booking> bookings = item.getBookings();
        List<Booking> lastBookings = new ArrayList<>();
        List<Booking> nextBookings = new ArrayList<>();
        for (Booking booking : bookings) {
            if (BookingStatus.APPROVED.equals(booking.getStatus())) {
                if (LocalDateTime.now().isAfter(booking.getStart())) lastBookings.add(booking);
                else if (LocalDateTime.now().isBefore(booking.getStart())) nextBookings.add(booking);
            }
        }
        lastBookings.sort(Comparator.comparing(Booking::getStart).reversed());
        nextBookings.sort(Comparator.comparing(Booking::getStart));
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (lastBookings.size() != 0) itemDto.setLastBooking(BookingMapper.toBookingDto(lastBookings.get(0)));
        if (nextBookings.size() != 0) itemDto.setNextBooking(BookingMapper.toBookingDto(nextBookings.get(0)));
        return itemDto;
    }
}