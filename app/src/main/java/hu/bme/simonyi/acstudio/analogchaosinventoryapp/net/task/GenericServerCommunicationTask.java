package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationException;

/**
 * Generic AsyncTask for server communication.
 *
 * @author Lajos Nyeki
 */
public abstract class GenericServerCommunicationTask<T> {

    private CommunicationStatusHandler<T> statusHandler;

    public void setStatusHandler(CommunicationStatusHandler<T> statusHandler) {
        this.statusHandler = statusHandler;
    }

    protected void execute() {
        if (statusHandler != null) {
            statusHandler.onPreExecute();
        }
        doRequest(new RetrofitCallback<>());
    }

    protected abstract void doRequest(Callback<T> callback);

    private final class RetrofitCallback<U extends T> implements Callback<U> {

        @Override
        public void onResponse(Response<U> response, Retrofit retrofit) {
            if (statusHandler != null) {
                if (response.isSuccess()) {
                    statusHandler.onSuccess(response.body());
                } else {
                    statusHandler.onThrowable(new ServerCommunicationException(response.code(), response.message()));
                }
                statusHandler.onFinally();
            }
        }

        @Override
        public void onFailure(Throwable t) {
            if (statusHandler != null) {
                statusHandler.onThrowable(t);
                statusHandler.onFinally();
            }
        }
    }
}
