package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.ResponseEntity;


import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class ExceptionApiHandlerTest {
    ExceptionApiHandler handler = new ExceptionApiHandler();

    @Test
    void invalidStateBookingException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.invalidStateBookingException(new InvalidStateBookingException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }

    @Test
    void invalidParameterException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.invalidParameterException(new InvalidParameterException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }

    @Test
    void entityAlreadyExistException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.entityAlreadyExistException(new EntityAlreadyExistException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }

    @Test
    void duplicateEmailException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.duplicateEmailException(new DuplicateEmailException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }

    @Test
    void entityNotFoundException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.entityNotFoundException(new EntityNotFoundException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }

    @Test
    void accessDeniedException() {
        String errorMessage = "ERRORMESSAGE";
        ResponseEntity<ErrorMessage> responseEntity =   handler.accessDeniedException(new AccessDeniedException(errorMessage));
        assertEquals(errorMessage, Objects.requireNonNull(responseEntity.getBody().getMessage()));
    }
}