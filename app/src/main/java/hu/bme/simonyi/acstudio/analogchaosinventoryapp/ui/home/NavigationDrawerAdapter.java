package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;

import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;

/**
 * Adapter for the RecyclerView of the navigation drawer fragment.
 *
 * @author Lajos Nyeki
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.DrawerItemHolder> {

    private List<DrawerItem> navigationList;
    private LayoutInflater inflater;
    private Context context;

    @Inject
    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        navigationList = NavigationDrawerDataProvider.getDrawerItems();
    }

    @Override
    public DrawerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.navigation_drawer_item, parent, false);
        return new DrawerItemHolder(view);
    }

    @Override
    public void onBindViewHolder(DrawerItemHolder holder, int position) {
        DrawerItem current = navigationList.get(position);

        holder.title.setText(context.getString(current.getTitleId()));
    }

    @Override
    public int getItemCount() {
        return navigationList.size();
    }

    static class DrawerItemHolder extends RecyclerView.ViewHolder {

        TextView title;

        public DrawerItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.drawer_item_title);
        }
    }
}
