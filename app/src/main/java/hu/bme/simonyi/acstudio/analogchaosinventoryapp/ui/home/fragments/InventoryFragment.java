package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.couchbase.lite.CouchbaseLiteException;
import com.google.inject.Inject;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory.TreeCreator;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.GenericServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ItemsListServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.dialog.DialogFactory;
import roboguice.fragment.RoboFragment;
import roboguice.inject.ContextScopedProvider;

/**
 * Fragment for displaying the inventory list of the AC Studio & Live
 *
 * @author Lajos Nyeki
 */
public class InventoryFragment extends RoboFragment {

    public static final int TITLE_ID = R.string.navigation_inventory;
    public static final String TAG = InventoryFragment.class.getSimpleName();

    @Inject
    private DialogFactory dialogFactory;
    @Inject
    private TreeCreator treeCreator;
    @Inject
    private CouchbaseLiteHelper couchbaseLiteHelper;

    @Inject
    private ContextScopedProvider<ItemsListServerCommunicationTask> itemsListServerCommunicationTaskProvider;

    private GenericServerCommunicationTask.CommunicationStatusHandler<ItemsListResponse> itemsListStatusHandler = new GenericServerCommunicationTask.CommunicationStatusHandler<ItemsListResponse>() {
        @Override
        public void onPreExecute() throws Exception {

        }

        @Override
        public void onSuccess(ItemsListResponse itemsListResponse) throws Exception {

        }

        @Override
        public void onThrowable(Throwable t) throws RuntimeException {
            Toast.makeText(getActivity(), getString(R.string.error_items_list), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onFinally() throws RuntimeException {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<Item> items = null;
        try {
            items = couchbaseLiteHelper.getItemsList();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            items = new ArrayList<>();
        }
        AndroidTreeView tView = new AndroidTreeView(getActivity(), treeCreator.createTree(items));

        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.inventory_fragment_content);
        linearLayout.addView(tView.getView());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ItemsListServerCommunicationTask itemsListServerCommunicationTask = itemsListServerCommunicationTaskProvider.get(getActivity());
        itemsListServerCommunicationTask.setStatusHandler(itemsListStatusHandler);
        itemsListServerCommunicationTask.updateItems();
    }
}
