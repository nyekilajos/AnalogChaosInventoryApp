package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;

/**
 * Unit test for GenericServerCommunicationTask class.
 * 
 * @author Lajos_Nyeki
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class GenericServerCommunicationTaskTest {

    private static final int CODE_ERROR = 500;

    @Mock
    private CommunicationStatusHandler<LogoutResponse> statusHandler;

    @Captor
    private ArgumentCaptor<ServerCommunicationException> capturedException;

    private LogoutResponse logoutResponse;
    private Response<LogoutResponse> successResponse;
    private Response<LogoutResponse> errorResponse;

    private GenericServerCommunicationTask<LogoutResponse> sut;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        logoutResponse = new LogoutResponse();
        successResponse = Response.success(logoutResponse);
        errorResponse = Response.error(CODE_ERROR, null);
    }

    @Test
    public void testExecuteSuccessfullyWithoutStatusHandler() {
        sut = new GenericServerCommunicationTask<LogoutResponse>() {
            @Override
            protected void doRequest(Callback<LogoutResponse> callback) {
                callback.onResponse(successResponse, null);
            }
        };
        sut.execute();
    }

    @Test
    public void testExecuteFailureWithoutStatusHandler() {
        sut = new GenericServerCommunicationTask<LogoutResponse>() {
            @Override
            protected void doRequest(Callback<LogoutResponse> callback) {
                callback.onResponse(errorResponse, null);
            }
        };
        sut.execute();
    }

    @Test
    public void testExecuteSuccessfully() {
        sut = new GenericServerCommunicationTask<LogoutResponse>() {
            @Override
            protected void doRequest(Callback<LogoutResponse> callback) {
                callback.onResponse(successResponse, null);
            }
        };

        sut.setStatusHandler(statusHandler);
        logoutResponse.setSuccess(true);
        sut.execute();
        verify(statusHandler, times(1)).onPreExecute();
        verify(statusHandler, times(1)).onSuccess(eq(successResponse.body()));
        verify(statusHandler, times(1)).onFinally();
    }

    @Test
    public void testExecuteWithWrongAnswerFromServer() {
        sut = new GenericServerCommunicationTask<LogoutResponse>() {
            @Override
            protected void doRequest(Callback<LogoutResponse> callback) {
                callback.onResponse(successResponse, null);
            }
        };

        sut.setStatusHandler(statusHandler);
        logoutResponse.setSuccess(false);
        logoutResponse.setCode(CODE_ERROR);
        sut.execute();
        verify(statusHandler, times(1)).onPreExecute();
        verify(statusHandler, times(1)).onThrowable(capturedException.capture());
        verify(statusHandler, times(1)).onFinally();

        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

    @Test
    public void testExecuteWithCommunicationFailure() {
        sut = new GenericServerCommunicationTask<LogoutResponse>() {
            @Override
            protected void doRequest(Callback<LogoutResponse> callback) {
                callback.onResponse(errorResponse, null);
            }
        };

        sut.setStatusHandler(statusHandler);
        sut.execute();
        verify(statusHandler, times(1)).onPreExecute();
        verify(statusHandler, times(1)).onThrowable(capturedException.capture());
        verify(statusHandler, times(1)).onFinally();

        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

}
