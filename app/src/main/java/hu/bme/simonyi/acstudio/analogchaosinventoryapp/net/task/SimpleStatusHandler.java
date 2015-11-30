package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

/**
 * Simple CommunicationStatusHandler implementation. With this class you do not have to implement all methods if you do not need all.
 * Additionally you do not have to modify your code if the status handler will be more methods.
 */
public class SimpleStatusHandler<T> implements CommunicationStatusHandler<T> {

    @Override
    public void onPreExecute() {
        //Default implementation. Override if needed.
    }

    @Override
    public void onSuccess(T t) {
        //Default implementation. Override if needed.
    }

    @Override
    public void onThrowable(Throwable t) {
        //Default implementation. Override if needed.
    }

    @Override
    public void onFinally() {
        //Default implementation. Override if needed.
    }
}
