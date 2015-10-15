package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments;

import java.util.ArrayList;
import java.util.List;

import roboguice.fragment.RoboFragment;
import roboguice.inject.ContextScopedProvider;

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

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.CouchbaseLiteHelper;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.database.Item;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.inventory.TreeCreator;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.Logger;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.log.LoggerFactory;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.GenericServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ItemsListServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.SimpleStatusHandler;

/**
 * Fragment for displaying the inventory list of the AC Studio & Live
 *
 * @author Lajos Nyeki
 */
public class InventoryFragment extends RoboFragment {

    public static final int TITLE_ID = R.string.navigation_inventory;
    public static final String TAG = InventoryFragment.class.getSimpleName();

    private static final Logger LOGGER = LoggerFactory.createLogger(InventoryFragment.class);
    private static final String SAVE_STATE_TREE_VIEW = "saveStateTreeView";

    @Inject
    private TreeCreator treeCreator;
    @Inject
    private CouchbaseLiteHelper couchbaseLiteHelper;

    private LinearLayout contentRootLayout;
    private AndroidTreeView treeView;

    @Inject
    private ContextScopedProvider<ItemsListServerCommunicationTask> itemsListServerCommunicationTaskProvider;

    private GenericServerCommunicationTask.CommunicationStatusHandler<ItemsListResponse> itemsListStatusHandler = new SimpleStatusHandler<ItemsListResponse>() {

        @Override
        public void onSuccess(ItemsListResponse itemsListResponse) throws Exception {
            if (isVisible()) {
                refreshTreeView();
            }
        }

        @Override
        public void onThrowable(Throwable t) throws RuntimeException {
            if (isVisible()) {
                Toast.makeText(getActivity(), getString(R.string.error_items_list), Toast.LENGTH_LONG).show();
            }
        }

    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);
        contentRootLayout = (LinearLayout) view.findViewById(R.id.inventory_fragment_content);
        refreshTreeView();
        restoreTreeViewState(savedInstanceState);
        updateLocalDatabase();
        return view;
    }

    private void restoreTreeViewState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            treeView.restoreState(savedInstanceState.getString(SAVE_STATE_TREE_VIEW));
        }
    }

    private List<Item> getItemsFromDatabase() {
        List<Item> items;
        try {
            items = couchbaseLiteHelper.getItemsList();
        } catch (CouchbaseLiteException e) {
            LOGGER.error(e.getMessage());
            items = new ArrayList<>();
        }
        return items;
    }

    private void refreshTreeView() {
        String savedTreeViewState = null;
        if (treeView != null) {
            savedTreeViewState = treeView.getSaveState();
        }
        treeView = new AndroidTreeView(getActivity(), treeCreator.createTree(getItemsFromDatabase()));
        treeView.setDefaultAnimation(true);
        if (savedTreeViewState != null) {
            treeView.restoreState(savedTreeViewState);
        }
        contentRootLayout.removeAllViews();
        contentRootLayout.addView(treeView.getView());
    }

    private void updateLocalDatabase() {
        ItemsListServerCommunicationTask itemsListServerCommunicationTask = itemsListServerCommunicationTaskProvider
                .get(getActivity().getApplicationContext());
        itemsListServerCommunicationTask.setStatusHandler(itemsListStatusHandler);
        itemsListServerCommunicationTask.updateItems();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVE_STATE_TREE_VIEW, treeView.getSaveState());
    }
}
