package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class ItemJsonTest {
    @Autowired
    JacksonTester<ItemDto> jacksonTester;
    static ItemDto itemDto;
    static Item item;

    @BeforeAll
    static void beforeAll() {
        item = new Item(1L, "test name", "test desc", true, 1L);
        itemDto = ItemMapper.toItemDto(item);
    }

    @Test
    void testJson() throws Exception {
        JsonContent<ItemDto> json = jacksonTester.write(itemDto);
        assertThat(json).extractingJsonPathNumberValue("$.id").isEqualTo(Math.toIntExact(itemDto.getId()));
        assertThat(json).extractingJsonPathStringValue("$.name")
                .isEqualTo(itemDto.getName());
        assertThat(json).extractingJsonPathStringValue(
                "$.description").isEqualTo(itemDto.getDescription());
        assertThat(json).extractingJsonPathBooleanValue("$.available")
                .isEqualTo(itemDto.getAvailable());
    }
}