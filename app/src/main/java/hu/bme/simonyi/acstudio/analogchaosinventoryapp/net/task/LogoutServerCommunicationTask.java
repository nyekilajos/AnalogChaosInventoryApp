package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

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
    protected LogoutServerCommunicationTask(Context context) {
        super(context);
        setHttpMethod(HttpMethod.POST);
        setResponseType(LogoutResponse.class);
        setServerUrl(AC_API_ENDPONT + AC_API_VERSION + AC_API_ACCOUNT_MODULE + AC_API_LOGOUT_METHOD);
    }

    public void logout() {
        LogoutRequest logoutRequest = new LogoutRequest(localSettingsService.getSessionCode());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        setRequestEntity(new HttpEntity<>(logoutRequest, headers));
        execute();
    }

    @Override
    protected void onSuccess(LogoutResponse t) throws Exception {
        localSettingsService.reset();
        super.onSuccess(t);
    }
}
