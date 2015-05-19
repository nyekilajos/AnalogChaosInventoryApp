package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import java.util.ArrayList;
import java.util.List;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.AboutFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.HelpAndTipsFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.InventoryFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.RentalHistoryFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.ScanFragment;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.fragments.SettingsFragment;

/**
 * Data provider for the navigation drawer. Creates the DTO class list.
 *
 * @author Lajos Nyeki
 */
public class NavigationDrawerDataProvider {

    private static List<DrawerItem> drawerItems;

    /**
     * Private constructor. Class can not be instantiated.
     */
    private NavigationDrawerDataProvider() {

    }

    /**
     * Returns the DrawerItem listed used for the navigation drawer.
     *
     * @return
     */
    public static List<DrawerItem> getDrawerItems() {
        if (drawerItems == null) {
            initDrawerList();
        }
        return drawerItems;
    }

    /**
     * Returns the navigation drawer DTO object by its position in the list.
     *
     * @param position Position of the DTO object in the list.
     * @return The DTO object.
     */
    public static DrawerItem getDrawerItemForPosition(int position) {
        if (drawerItems == null) {
            initDrawerList();
        }
        return drawerItems.get(position);
    }

    private static void initDrawerList() {
        drawerItems = new ArrayList<>();
        drawerItems.add(new DrawerItem(ScanFragment.TAG, ScanFragment.TITLE_ID));
        drawerItems.add(new DrawerItem(InventoryFragment.TAG, InventoryFragment.TITLE_ID));
        drawerItems.add(new DrawerItem(RentalHistoryFragment.TAG, RentalHistoryFragment.TITLE_ID));
        drawerItems.add(new DrawerItem(SettingsFragment.TAG, SettingsFragment.TITLE_ID));
        drawerItems.add(new DrawerItem(HelpAndTipsFragment.TAG, HelpAndTipsFragment.TITLE_ID));
        drawerItems.add(new DrawerItem(AboutFragment.TAG, AboutFragment.TITLE_ID));
    }
}
