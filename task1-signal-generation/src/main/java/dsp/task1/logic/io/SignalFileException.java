package dsp.task1.logic.io;

public class SignalFileException extends Exception {
    public SignalFileException(String message) {
        super(message);
    }

    public SignalFileException(String message, Throwable cause) {
        super(message, cause);
    }
}