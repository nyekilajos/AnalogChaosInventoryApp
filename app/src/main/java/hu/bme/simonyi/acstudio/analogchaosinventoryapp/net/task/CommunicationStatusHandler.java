package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

/**
 * Status handler for server communication tasks.
 *
 * @param <T> The type of the response used in the server communication task.
 */
public interface CommunicationStatusHandler<T> {

    /**
     * This method called before the server communication starts. It runs on the UI thread.
     */
    void onPreExecute();

    /**
     * This method called when the server communication was successful. It runs on the UI thread.
     *
     * @param t The response of the server communication.
     */
    void onSuccess(T t);

    /**
     * This method called when some problem occurred during the server communication. It runs on the UI thread.
     *
     * @param t The throwable object that contains the problem.
     */
    void onThrowable(Throwable t);

    /**
     * This method called always after the server communication. It runs on the UI thread.
     */
    void onFinally();
}
