package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.google.inject.Inject;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.dto.LogoutResponse;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.GenericServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.net.task.LogoutServerCommunicationTask;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.dialog.DialogFactory;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.ScanFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.login.LoginActivity;
import roboguice.activity.RoboActionBarActivity;
import roboguice.fragment.RoboFragment;
import roboguice.inject.ContextScopedProvider;
import roboguice.inject.InjectView;

/**
 * Home Activity which hosts the fragments of the main features.
 *
 * @author Lajos Nyeki
 */
public class HomeActivity extends RoboActionBarActivity {

    private ActionBarDrawerToggle drawerToggle;

    @InjectView(R.id.drawer_layout)
    private DrawerLayout drawerLayout;

    @Inject
    private DialogFactory dialogFactory;

    @Inject
    private ContextScopedProvider<LogoutServerCommunicationTask> logoutServerCommunicationTaskContextScopedProvider;

    private GenericServerCommunicationTask.CommunicationStatusHandler<LogoutResponse> statusHandler = new GenericServerCommunicationTask.CommunicationStatusHandler<LogoutResponse>() {
        @Override
        public void onPreExecute() throws Exception {
            setSupportProgressBarIndeterminateVisibility(true);
        }

        @Override
        public void onSuccess(LogoutResponse logoutResponse) throws Exception {
            if (logoutResponse.isSuccess()) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                dialogFactory.createAlertDialog("Logout error, please try again later.");
            }
        }

        @Override
        public void onThrowable(Throwable t) throws RuntimeException {
            dialogFactory.createAlertDialog(getString(R.string.unknown_communication_error)).show();
        }

        @Override
        public void onFinally() throws RuntimeException {
            setSupportProgressBarIndeterminateVisibility(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_home);
        initDrawer();
        swapFragment(ScanFragment.TAG, ScanFragment.TITLE_ID);
    }

    private void initDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer_content_desc, R.string.close_drawer_content_desc);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled;
        if (item.getItemId() == R.id.menu_logout) {
            LogoutServerCommunicationTask logoutTask = logoutServerCommunicationTaskContextScopedProvider.get(this);
            logoutTask.setStatusHandler(statusHandler);
            logoutTask.logout();
            handled = true;
        } else {
            handled = drawerToggle.onOptionsItemSelected(item);
        }
        return handled || super.onOptionsItemSelected(item);
    }

    private void swapFragment(String openingFragmentTag, int titleId) {
        RoboFragment fragment = HomeScreenFragmentFactory.getFragmentByTag(openingFragmentTag);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_body, fragment).commit();
        getSupportActionBar().setTitle(getString(titleId));
        drawerLayout.closeDrawer(Gravity.LEFT);
    }
}
