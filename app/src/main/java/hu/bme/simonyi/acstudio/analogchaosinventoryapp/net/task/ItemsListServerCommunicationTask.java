package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListRequest;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;

/**
 * Async task for getting the list o the available items in the inventory.
 *
 * @author Lajos Nyeki
 */
public class ItemsListServerCommunicationTask extends GenericServerCommunicationTask<ItemsListRequest, ItemsListResponse> {

    private static final String AC_API_LIST_METHOD = "/list";

    private CouchbaseLiteHelper couchbaseLiteHelper;
    private LocalSettingsService localSettingsService;

    @Inject
    protected ItemsListServerCommunicationTask(Context context, LocalSettingsService localSettingsService, CouchbaseLiteHelper couchbaseLiteHelper) {
        super(context);
        this.localSettingsService = localSettingsService;
        this.couchbaseLiteHelper = couchbaseLiteHelper;
        setHttpMethod(HttpMethod.POST);
        setResponseType(ItemsListResponse.class);
        setServerUrl(AC_API_ENDPONT + AC_API_VERSION + AC_API_ITEMS_MODULE + AC_API_LIST_METHOD);
    }

    /**
     * Updates the local inventory database from web.
     */
    public void updateItems() {
        ItemsListRequest itemsListRequest = new ItemsListRequest(localSettingsService.getSessionCode());
        setRequestEntity(new HttpEntity<>(itemsListRequest, getJsonHttpHeaders()));
        execute();
    }

    @Override
    public ItemsListResponse call() throws Exception {
        ItemsListResponse itemsListResponse = super.call();
        couchbaseLiteHelper.writeItemsToDb(itemsListResponse.getResult());
        return itemsListResponse;
    }
}
