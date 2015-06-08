package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments;

import android.widget.Toast;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.ItemsListResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.GenericServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.ItemsListServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.dialog.DialogFactory;
import roboguice.activity.RoboActionBarActivity;
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
    private ContextScopedProvider<ItemsListServerCommunicationTask> itemsListServerCommunicationTaskProvider;

    private GenericServerCommunicationTask.CommunicationStatusHandler<ItemsListResponse> itemsListStatusHandler = new GenericServerCommunicationTask.CommunicationStatusHandler<ItemsListResponse>() {
        @Override
        public void onPreExecute() throws Exception {
            ((RoboActionBarActivity) getActivity()).setSupportProgressBarIndeterminateVisibility(true);
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
            ((RoboActionBarActivity) getActivity()).setSupportProgressBarIndeterminateVisibility(true);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        ItemsListServerCommunicationTask itemsListServerCommunicationTask = itemsListServerCommunicationTaskProvider.get(getActivity());
        itemsListServerCommunicationTask.setStatusHandler(itemsListStatusHandler);
        itemsListServerCommunicationTask.updateItems();
    }
}
