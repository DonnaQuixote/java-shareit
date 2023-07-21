package ru.practicum.shareit.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataJpaTest
public class ItemJpaTest {
    @Autowired
    ItemRepository repository;
    @Autowired
    UserRepository userRepository;
    static User user;
    static Item item;

    @BeforeAll
    static void beforeAll() {
        user = User.builder().name("name").email("test@test.com").build();
        item = Item.builder().name("test").description("test desc").available(true).build();
    }

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(user);
        item.setOwner(user.getId());
        item = repository.save(item);
    }

    @Test
    void findByNameOrDescriptionTest() {
        List<Item> items = repository.findByNameOrDescription("test", Pageable.ofSize(10));
        assertThat(items.get(0).getId(), notNullValue());
        assertThat(items.get(0).getDescription(), equalTo(item.getDescription()));
        assertThat(items.get(0).getName(), equalTo(item.getName()));
        assertThat(items.get(0).getRequest(), equalTo(item.getRequest()));
        assertThat(items.get(0).getOwner(), equalTo(item.getOwner()));
    }

    @AfterEach
    void afterEach() {
        userRepository.delete(user);
        repository.delete(item);
    }
}