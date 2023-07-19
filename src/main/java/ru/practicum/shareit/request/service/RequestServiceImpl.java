package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dao.RequestRepository;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository repository;
    private final UserRepository userRepository;

    @Override
    public RequestDto postRequest(Long userId, RequestDto requestDto) {
        User user = userRepository.findById(userId).orElseThrow();
        requestDto.setRequester(UserMapper.toUserDto(user));
        requestDto.setCreated(LocalDateTime.now());
        return RequestMapper.toItemRequestDto(repository.save(RequestMapper.toItemRequest(requestDto)));
    }

    @Override
    public List<RequestDto> getRequests(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<Request> requests = repository.findAllByRequesterOrderByCreatedAsc(user);
        return requests.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<RequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow();
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Request> requests = repository.findAllByRequesterNotOrderByCreatedAsc(user, page);
        return requests.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public RequestDto getRequest(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow();
        return RequestMapper.toItemRequestDto(repository.findById(requestId).orElseThrow());
    }
}