package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.CODE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.EMAIL;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.MESSAGE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.PASS;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.REMEMBER_ME;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.SESSION_CODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import retrofit.Callback;
import retrofit.Response;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationException;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LoginResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Unit test for LoginServerCommunicationTask class.
 * 
 * @author Lajos_Nyeki
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LoginServerCommunicationTaskTest {

    @Mock
    private ServerCommunicationHelper serverCommunicationHelper;
    @Mock
    private LocalSettingsService localSettingsService;
    @Mock
    private CommunicationStatusHandler<LoginResponse> statusHandler;

    @Captor
    private ArgumentCaptor<Callback<LoginResponse>> capturedCallback;
    @Captor
    private ArgumentCaptor<ServerCommunicationException> capturedException;

    private LoginServerCommunicationTask sut;

    private LoginRequest loginRequest;
    private LoginResponse successfulResponse;
    private LoginResponse failedResponse;
    private ServerCommunicationException serverCommunicationException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new LoginServerCommunicationTask(localSettingsService, serverCommunicationHelper);

        loginRequest = new LoginRequest(EMAIL, PASS, REMEMBER_ME);

        successfulResponse = new LoginResponse();
        successfulResponse.setSuccess(true);
        successfulResponse.setResult(SESSION_CODE);

        failedResponse = new LoginResponse();
        failedResponse.setSuccess(false);
        failedResponse.setCode(CODE_ERROR);

        serverCommunicationException = new ServerCommunicationException(CODE_ERROR, MESSAGE_ERROR);
    }

    @Test
    public void testLoginStartsTheTask() {
        sut.setStatusHandler(statusHandler);
        sut.login(EMAIL, PASS);
        verify(statusHandler, times(1)).onPreExecute();
    }

    @Test
    public void testRequestSuccessful() {
        sut.setStatusHandler(statusHandler);
        sut.login(EMAIL, PASS);
        verify(serverCommunicationHelper).login(eq(loginRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(successfulResponse), null);
        verify(localSettingsService).setSessionCode(eq(SESSION_CODE));
        verify(statusHandler).onSuccess(eq(successfulResponse));
    }

    @Test
    public void testRequestFailedOnServer() {
        sut.setStatusHandler(statusHandler);
        sut.login(EMAIL, PASS);
        verify(serverCommunicationHelper).login(eq(loginRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(failedResponse), null);
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

    @Test
    public void testRequestCommunicationFailed() {
        sut.setStatusHandler(statusHandler);
        sut.login(EMAIL, PASS);
        verify(serverCommunicationHelper).login(eq(loginRequest), capturedCallback.capture());
        capturedCallback.getValue().onFailure(serverCommunicationException);
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

    @Test
    public void testRefreshSessionSuccessful() throws IOException {
        when(localSettingsService.getEmailAddress()).thenReturn(EMAIL);
        when(localSettingsService.getPassword()).thenReturn(PASS);
        when(serverCommunicationHelper.loginSynchronous(loginRequest)).thenReturn(successfulResponse);
        sut.refreshSessionSynchronous();
        verify(localSettingsService).setSessionCode(SESSION_CODE);
    }

    @Test
    public void testRefreshSessionFailedOnServer() throws IOException {
        when(localSettingsService.getEmailAddress()).thenReturn(EMAIL);
        when(localSettingsService.getPassword()).thenReturn(PASS);
        when(serverCommunicationHelper.loginSynchronous(loginRequest)).thenReturn(failedResponse);
        sut.refreshSessionSynchronous();
        verify(localSettingsService, times(0)).setSessionCode(anyString());
    }

    @Test
    public void testRefreshSessionFailed() throws IOException {
        when(localSettingsService.getEmailAddress()).thenReturn(EMAIL);
        when(localSettingsService.getPassword()).thenReturn(PASS);
        when(serverCommunicationHelper.loginSynchronous(loginRequest)).thenThrow(new IOException());
        sut.refreshSessionSynchronous();
        verify(localSettingsService, times(0)).setSessionCode(anyString());
    }

}
