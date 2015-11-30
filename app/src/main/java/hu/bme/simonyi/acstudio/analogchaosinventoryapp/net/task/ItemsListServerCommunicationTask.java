package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

import com.couchbase.lite.CouchbaseLiteException;
import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.ServerCommunicationHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Async task for getting the list o the available items in the inventory.
 *
 * @author Lajos Nyeki
 */
public class ItemsListServerCommunicationTask extends GenericServerCommunicationTask<ItemsListResponse> {

    private final CouchbaseLiteHelper couchbaseLiteHelper;
    private final LocalSettingsService localSettingsService;
    private final LoginServerCommunicationTask loginServerCommunicationTask;
    private final ServerCommunicationHelper serverCommunicationHelper;

    private ItemsListRequest itemsListRequest;

    @Inject
    public ItemsListServerCommunicationTask(CouchbaseLiteHelper couchbaseLiteHelper, LocalSettingsService localSettingsService,
            LoginServerCommunicationTask loginServerCommunicationTask, ServerCommunicationHelper serverCommunicationHelper) {
        this.couchbaseLiteHelper = couchbaseLiteHelper;
        this.localSettingsService = localSettingsService;
        this.loginServerCommunicationTask = loginServerCommunicationTask;
        this.serverCommunicationHelper = serverCommunicationHelper;
    }

    /**
     * Updates the local inventory database from web.
     */
    public void updateItems() {
        itemsListRequest = new ItemsListRequest(localSettingsService.getSessionCode());
        execute();
    }

    @Override
    protected void doRequest(final Callback<ItemsListResponse> callback) {

        serverCommunicationHelper.listItems(itemsListRequest, new CallbackDecorator(callback));
    }

    private final class CallbackDecorator implements retrofit.Callback<ItemsListResponse> {

        private final Callback<ItemsListResponse> callback;

        public CallbackDecorator(Callback<ItemsListResponse> callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Response<ItemsListResponse> response, Retrofit retrofit) {
            if (response.isSuccess() && response.body().isSuccess()) {
                try {
                    couchbaseLiteHelper.writeItemsToDb(response.body().getResult());
                    callback.onResponse(response, retrofit);
                } catch (CouchbaseLiteException e) {
                    callback.onFailure(e);
                }
            } else if (response.isSuccess() && response.body().getCode() == ServerCommunicationHelper.HTTP_UNAUTHORIZED) {
                loginServerCommunicationTask.refreshSessionSynchronous();
                updateItems();
            } else {
                callback.onResponse(response, retrofit);
            }
        }

        @Override
        public void onFailure(Throwable t) {
            callback.onFailure(t);
        }

    }
}
