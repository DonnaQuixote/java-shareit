package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class UserJsonTest {
    @Autowired
    JacksonTester<UserDto> jacksonTester;
    static UserDto userDto;
    static User user;

    @BeforeAll
    static void beforeAll() {
        user = new User(1L, "test", "test@test.com");
        userDto = UserMapper.toUserDto(user);
    }

    @Test
    void testJson() throws Exception {
        JsonContent<UserDto> json = jacksonTester.write(userDto);
        assertThat(json).extractingJsonPathNumberValue(
                "$.id").isEqualTo(Math.toIntExact(userDto.getId()));
        assertThat(json).extractingJsonPathStringValue(
                "$.name").isEqualTo(userDto.getName());
        assertThat(json).extractingJsonPathStringValue(
                "$.email").isEqualTo(userDto.getEmail());
    }
}