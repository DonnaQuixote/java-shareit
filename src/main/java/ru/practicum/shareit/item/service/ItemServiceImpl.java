package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.CommentDto;
import ru.practicum.shareit.comment.CommentMapper;
import ru.practicum.shareit.comment.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;

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
            return fillDto(repository.save(oldItem));
        } else throw new NoSuchElementException(String.format("Вы не являетесь владельцем вещи c id %d", itemId));
    }

    @Override
    public ItemDto getItem(Long id, Long userId) {
        Optional<Item> item = repository.findById(id);
        if (item.isPresent()) {
            if (item.get().getOwner().equals(userId)) return getItemWithBookings(fillDto(item.get()));
            else return fillDto(item.get());
        } else throw new NoSuchElementException("Вещь не найдена");
    }

    @Override
    public List<ItemDto> getItems(Long userId) {
        List<Item> foundItems = repository.findAllByOwnerOrderById(userId);
        List<ItemDto> foundItemsDto = new ArrayList<>();
        foundItems.forEach(item -> foundItemsDto.add(getItemWithBookings(fillDto(item))));
        return foundItemsDto;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (!text.isBlank()) {
            List<Item> items = repository.findByNameOrDescription(text);
            List<ItemDto> itemsDTO = new ArrayList<>();
            items.forEach(item -> itemsDTO.add(fillDto(item)));
            return itemsDTO;
        } else return new ArrayList<>();
    }

    @Override
    public CommentDto postComment(Long itemId, Long userId, CommentDto comment) {
        bookingRepository.findFirstByItemAndBookerAndEndBeforeAndStatus(
                itemId, userId, LocalDateTime.now(), BookingStatus.APPROVED).orElseThrow(() -> {
            throw new UnsupportedOperationException("Вы не брали данную вещь в аренду, либо срок аренды не окончен");
        });
        comment.setItem(itemId);
        comment.setAuthor(userId);
        comment.setCreated(LocalDateTime.now());
        comment = CommentMapper.toCommentDto(commentRepository.save(CommentMapper.toComment(comment)));
        comment.setAuthorName(userRepository.findById(comment.getAuthor()).orElseThrow().getName());
        return comment;
    }

    private ItemDto getItemWithBookings(ItemDto itemDto) {
        Optional<Booking> bookingOptionalBefore = bookingRepository
                .findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
                        itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        Optional<Booking> bookingOptionalAfter = bookingRepository.findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
                itemDto.getId(), LocalDateTime.now(), BookingStatus.APPROVED);
        bookingOptionalBefore.ifPresent(booking -> itemDto.setLastBooking(BookingMapper.toBookingDto(booking)));
        bookingOptionalAfter.ifPresent(booking -> itemDto.setNextBooking(BookingMapper.toBookingDto(booking)));
        return itemDto;
    }

    private ItemDto fillDto(Item item) {
        List<CommentDto> commentsDto = new ArrayList<>();
        ItemDto itemDto = ItemMapper.toItemDto(item);
        commentRepository.findAllByItem(item.getId())
                .forEach(comment -> commentsDto.add(CommentMapper.toCommentDto(comment)));
        commentsDto.forEach(comment ->
                comment.setAuthorName(userRepository.findById(comment.getAuthor()).orElseThrow().getName()));
        itemDto.setComments(commentsDto);
        return itemDto;
    }
}