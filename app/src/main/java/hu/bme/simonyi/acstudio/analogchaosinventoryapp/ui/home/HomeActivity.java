package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
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
    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initDrawer();
        swapFragment(ScanFragment.TAG, ScanFragment.TITLE_ID);
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer_content_desc, R.string.close_drawer_content_desc);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setDrawerItemSelectedListener(new NavigationDrawerFragment.DrawerItemSelectedListener() {
            @Override
            public void onDrawerItemSelected(View view, int position) {
                DrawerItem drawerItem = NavigationDrawerDataProvider.getDrawerItemForPosition(position);
                swapFragment(drawerItem.getOpeningFragmentTag(), drawerItem.getTitleId());
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    private void swapFragment(String openingFragmentTag, int titleId) {
        RoboFragment fragment = HomeScreenFragmentFactory.getFragmentByTag(openingFragmentTag);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
        getSupportActionBar().setTitle(getString(titleId));
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

}
