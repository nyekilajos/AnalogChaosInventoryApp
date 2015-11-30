package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net;

import java.io.IOException;

/**
 * Exception for failed server communications.
 *
 * @author Lajos_Nyeki
 */
public class ServerCommunicationException extends IOException {

    private final int errorCode;

    public ServerCommunicationException(int errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        return "Error code: " + errorCode + ", " + super.getMessage();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
