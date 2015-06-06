package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Async task for login.
 *
 * @author Lajos Nyeki
 */
public class LoginServerCommunicationTask extends GenericServerCommunicationTask<LoginRequest, LoginResponse> {

    private static final String AC_API_LOGIN_METHOD = "/login";
    private static final String REMEMBERME = "no";

    @Inject
    private LocalSettingsService localSettingsService;

    @Inject
    protected LoginServerCommunicationTask(Context context) {
        super(context);
        setHttpMethod(HttpMethod.POST);
        setResponseType(LoginResponse.class);
        setServerUrl(AC_API_ENDPONT + AC_API_VERSION + AC_API_ACCOUNT_MODULE + AC_API_LOGIN_METHOD);
    }

    /**
     * Creates a login request and executes it, as well.
     *
     * @param email    The e-mail address of the user.
     * @param password The password of the user.
     */
    public void login(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password, REMEMBERME);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        setRequestEntity(new HttpEntity<>(loginRequest, headers));
        execute();
    }

    @Override
    protected void onSuccess(LoginResponse t) throws Exception {
        localSettingsService.setSessionCode(t.getResult());
        super.onSuccess(t);
    }
}
