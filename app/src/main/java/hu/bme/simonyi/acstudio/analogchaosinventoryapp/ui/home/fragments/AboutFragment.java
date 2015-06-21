package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

/**
 * Fragment for displaying basic info about the application.
 *
 * @author Lajos Nyeki
 */
public class AboutFragment extends RoboFragment {

    public static final int TITLE_ID = R.string.navigation_about;
    public static final String TAG = AboutFragment.class.getSimpleName();

    @InjectView(R.id.about_version)
    private TextView versionTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        versionTextView.setText(getString(R.string.about_version_text) + " " + getString(R.string.appversion));
    }
}
