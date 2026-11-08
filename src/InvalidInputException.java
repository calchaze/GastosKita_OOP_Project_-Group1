/** Thrown when the user enters invalid data (negative amounts, empty fields, bad dates...). */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super(message);
    }
}
