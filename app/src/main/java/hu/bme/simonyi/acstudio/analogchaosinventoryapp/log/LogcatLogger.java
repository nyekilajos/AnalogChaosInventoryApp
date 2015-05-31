package hu.bme.simonyi.acstudio.analogchaosinventoryapp.log;

import android.util.Log;

/**
 * Logger logging to Logcat
 *
 * @author Lajos Nyeki
 */
public class LogcatLogger implements Logger {

    private String tag;

    public LogcatLogger(String tag) {
        this.tag = tag;
    }

    @Override
    public void debug(String s) {
        Log.d(tag, s);
    }

    @Override
    public void error(String s) {
        Log.e(tag, s);
    }

    @Override
    public void info(String s) {
        Log.i(tag, s);
    }
}
