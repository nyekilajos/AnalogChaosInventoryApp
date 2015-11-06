package hu.bme.simonyi.acstudio.analogchaosinventoryapp.log;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;

/**
 * Factory class for creating the appropriate Logger
 *
 * @author Lajos Nyeki
 */
public class LoggerFactory {

    private LoggerFactory() {

    }

    /**
     * Returns a Logger instance according to the actual build configuration.
     *
     * @param c The Class where the Logger is used. Used to identify where the log message came from.
     * @return The appropriate Logger instance
     */
    public static Logger createLogger(Class c) {
        if (BuildConfig.DEBUG) {
            return new LogcatLogger(c.getName());
        } else {
            return new NullLogger();
        }

    }
}
