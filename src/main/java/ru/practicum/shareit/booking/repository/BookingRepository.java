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

    List<Booking> findBookingsByBookerId(@Param("booker_id") Long id);

    List<Booking> findBookingByBookerIdAndStatus(Long userId, Status status);

    @Query(value = "SELECT * FROM bookings b WHERE i.booker_id = id AND b.status like 'CURRENT'",
            nativeQuery = true)
    List<Booking> findCurrentBookings(@Param("booker_id") Long id);
}
