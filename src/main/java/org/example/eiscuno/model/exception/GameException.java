package org.example.eiscuno.model.exception;

/**
 * Represents a custom exception for game-related errors in the EISCUno application.
 * This class extends {@link Exception} and provides constructors for various use cases.
 */
public class GameException extends Exception {

    /**
     * Constructs a new {@code GameException} with no detail message or cause.
     */
    public GameException() {
        super();
    }

    /**
     * Constructs a new {@code GameException} with the specified detail message.
     *
     * @param message the detail message, which provides additional context for the exception
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructs a new {@code GameException} with the specified cause.
     *
     * @param cause the cause of the exception, which can be another throwable
     */
    public GameException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new {@code GameException} with the specified detail message and cause.
     *
     * @param message the detail message, which provides additional context for the exception
     * @param cause   the cause of the exception, which can be another throwable
     */
    public GameException(String message, Throwable cause) {
        super(message, cause);
    }
}
