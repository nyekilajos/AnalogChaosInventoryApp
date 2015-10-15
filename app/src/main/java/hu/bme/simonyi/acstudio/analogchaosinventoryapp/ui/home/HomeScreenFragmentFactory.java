package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import roboguice.fragment.RoboFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.AboutFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.HelpAndTipsFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.InventoryFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.RentalHistoryFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.ScanFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.SettingsFragment;

/**
 * Factory for the home screen fragments
 *
 * @author Lajos Nyeki
 */
public class HomeScreenFragmentFactory {

    private HomeScreenFragmentFactory() {

    }

    /**
     * Method to create a fragment by its tag.
     *
     * @param tag Tag of the fragment to be created.
     * @return Created fragment.
     */
    public static RoboFragment getFragmentByTag(String tag) {
        RoboFragment fragment;
        if (AboutFragment.TAG.equals(tag)) {
            fragment = new AboutFragment();
        } else if (HelpAndTipsFragment.TAG.equals(tag)) {
            fragment = new HelpAndTipsFragment();
        } else if (InventoryFragment.TAG.equals(tag)) {
            fragment = new InventoryFragment();
        } else if (RentalHistoryFragment.TAG.equals(tag)) {
            fragment = new RentalHistoryFragment();
        } else if (SettingsFragment.TAG.equals(tag)) {
            fragment = new SettingsFragment();
        } else {
            fragment = new ScanFragment();
        }
        return fragment;
    }
}
