package ru.practicum.shareit.request.dto;

import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dto.UserMapper;

public class RequestMapper {

    public static RequestDto toItemRequestDto(Request request) {
        return new RequestDto(request.getId(),
                request.getDescription(),
                request.getCreated(),
                UserMapper.toUserDto(request.getRequester()),
                ItemMapper.toItemDto(request.getItems()));
    }

    public static Request toItemRequest(RequestDto requestDto) {
        return new Request(requestDto.getId(),
                requestDto.getDescription(),
                requestDto.getCreated(),
                UserMapper.toUser(requestDto.getRequester()));
    }
}