package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker.id=?1 and b.end<?2 order by b.start desc")
    List<Booking> findPastBookings(Long userId, LocalDateTime currentTime, Pageable pageable);

    @Query("select b from Booking b where b.booker.id=?1 and b.start>?2 order by b.start desc")
    List<Booking> findFutureBookings(Long userId, LocalDateTime currentTime, Pageable pageable);

    @Query("select b from Booking b where b.booker.id=?1 and b.start<?2 and b.end>?2")
    List<Booking> findCurrentBookings(Long userId, LocalDateTime currentTime, Pageable pageable);

    List<Booking> getBookingsByBookerAndStatus(User user, BookingStatus status, Pageable page);

    List<Booking> getBookingsByBookerOrderByStartDesc(User user, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item.id where i.owner=?1 order by b.start desc")
    List<Booking> findAllByOwner(Long userId, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item.id "
            + "where i.owner=?1 and b.end>?2 and b.start<?2 order by b.start desc")
    List<Booking> findAllByOwnerCurrent(Long userId, LocalDateTime currentTime, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item.id where i.owner=?1 and b.end<?2 order by b.start desc")
    List<Booking> findAllByOwnerPast(Long userId, LocalDateTime currentTime, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item.id where i.owner=?1 and b.end>?2 order by b.start desc")
    List<Booking> findAllByOwnerFuture(Long userId, LocalDateTime currentTime, Pageable pageable);

    @Query("select b from Booking b join Item i on i.id=b.item.id where i.owner=?1 and b.status=?2 order by b.start desc")
    List<Booking> findAllByOwnerAndStatus(Long userId, BookingStatus status, Pageable pageable);

    Optional<Booking> findFirstByItemAndBookerAndEndBeforeAndStatus(Item item, User user,
                                                                    LocalDateTime currentTime, BookingStatus status);
}