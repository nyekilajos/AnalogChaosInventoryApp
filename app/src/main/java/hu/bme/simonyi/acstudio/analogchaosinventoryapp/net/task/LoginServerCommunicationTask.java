package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import java.io.IOException;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationException;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Async task for login.
 *
 * @author Lajos Nyeki
 */
public class LoginServerCommunicationTask extends GenericServerCommunicationTask<LoginResponse> {

    private static final String REMEMBER_ME = "no";

    private final LocalSettingsService localSettingsService;
    private final ServerCommunicationHelper serverCommunicationHelper;

    private LoginRequest loginRequest;

    @Inject
    public LoginServerCommunicationTask(LocalSettingsService localSettingsService, ServerCommunicationHelper serverCommunicationHelper) {
        this.localSettingsService = localSettingsService;
        this.serverCommunicationHelper = serverCommunicationHelper;
    }

    /**
     * Creates a login request and executes it, as well.
     *
     * @param email    The e-mail address of the user.
     * @param password The password of the user.
     */
    public void login(String email, String password) {
        loginRequest = new LoginRequest(email, password, REMEMBER_ME);
        execute();
    }

    /**
     * Refreshes the session code with the user data stored in the local settings without creating a new thread.
     */
    public void refreshSessionSynchronous() {
        LoginRequest refreshSessionRequest = new LoginRequest(localSettingsService.getEmailAddress(), localSettingsService.getPassword(),
                REMEMBER_ME);
        try {
            LoginResponse response = serverCommunicationHelper.loginSynchronous(refreshSessionRequest);
            if (response.isSuccess()) {
                localSettingsService.setSessionCode(response.getResult());
            } else {
                throw new ServerCommunicationException(response.getCode(), response.getText());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    protected void doRequest(final Callback<LoginResponse> callback) {
        Callback<LoginResponse> callbackDecorator = new Callback<LoginResponse>() {
            @Override
            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body().isSuccess()) {
                    localSettingsService.setSessionCode(response.body().getResult());
                }
                callback.onResponse(response, retrofit);
            }

            @Override
            public void onFailure(Throwable t) {
                callback.onFailure(t);
            }
        };
        serverCommunicationHelper.login(loginRequest, callbackDecorator);

    }
}
