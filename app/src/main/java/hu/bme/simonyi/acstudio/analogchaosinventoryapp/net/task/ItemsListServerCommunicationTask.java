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

    @Inject
    private CouchbaseLiteHelper couchbaseLiteHelper;
    @Inject
    private LocalSettingsService localSettingsService;
    @Inject
    private LoginServerCommunicationTask loginServerCommunicationTask;
    @Inject
    private ServerCommunicationHelper serverCommunicationHelper;

    private ItemsListRequest itemsListRequest;

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

        private Callback<ItemsListResponse> callback;

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
            } else if (response.code() == ServerCommunicationHelper.HTTP_UNAUTHORIZED) {
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

    };
}
