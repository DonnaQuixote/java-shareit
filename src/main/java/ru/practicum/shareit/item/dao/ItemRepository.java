package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select o from Item o where (lower(o.name) like lower(concat('%',?1,'%')) "
            + "or lower(o.description) like lower(concat('%',?1,'%'))) and o.available=true")
    List<Item> findByNameOrDescription(String text);

    List<Item> findAllByOwnerOrderById(Long owner);
}