package bg.uni.fmi.theatre.validation;

import bg.uni.fmi.theatre.exception.ValidationException;

public class Validator {
    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new ValidationException(message);
        }
    }

    public static void validatePositiveNumber(Number number, String message) {
        if (number.doubleValue() <= 0) {
            throw new ValidationException(message);
        }
    }

    public static void validateNonNegativeNumber(Number number, String message) {
        if (number.doubleValue() < 0) {
            throw new ValidationException(message);
        }
    }

    public static void validateString(String str, String message) {
        if (str == null || str.isBlank()) {
            throw new ValidationException(message);
        }
    }

}
