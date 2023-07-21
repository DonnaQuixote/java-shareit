package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {

    RequestDto postRequest(Long userId, RequestDto requestDto);

    List<RequestDto> getRequests(Long userId);

    List<RequestDto> getAllRequests(Long userId,Integer from, Integer size);

    RequestDto getRequest(Long userId, Long requestId);
}