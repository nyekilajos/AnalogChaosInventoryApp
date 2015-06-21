package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

/**
 * Simple CommunicationStatusHandler implementation. With this class you do not have to implement all methods if you do not need all.
 * Additionally you do not have to modify your code if the status handler will be more methods.
 */
public class SimpleStatusHandler<T> implements GenericServerCommunicationTask.CommunicationStatusHandler<T> {

    @Override
    public void onPreExecute() throws Exception {

    }

    @Override
    public void onSuccess(T t) throws Exception {

    }

    @Override
    public void onThrowable(Throwable t) throws RuntimeException {

    }

    @Override
    public void onFinally() throws RuntimeException {

    }
}
