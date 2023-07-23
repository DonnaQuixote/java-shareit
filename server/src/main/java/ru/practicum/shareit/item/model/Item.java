package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.Comment;
import ru.practicum.shareit.request.model.Request;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    @ManyToOne
    @JoinColumn(name = "request")
    @JsonBackReference
    private Request request;
    @OneToMany(mappedBy = "item")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Comment> comments = new ArrayList<>();
    @OneToMany(mappedBy = "item")
    @LazyCollection(LazyCollectionOption.FALSE)
    @JsonManagedReference
    private List<Booking> bookings = new ArrayList<>();

    public Item(Long id, String name, String description, Boolean available, Long owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.available = available;
        this.owner = owner;
    }
}