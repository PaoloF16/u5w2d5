package PaoloF16.u5w2d5.payloads;

import PaoloF16.u5w2d5.enums.TripStatus;
import jakarta.validation.constraints.NotNull;


public record UpdateTripStatusDTO(
        @NotNull(message = "New status is required.")
        TripStatus newStatus
) {}