package PaoloF16.u5w2d5.payloads;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;


public record NewBookingDTO(

        @NotNull(message = "Employee ID is required.")
        @Min(value = 1, message = "Employee ID must be greater than 0.")
        Long employeeId,

        @NotNull(message = "Trip ID is required.")
        @Min(value = 1, message = "Trip ID must be greater than 0.")
        Long tripId,

        @NotNull(message = "Requested trip date is required.")
        @FutureOrPresent(message = "The requested date must be today or in the future.")
        LocalDate requestedTripDate,

        @Size(max = 1000, message = "Special requests must be shorter than 1000 characters")
        String specialRequests
) {}
