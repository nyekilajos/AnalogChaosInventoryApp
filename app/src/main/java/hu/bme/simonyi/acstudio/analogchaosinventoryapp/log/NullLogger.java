package hu.bme.simonyi.acstudio.analogchaosinventoryapp.log;

/**
 * Default implementation of Logger interface. Use this class for disabling logging.
 * 
 * @author Lajos_Nyeki
 */
public class NullLogger implements Logger {
    @Override
    public void debug(String s) {
        //Default implementation, do nothing.
    }

    @Override
    public void error(String s) {
        //Default implementation, do nothing.
    }

    @Override
    public void info(String s) {
        //Default implementation, do nothing.
    }
}
