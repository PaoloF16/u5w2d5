package PaoloF16.u5w2d5.exceptions;
import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<String> errorMessages;

    public ValidationException(List<String> errorMessages) {
        super("Validation errors!");
        this.errorMessages = errorMessages;
    }
}
