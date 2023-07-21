package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select o from Item o where (lower(o.name) like lower(concat('%',?1,'%')) "
            + "or lower(o.description) like lower(concat('%',?1,'%'))) and o.available=true")
    List<Item> findByNameOrDescription(String text, Pageable pageable);

    List<Item> findAllByOwnerOrderById(Long owner, Pageable pageable);
}