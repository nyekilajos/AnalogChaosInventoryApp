package hu.bme.simonyi.acstudio.analogchaosinventoryapp.log;

/**
 * Factory class for creating the appropriate Logger
 *
 * @author Lajos Nyeki
 */
public class LoggerFactory {

    /**
     * Returns a Logger instance according to the actual build configuration.
     *
     * @param c The Class where the Logger is used. Used to identify where the log message came from.
     * @return The appropriate Logger instance
     */
    public static Logger createLogger(Class c) {
        return new LogcatLogger(c.getName());
    }
}
