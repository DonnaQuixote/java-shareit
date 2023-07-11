package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.dto.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("select b from Booking b where b.booker=?1 and b.end<?2 order by b.start desc")
    List<Booking> findPastBookings(Long userId, LocalDateTime dt);

    @Query("select b from Booking b where b.booker=?1 and b.start>?2 order by b.start desc")
    List<Booking> findFutureBookings(Long userId, LocalDateTime dt);

    @Query("select b from Booking b where b.booker=?1 and b.start<?2 and b.end>?2")
    List<Booking> findCurrentBookings(Long userId, LocalDateTime dt);

    List<Booking> getBookingsByBookerAndStatus(Long userId, BookingStatus status);

    List<Booking> getBookingsByBookerOrderByStartDesc(Long userId);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 order by b.start desc")
    List<Booking> findAllByOwner(Long userId);

    @Query("select b from Booking b join Item i on i.id=b.item "
            + "where i.owner=?1 and b.end>?2 and b.start<?2 order by b.start desc")
    List<Booking> findAllByOwnerCurrent(Long userId, LocalDateTime dt);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.end<?2 order by b.start desc")
    List<Booking> findAllByOwnerPast(Long userId, LocalDateTime dt);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.end>?2 order by b.start desc")
    List<Booking> findAllByOwnerFuture(Long userId, LocalDateTime dt);

    @Query("select b from Booking b join Item i on i.id=b.item where i.owner=?1 and b.status=?2 order by b.start desc")
    List<Booking> findAllByOwnerAndStatus(Long userId, BookingStatus status);

    Optional<Booking> findFirstByItemAndStartBeforeAndStatusOrderByStartDesc(
            Long itemId, LocalDateTime dt, BookingStatus status);

    Optional<Booking> findFirstByItemAndStartAfterAndStatusOrderByStartAsc(
            Long itemId, LocalDateTime dt, BookingStatus status);

    Optional<Booking> findFirstByItemAndBookerAndEndBeforeAndStatus(Long itemId, Long userId,
                                                                    LocalDateTime dt, BookingStatus status);
}