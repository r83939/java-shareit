package ru.practicum.shareit.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorMessage {
    @JsonProperty("Error message")
    private String message;
}