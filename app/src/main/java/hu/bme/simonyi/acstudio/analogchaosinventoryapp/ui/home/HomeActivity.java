package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.ScanFragment;
import roboguice.activity.RoboActionBarActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Home Activity which hosts the fragments of the main features.
 *
 * @author Lajos Nyeki
 */
public class HomeActivity extends RoboActionBarActivity {

    private NavigationDrawerFragment drawerFragment;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_home);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setDrawerItemSelectedListener(new NavigationDrawerFragment.DrawerItemSelectedListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                swapFragment(NavigationDrawerDataProvider.getDrawerItemForPosition(position).getOpeningFragmentTag());
            }
        });
        swapFragment(ScanFragment.TAG);
    }

    private void swapFragment(String openingFragmentTag) {
        RoboFragment fragment = HomeScreenFragmentFactory.getFragmentByTag(openingFragmentTag);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
        getSupportActionBar().setTitle(openingFragmentTag);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

}
