package eu.linkedtv.utils.asr;

public class AsrException extends Exception {
    private static final long serialVersionUID = 1L;

    public AsrException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public AsrException(String message) {
        super(message);
    }

}
