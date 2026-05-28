package dsp.common.io;

public class SignalFileException extends Exception {
    public SignalFileException(String message) {
        super(message);
    }

    public SignalFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
