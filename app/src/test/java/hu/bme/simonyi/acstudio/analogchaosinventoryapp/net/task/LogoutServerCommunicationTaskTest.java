package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.CODE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.EMAIL;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.MESSAGE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.PASS;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.SESSION_CODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Unit test for LogoutServerCommunicationTask class.
 *
 * @author Lajos_Nyeki
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class LogoutServerCommunicationTaskTest {

    @Mock
    private ServerCommunicationHelper serverCommunicationHelper;
    @Mock
    private LocalSettingsService localSettingsService;
    @Mock
    private CommunicationStatusHandler<LogoutResponse> statusHandler;
    @Mock
    private LoginServerCommunicationTask loginServerCommunicationTask;

    @Captor
    private ArgumentCaptor<Callback<LogoutResponse>> capturedCallback;
    @Captor
    private ArgumentCaptor<ServerCommunicationException> capturedException;

    private LogoutServerCommunicationTask sut;

    private LogoutRequest logoutRequest;
    private LogoutResponse successfulResponse;
    private LogoutResponse failedResponse;
    private LogoutResponse expiredSessionResponse;
    private ServerCommunicationException serverCommunicationException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new LogoutServerCommunicationTask(localSettingsService, loginServerCommunicationTask, serverCommunicationHelper);

        logoutRequest = new LogoutRequest(SESSION_CODE);

        successfulResponse = new LogoutResponse();
        successfulResponse.setSuccess(true);

        failedResponse = new LogoutResponse();
        failedResponse.setSuccess(false);
        failedResponse.setCode(CODE_ERROR);

        expiredSessionResponse = new LogoutResponse();
        expiredSessionResponse.setSuccess(false);
        expiredSessionResponse.setCode(ServerCommunicationHelper.HTTP_UNAUTHORIZED);

        serverCommunicationException = new ServerCommunicationException(CODE_ERROR, MESSAGE_ERROR);

        when(localSettingsService.getEmailAddress()).thenReturn(EMAIL);
        when(localSettingsService.getPassword()).thenReturn(PASS);
        when(localSettingsService.getSessionCode()).thenReturn(SESSION_CODE);
    }

    @Test
    public void testLogoutStartsTheTask() {
        sut.setStatusHandler(statusHandler);
        sut.logout();
        verify(statusHandler, times(1)).onPreExecute();
    }

    @Test
    public void testRequestSuccessful() {
        sut.setStatusHandler(statusHandler);
        sut.logout();
        verify(serverCommunicationHelper).logout(eq(logoutRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(successfulResponse), null);
        verify(localSettingsService).reset();
        verify(statusHandler).onSuccess(eq(successfulResponse));
    }

    @Test
    public void testRequestFailedOnServer() {
        sut.setStatusHandler(statusHandler);
        sut.logout();
        verify(serverCommunicationHelper).logout(eq(logoutRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(failedResponse), null);
        verify(localSettingsService, times(0)).reset();
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

    @Test
    public void testRequestFailedWithExpiredSession() {
        sut = spy(sut);
        sut.setStatusHandler(statusHandler);
        sut.logout();
        verify(serverCommunicationHelper).logout(eq(logoutRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(expiredSessionResponse), null);
        verify(localSettingsService, times(0)).reset();
        verify(loginServerCommunicationTask).refreshSessionSynchronous();
        verify(sut, times(2)).logout();
    }

    @Test
    public void testRequestCommunicationFailed() {
        sut.setStatusHandler(statusHandler);
        sut.logout();
        verify(serverCommunicationHelper).logout(eq(logoutRequest), capturedCallback.capture());
        capturedCallback.getValue().onFailure(serverCommunicationException);
        verify(localSettingsService, times(0)).reset();
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }
}
