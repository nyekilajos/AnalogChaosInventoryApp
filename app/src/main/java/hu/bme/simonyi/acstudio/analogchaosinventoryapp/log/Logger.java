package hu.bme.simonyi.acstudio.analogchaosinventoryapp.log;

/**
 * Interface for logging
 *
 * @author Lajos Nyeki
 */
public interface Logger {

    /**
     * Write debug log
     *
     * @param s The string to be written into the log
     */
    void debug(String s);

    /**
     * Write debug log
     *
     * @param s The string to be written into the log
     */
    void error(String s);

    /**
     * Write debug log
     *
     * @param s The string to be written into the log
     */
    void info(String s);
}
