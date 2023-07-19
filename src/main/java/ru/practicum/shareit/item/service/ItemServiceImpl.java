package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto postItem(Long userId, ItemDto itemDto) {
        if (userRepository.findById(userId).isPresent()) {
            return ItemMapper.toItemDto(repository.save(ItemMapper.toItem(userId, itemDto)));
        } else throw new NoSuchElementException("Пользователь не найден");
    }

    @Override
    public ItemDto patchItem(Long userId, Long itemId, ItemDto itemDto) {
        if (repository.findById(itemId).isPresent() &&
                Objects.equals(userId, repository.findById(itemId).get().getOwner())) {
            Item oldItem = repository.findById(itemId).orElseThrow();
            if (itemDto.getName() != null) oldItem.setName(itemDto.getName());
            if (itemDto.getDescription() != null) oldItem.setDescription(itemDto.getDescription());
            if (itemDto.getAvailable() != null) oldItem.setAvailable(itemDto.getAvailable());
            return ItemMapper.toItemDto(repository.save(oldItem));
        } else throw new NoSuchElementException(String.format("Вы не являетесь владельцем вещи c id %d", itemId));
    }

    @Override
    public ItemDto getItem(Long id, Long userId) {
        Optional<Item> item = repository.findById(id);
        if (item.isPresent()) {
            if (userId.equals(item.get().getOwner())) return getItemWithBookings(item.get());
            else return ItemMapper.toItemDto(item.get());
        } else throw new NoSuchElementException("Вещь не найдена");
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> foundItems = repository.findAllByOwnerOrderById(userId);
        List<ItemDto> foundItemsDto = new ArrayList<>();
        foundItems.forEach(item -> foundItemsDto.add(getItemWithBookings(item)));
        return foundItemsDto;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (!text.isBlank()) {
            List<Item> items = repository.findByNameOrDescription(text);
            List<ItemDto> itemsDTO = new ArrayList<>();
            items.forEach(item -> itemsDTO.add(ItemMapper.toItemDto(item)));
            return itemsDTO;
        } else return new ArrayList<>();
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
        Optional<Booking> bookingOptionalBefore = bookingRepository
                .findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                        item, LocalDateTime.now(), BookingStatus.APPROVED);
        Optional<Booking> bookingOptionalAfter = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                item, LocalDateTime.now(), BookingStatus.APPROVED);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        bookingOptionalBefore.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toBookingDto(booking)));
        bookingOptionalAfter.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toBookingDto(booking)));
        return itemDto;
    }
}