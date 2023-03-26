package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdOrderByStartDesc(@Param("booker_id") Long id);

    List<Booking> findBookingByBookerIdAndStatus(Long userId, Status status);

    @Query(value = "SELECT * FROM bookings b WHERE b.booker_id = id AND b.status like 'CURRENT'",
            nativeQuery = true)
    List<Booking> findCurrentBookings(@Param("booker_id") Long id);

    @Query(value = "SELECT * FROM bookings b WHERE b.item_id " +
            "IN (SELECT i.id FROM items i WHERE i.user_id = :user_id) " +
            "ORDER BY b.start_date DESC ",
            nativeQuery = true)
    List<Booking> findBookingByOwnerId(@Param("user_id") Long userId);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE item_id = :item_id " +
            "AND end_date < CURRENT_TIMESTAMP " +
            "ORDER BY end_date DESC LIMIT 1" ,nativeQuery = true)
    Booking getLastBookingByItemId(@Param("item_id") Long ItemId);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE item_id = :item_id " +
            "AND start_date > CURRENT_TIMESTAMP " +
            "ORDER BY start_date ASC LIMIT 1" ,nativeQuery = true)
    Booking getNextBookingByItemId(@Param("item_id") Long ItemId);

}
