package ru.practicum.shareit.booking.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByBookerIdOrderByStartDesc(@Param("booker_id") Long id);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE b.booker_id = :booker_id " +
            "ORDER BY b.start_date DESC LIMIT :size OFFSET :from", nativeQuery = true)
    List<Booking> findBookingsWithPagination(@Param("booker_id") Long id,
                                             @Param("from") int from,
                                             @Param("size") int size);

    @Query(value = "SELECT * FROM bookings b WHERE b.item_id " +
            "IN (SELECT i.id FROM items i WHERE i.user_id = :user_id) " +
            "ORDER BY b.start_date DESC LIMIT :size OFFSET :from",
            nativeQuery = true)
    List<Booking> findBookingByOwnerIdWithPagination(@Param("user_id") Long userId,
                                                     @Param("from") int from,
                                                     @Param("size") int size);

    @Query(value = "SELECT * FROM bookings b WHERE b.item_id " +
            "IN (SELECT i.id FROM items i WHERE i.user_id = :user_id) " +
            "ORDER BY b.start_date DESC ",
            nativeQuery = true)
    List<Booking> findBookingByOwnerId(@Param("user_id") Long userId);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE item_id = :item_id " +
            "AND start_date < :now " +
            "ORDER BY start_date DESC LIMIT 1", nativeQuery = true)
    Booking getLastBookingByItemId(@Param("item_id") Long itemId, LocalDateTime now);

    @Query(value = "SELECT * FROM bookings b " +
            "WHERE item_id = :item_id " +
            "AND start_date > :now " +
            "ORDER BY start_date ASC LIMIT 1", nativeQuery = true)
    Booking getNextBookingByItemId(@Param("item_id") Long itemId, LocalDateTime now);

    @Query(value = "SELECT COUNT (*) FROM bookings b " +
            "WHERE b.booker_id = :user_id " +
            "AND b.item_id = :item_id " +
            "AND b.status LIKE 'APPROVED'" +
            "AND b.start_date < CURRENT_TIMESTAMP", nativeQuery = true)
    int existsByBookerAndItem(@Param("user_id") long userId, @Param("item_id") long itemId);
}
