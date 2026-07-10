package PaoloF16.u5w2d5.payloads;

import PaoloF16.u5w2d5.enums.TripStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record NewTripDTO(
        @NotBlank(message = "Destination is required.")
        String destination,

        @NotNull(message = "Trip date is required.")
        @FutureOrPresent(message = "The requested date must be today or in the future")
        LocalDate dateOfTravel,

        @NotNull(message = "Trip status is required.")
        TripStatus status) {
}