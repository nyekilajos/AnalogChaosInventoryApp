package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.CommunicationTaskUtils;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;
import roboguice.inject.ContextScopedProvider;

/**
 * Async task for logout.
 *
 * @author Lajos Nyeki
 */
public class LogoutServerCommunicationTask extends GenericServerCommunicationTask<LogoutRequest, LogoutResponse> {

    private static final String AC_API_LOGOUT_METHOD = "/logout";

    @Inject
    private LocalSettingsService localSettingsService;
    @Inject
    private ContextScopedProvider<LoginServerCommunicationTask> loginServerCommunicationTaskProvider;

    @Inject
    protected LogoutServerCommunicationTask(Context context) {
        super(context);
        setHttpMethod(HttpMethod.POST);
        setResponseType(LogoutResponse.class);
        setServerUrl(AC_API_ENDPONT + AC_API_VERSION + AC_API_ACCOUNT_MODULE + AC_API_LOGOUT_METHOD);
    }

    /**
     * Logs out the already authenticated user. If logout was successful, this method deletes the user database.
     */
    public void logout() {
        LogoutRequest logoutRequest = new LogoutRequest(localSettingsService.getSessionCode());
        setRequestEntity(new HttpEntity<>(logoutRequest, getJsonHttpHeaders()));
        execute();
    }

    @Override
    protected void onSuccess(LogoutResponse t) throws Exception {
        if (t.isSuccess()) {
            localSettingsService.reset();
        }
        super.onSuccess(t);
    }

    @Override
    public LogoutResponse call() throws Exception {
        LogoutResponse logoutResponse = null;
        try {
            logoutResponse = super.call();
        } catch (HttpStatusCodeException e) {
            if (CommunicationTaskUtils.isAuthenticationFailed(e)) {
                loginServerCommunicationTaskProvider.get(context).refreshSessionSynchronous();
                logoutResponse = super.call();
            }
        }
        return logoutResponse;
    }
}
