package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * Utils class for common stateless server communication operations.
 *
 * @author Lajos Nyeki
 */
public class CommunicationTaskUtils {

    private CommunicationTaskUtils() {

    }

    /**
     * Returns whether a trowable object refers to an authentication failure or not.
     *
     * @param t The throwable to be investigated.
     * @return True if there was an authentication error.
     */
    public static boolean isAuthenticationFailed(Throwable t) {
        return t != null && t instanceof HttpStatusCodeException && HttpStatus.UNAUTHORIZED.equals((((HttpStatusCodeException) t).getStatusCode()));
    }
}
