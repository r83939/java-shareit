package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.domain.validator.Create;
import ru.practicum.shareit.exception.InvalidStateBookingException;


import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {
	private static final String USER_ID = "X-Sharer-User-Id";
	private final BookingClient bookingClient;

	@GetMapping(value = "/{bookingId}")
	public ResponseEntity<Object> getBooking(
			@RequestHeader(USER_ID) Long userId,
			@PathVariable Long bookingId) {
		log.info("BookingGatewayController: getBooking implementation. User ID {}, booking ID {}.", userId, bookingId);
		return bookingClient.getBooking(userId, bookingId);
	}

	@PostMapping
	public ResponseEntity<Object> addBooking(
			@RequestHeader(USER_ID) Long userId,
			@Validated(Create.class)
			@RequestBody BookItemRequestDto bookingRequestDto) {
		log.info("BookingGatewayController: createBooking implementation. User ID {}.", userId);
		return bookingClient.createBooking(userId, bookingRequestDto);
	}

	@PatchMapping(value = "/{bookingId}")
	public ResponseEntity<Object> approveBooking(
			@RequestHeader(USER_ID) Long userId,
			@PathVariable Long bookingId,
			@RequestParam Boolean approved) {
		log.info("BookingGatewayController: updateBookingStatus implementation. User ID {}, booking ID {}.",
				userId, bookingId);
		return bookingClient.approveBooking(userId, bookingId, approved);
	}

	@GetMapping
	public ResponseEntity<Object> getBookings(
			@RequestHeader(USER_ID) Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String bookingState,
			@RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
			@RequestParam(value = "size", defaultValue = "10") @Positive Integer size) throws InvalidStateBookingException {
		log.info("BookingGatewayController: getBookingsByStatus implementation. User ID {}, stateText {}.",
				userId, bookingState);
		return bookingClient.getBookings(userId, bookingState, from, size);
	}

	@GetMapping(value = "/owner")
	public ResponseEntity<Object> getOwnBookingsByUserId(
			@RequestHeader(USER_ID) Long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String bookingState,
			@RequestParam(value = "from", defaultValue = "0")
			@PositiveOrZero Integer from,
			@RequestParam(value = "size", defaultValue = "10")
			@Positive Integer size) throws InvalidStateBookingException {
		log.info("BookingGatewayController: getBookingsByOwnerAndStatus implementation. User ID {}, stateText {}.",
				userId, bookingState);
		return bookingClient.getBookingsByOwnerAndStatus(userId, bookingState, from, size);
	}
}
