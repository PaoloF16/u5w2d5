package PaoloF16.u5w2d5.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AssignEmployeeDTO(
        @NotNull(message = "Employee ID is required.")
        @Min(value = 1, message = "Employee ID must be greater than 0.")
        Long employeeId
) {}