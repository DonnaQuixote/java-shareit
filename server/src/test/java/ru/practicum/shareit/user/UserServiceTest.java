package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    UserService userService;
    UserDto userDto;
    UserDto userDto2;

    @BeforeEach
    void beforeEach() {
        userDto = UserDto.builder().name("test").email("test@test.com").build();
        userDto2 = UserDto.builder().name("test2").email("test2@test.com").build();
        userDto = userService.postUser(userDto);
        userDto2 = userService.postUser(userDto2);
    }

    @Test
    void postUserTest() {
        UserDto newUserDto = UserDto.builder().name("test3").email("test3@test.com").build();
        newUserDto = userService.postUser(newUserDto);
        assertThat(newUserDto.getId(), equalTo(3L));
        assertThat(newUserDto.getName(), equalTo("test3"));
        assertThat(newUserDto.getEmail(), equalTo("test3@test.com"));
    }

    @Test
    void patchUserTest() {
        UserDto newUserDto = UserDto.builder().name("testNEW").email("testNEW@test.com").build();
        newUserDto = userService.patchUser(1L, newUserDto);
        assertThat(newUserDto.getId(), equalTo(1L));
        assertThat(newUserDto.getName(), equalTo("testNEW"));
        assertThat(newUserDto.getEmail(), equalTo("testNEW@test.com"));
    }

    @Test
    void getUserTest() {
        UserDto user = userService.getUser(2L);
        assertThat(user.getId(), equalTo(userDto2.getId()));
        assertThat(user.getName(), equalTo(userDto2.getName()));
        assertThat(user.getEmail(), equalTo(userDto2.getEmail()));

        assertThrows(NoSuchElementException.class,
                () -> userService.getUser(100L));
    }

    @Test
    void getUsersTest() {
        List<UserDto> usersDto = userService.getUsers();
        assertThat(usersDto, equalTo(List.of(userDto, userDto2)));
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);
        assertThrows(NoSuchElementException.class,
                () -> userService.getUser(1L));
    }
}