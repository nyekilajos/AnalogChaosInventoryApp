package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Async task for logout.
 *
 * @author Lajos Nyeki
 */
public class LogoutServerCommunicationTask extends GenericServerCommunicationTask<LogoutResponse> {

    private final LocalSettingsService localSettingsService;
    private final LoginServerCommunicationTask loginServerCommunicationTask;
    private final ServerCommunicationHelper serverCommunicationHelper;

    private LogoutRequest logoutRequest;

    @Inject
    public LogoutServerCommunicationTask(LocalSettingsService localSettingsService, LoginServerCommunicationTask loginServerCommunicationTask,
            ServerCommunicationHelper serverCommunicationHelper) {
        this.localSettingsService = localSettingsService;
        this.loginServerCommunicationTask = loginServerCommunicationTask;
        this.serverCommunicationHelper = serverCommunicationHelper;
    }

    /**
     * Logs out the already authenticated user. If logout was successful, this method deletes the user database.
     */
    public void logout() {
        logoutRequest = new LogoutRequest(localSettingsService.getSessionCode());
        execute();
    }

    @Override
    protected void doRequest(final Callback<LogoutResponse> callback) {
        Callback<LogoutResponse> callbackDecorator = new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Response<LogoutResponse> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().isSuccess()) {
                    localSettingsService.reset();
                    callback.onResponse(response, retrofit);
                } else if (response.isSuccess() && response.body().getCode() == ServerCommunicationHelper.HTTP_UNAUTHORIZED) {
                    loginServerCommunicationTask.refreshSessionSynchronous();
                    logout();
                } else {
                    callback.onResponse(response, retrofit);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        };
        serverCommunicationHelper.logout(logoutRequest, callbackDecorator);
    }
}
