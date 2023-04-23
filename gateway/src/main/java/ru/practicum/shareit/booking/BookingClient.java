package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.exception.InvalidStateBookingException;

import java.util.Map;
import java.util.stream.Stream;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getBookings(Long userId, String bookingState, Integer from, Integer size) throws InvalidStateBookingException {
        checkBookingStatus(bookingState);
        Map<String, Object> parameters = Map.of(
                "state", bookingState,
                "from", from,
                "size", size
        );
        return get("?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBookingsByOwnerAndStatus(Long userId, String bookingState, Integer from, Integer size) throws InvalidStateBookingException {
        checkBookingStatus(bookingState);
        Map<String, Object> parameters = Map.of(
                "state", bookingState,
                "from", from,
                "size", size
        );
        return get("/owner?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getBooking(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookItemRequestDto requestDto) {
        return post("", userId, requestDto);
    }

    public ResponseEntity<Object> approveBooking(Long userId, Long bookingId, Boolean approved) {
        Map<String, Object> parameters = Map.of("approved", approved);
        return patch("/" + bookingId + "?approved={approved}", userId, parameters, null);
    }

    private void checkBookingStatus(String stateText) throws InvalidStateBookingException {
        if (!Stream.of(State.values()).anyMatch(v -> v.name().equals(stateText))) {
            throw new InvalidStateBookingException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

}
