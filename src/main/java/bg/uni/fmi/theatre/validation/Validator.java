package bg.uni.fmi.theatre.validation;

public class Validator {
    public static void validateNotNull(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validatePositiveNumber(Number number, String message) {
        if (number.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateNonNegativeNumber(Number number, String message) {
        if (number.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void validateString(String str, String message) {
        if (str == null) {
            throw new IllegalArgumentException(message);
        }
        if (str == null || str.strip().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

}
