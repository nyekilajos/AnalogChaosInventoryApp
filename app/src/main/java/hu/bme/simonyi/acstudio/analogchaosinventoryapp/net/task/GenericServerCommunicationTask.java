package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import roboguice.util.RoboAsyncTask;

/**
 * Generic AsyncTask for server communication.
 *
 * @author Lajos Nyeki
 */
public class GenericServerCommunicationTask<T, U> extends RoboAsyncTask<U> {

    public static final String AC_API_ENDPONT = "https://acstudio.sch.bme.hu/api";
    public static final String AC_API_VERSION = "/1.0";

    public static final String AC_API_ACCOUNT_MODULE = "/account";

    private String serverUrl;
    private HttpMethod httpMethod;
    private HttpEntity<T> requestEntity;
    private Class<U> responseType;
    private CommunicationStatusHandler<U> statusHandler;


    @Inject
    protected GenericServerCommunicationTask(Context context) {
        super(context);
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setRequestEntity(HttpEntity<T> requestEntity) {
        this.requestEntity = requestEntity;
    }

    public void setResponseType(Class<U> responseType) {
        this.responseType = responseType;
    }

    public void setStatusHandler(CommunicationStatusHandler<U> statusHandler) {
        this.statusHandler = statusHandler;
    }

    @Override
    public U call() throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());
        ResponseEntity<U> response = restTemplate.exchange(serverUrl, httpMethod, requestEntity, responseType);
        return response.getBody();
    }

    @Override
    protected void onPreExecute() throws Exception {
        super.onPreExecute();
        if (statusHandler != null) {
            statusHandler.onPreExecute();
        }
    }

    @Override
    protected void onSuccess(U t) throws Exception {
        super.onSuccess(t);
        if (statusHandler != null) {
            statusHandler.onSuccess(t);
        }
    }

    @Override
    protected void onThrowable(Throwable t) throws RuntimeException {
        super.onThrowable(t);
        if (statusHandler != null) {
            statusHandler.onThrowable(t);
        }
    }

    @Override
    protected void onFinally() throws RuntimeException {
        super.onFinally();
        if (statusHandler != null) {
            statusHandler.onFinally();
        }
    }

    /**
     * Status handler for server communication tasks.
     *
     * @param <T> The type of the response used in the server communication task.
     */
    public interface CommunicationStatusHandler<T> {

        /**
         * This method called before the server communication starts. It runs on the UI thread.
         *
         * @throws Exception
         */
        void onPreExecute() throws Exception;

        /**
         * This method called when the server communication was successful. It runs on the UI thread.
         *
         * @param t The response of the server communication.
         * @throws Exception
         */
        void onSuccess(T t) throws Exception;

        /**
         * This method called when some problem occurred during the server communication. It runs on the UI thread.
         *
         * @param t The throwable object that contains the problem.
         * @throws RuntimeException
         */
        void onThrowable(Throwable t) throws RuntimeException;

        /**
         * This method called always after the server communication. It runs on the UI thread.
         *
         * @throws RuntimeException
         */
        void onFinally() throws RuntimeException;
    }
}
