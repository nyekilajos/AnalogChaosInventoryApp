package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.CODE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.EMAIL;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.MESSAGE_ERROR;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.PASS;
import static hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ServerCommunicationTestConstants.SESSION_CODE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

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

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Status;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.BuildConfig;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationException;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Unit test for ItemsListServerCommunicationTask class.
 *
 * @author Lajos_Nyeki
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class ItemsListServerCommunicationTaskTest {

    @Mock
    private CouchbaseLiteHelper couchbaseLiteHelper;
    @Mock
    private ServerCommunicationHelper serverCommunicationHelper;
    @Mock
    private LocalSettingsService localSettingsService;
    @Mock
    private CommunicationStatusHandler<ItemsListResponse> statusHandler;
    @Mock
    private LoginServerCommunicationTask loginServerCommunicationTask;

    @Captor
    private ArgumentCaptor<Callback<ItemsListResponse>> capturedCallback;
    @Captor
    private ArgumentCaptor<ServerCommunicationException> capturedException;

    private ItemsListServerCommunicationTask sut;

    private ItemsListRequest itemsListRequest;
    private ItemsListResponse successfulResponse;
    private List<Item> results;

    private ItemsListResponse failedResponse;
    private ItemsListResponse expiredSessionResponse;
    private ServerCommunicationException serverCommunicationException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sut = new ItemsListServerCommunicationTask(couchbaseLiteHelper, localSettingsService, loginServerCommunicationTask,
                serverCommunicationHelper);

        itemsListRequest = new ItemsListRequest(SESSION_CODE);

        results = new ArrayList<>();
        results.add(new Item());

        successfulResponse = new ItemsListResponse();
        successfulResponse.setSuccess(true);
        successfulResponse.setResult(results);

        failedResponse = new ItemsListResponse();
        failedResponse.setSuccess(false);
        failedResponse.setCode(CODE_ERROR);

        expiredSessionResponse = new ItemsListResponse();
        expiredSessionResponse.setSuccess(false);
        expiredSessionResponse.setCode(ServerCommunicationHelper.HTTP_UNAUTHORIZED);

        serverCommunicationException = new ServerCommunicationException(CODE_ERROR, MESSAGE_ERROR);

        when(localSettingsService.getEmailAddress()).thenReturn(EMAIL);
        when(localSettingsService.getPassword()).thenReturn(PASS);
        when(localSettingsService.getSessionCode()).thenReturn(SESSION_CODE);
    }

    @Test
    public void testUpdateItemsStartsTheTask() {
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(statusHandler, times(1)).onPreExecute();
    }

    @Test
    public void testRequestSuccessful() throws CouchbaseLiteException {
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(serverCommunicationHelper).listItems(eq(itemsListRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(successfulResponse), null);
        verify(couchbaseLiteHelper).writeItemsToDb(eq(results));
        verify(statusHandler).onSuccess(eq(successfulResponse));
    }

    @Test
    public void testRequestFailedOnServer() {
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(serverCommunicationHelper).listItems(eq(itemsListRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(failedResponse), null);
        verify(localSettingsService, times(0)).reset();
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }

    @Test
    public void testRequestFailedWithExpiredSession() {
        sut = spy(sut);
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(serverCommunicationHelper).listItems(eq(itemsListRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(expiredSessionResponse), null);
        verify(localSettingsService, times(0)).reset();
        verify(loginServerCommunicationTask).refreshSessionSynchronous();
        verify(sut, times(2)).updateItems();
    }

    @Test
    public void testRequestFailedWithDatabaseException() throws CouchbaseLiteException {
        doThrow(new CouchbaseLiteException(Status.DB_ERROR)).when(couchbaseLiteHelper).writeItemsToDb(eq(results));
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(serverCommunicationHelper).listItems(eq(itemsListRequest), capturedCallback.capture());
        capturedCallback.getValue().onResponse(Response.success(successfulResponse), null);
        verify(statusHandler).onThrowable(any(CouchbaseLiteException.class));
    }

    @Test
    public void testRequestCommunicationFailed() {
        sut.setStatusHandler(statusHandler);
        sut.updateItems();
        verify(serverCommunicationHelper).listItems(eq(itemsListRequest), capturedCallback.capture());
        capturedCallback.getValue().onFailure(serverCommunicationException);
        verify(localSettingsService, times(0)).reset();
        verify(statusHandler).onThrowable(capturedException.capture());
        assertEquals(CODE_ERROR, capturedException.getValue().getErrorCode());
    }
}
