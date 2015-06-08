package hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task;

import android.content.Context;

import com.google.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.task.UpdateItemsTableTask;
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

    private Context context;

    @Inject
    private LocalSettingsService localSettingsService;

    @Inject
    private ContextScopedProvider<UpdateItemsTableTask> updateItemsTableTaskProvider;

    @Inject
    protected ItemsListServerCommunicationTask(Context context) {
        super(context);
        this.context = context;
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
        UpdateItemsTableTask updateItemsTableTask = updateItemsTableTaskProvider.get(context);
        updateItemsTableTask.setItems(itemsListResponse.getResult());
        updateItemsTableTask.call();
        return itemsListResponse;
    }
}
