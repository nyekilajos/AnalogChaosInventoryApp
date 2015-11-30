package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.Logger;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.LoggerFactory;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationException;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.GenericServerResponse;

/**
 * Generic AsyncTask for server communication.
 *
 * @author Lajos Nyeki
 */
public abstract class GenericServerCommunicationTask<T extends GenericServerResponse<?>> {

    protected static final Logger LOGGER = LoggerFactory.createLogger(GenericServerCommunicationTask.class);

    private CommunicationStatusHandler<T> statusHandler;

    public void setStatusHandler(CommunicationStatusHandler<T> statusHandler) {
        this.statusHandler = statusHandler;
    }

    protected void execute() {
        if (statusHandler != null) {
            statusHandler.onPreExecute();
        }
        doRequest(new RetrofitCallback());
    }

    protected abstract void doRequest(Callback<T> callback);

    private final class RetrofitCallback implements Callback<T> {

        @Override
        public void onResponse(Response<T> response, Retrofit retrofit) {
            LOGGER.debug("Response from server: " + response.message()
                    + (response.isSuccess() ? "; body: " + response.body() : "; error body: " + response.errorBody()));
            if (statusHandler != null) {
                if (response.isSuccess()) {
                    if (response.body().isSuccess()) {
                        statusHandler.onSuccess(response.body());
                    } else {
                        statusHandler.onThrowable(new ServerCommunicationException(response.body().getCode(), response.body().getText()));
                    }
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
