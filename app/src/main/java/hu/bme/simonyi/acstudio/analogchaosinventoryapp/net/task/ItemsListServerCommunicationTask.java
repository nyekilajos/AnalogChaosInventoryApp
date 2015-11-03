package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.HttpStatusCodeException;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.CommunicationTaskUtils;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;
import roboguice.inject.ContextScopedProvider;

/**
 * Async task for getting the list o the available items in the inventory.
 *
 * @author Lajos Nyeki
 */
public class ItemsListServerCommunicationTask extends GenericServerCommunicationTask<ItemsListRequest, ItemsListResponse> {

    private static final String AC_API_LIST_METHOD = "/list";

    @Inject
    private CouchbaseLiteHelper couchbaseLiteHelper;
    @Inject
    private LocalSettingsService localSettingsService;
    @Inject
    private ContextScopedProvider<LoginServerCommunicationTask> loginServerCommunicationTaskProvider;

    @Inject
    protected ItemsListServerCommunicationTask(Context context) {
        super(context);
        setHttpMethod(HttpMethod.POST);
        setResponseType(ItemsListResponse.class);
        setServerUrl(AC_API_ENDPONT + AC_API_VERSION + AC_API_ITEMS_MODULE + AC_API_LIST_METHOD);
    }

    /**
     * Updates the local inventory database from web.
     */
    public void updateItems() {
        setupRequest();
        execute();
    }

    private void setupRequest() {
        ItemsListRequest itemsListRequest = new ItemsListRequest(localSettingsService.getSessionCode());
        setRequestEntity(new HttpEntity<>(itemsListRequest, getJsonHttpHeaders()));
    }

    @Override
    public ItemsListResponse call() throws Exception {
        ItemsListResponse itemsListResponse = null;
        try {
            itemsListResponse = super.call();
        } catch (HttpStatusCodeException e) {
            if (CommunicationTaskUtils.isAuthenticationFailed(e)) {
                loginServerCommunicationTaskProvider.get(context).refreshSessionSynchronous();
                setupRequest();
                itemsListResponse = super.call();
            }
        }
        couchbaseLiteHelper.writeItemsToDb(itemsListResponse.getResult());
        return itemsListResponse;
    }
}
