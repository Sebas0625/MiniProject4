package org.example.eiscuno.model.exception;

/**
 * Custom exception class for the game.
 * This class provides detailed error handling mechanisms for game-related exceptions.
 *
 * @author Juan Sebástian Sierra Cruz
 * @code 202343656
 * @author Maycol Ándres Taquez Carlosama
 * @code 202375000
 * @author Santiago Valencia Aguiño
 * @code 202343334
 * @version 1.0
 * @since 1.0
 * @see Exception
 * @serial This class supports serialization, inheriting the serializable behavior from {@link Exception}.
 */
public class GameException extends Exception{

    /**
     * Default constructor for GameException.
     * Initializes the exception without a specific message or cause.
     *
     * @serialData No additional data is serialized for this constructor.
     * @since 1.0
     */
    public GameException() {
        super();
    }

    /**
     * Constructor for GameException with a custom message.
     * Initializes the exception with the provided message to describe the error.
     *
     * @param message the detail message explaining the reason for the exception.
     * @serialData The message is serialized as part of the superclass {@link Exception}.
     * @since 1.0
     */
    public GameException(String message) {
        super(message);
    }

    /**
     * Constructor for GameException with a cause.
     * Initializes the exception with the specified cause of the error.
     *
     * @param cause the throwable that caused this exception to be thrown.
     * @serialData The cause is serialized as part of the superclass {@link Exception}.
     * @since 1.0
     */
    public GameException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor for GameException with both a custom message and a cause.
     * Initializes the exception with the provided message and cause.
     *
     * @param message the detail message explaining the reason for the exception.
     * @param cause the throwable that caused this exception to be thrown.
     * @serialData Both the message and cause are serialized as part of the superclass {@link Exception}.
     * @since 1.0
     */
    public GameException(String message, Throwable cause){
        super(message, cause);
    }
}
