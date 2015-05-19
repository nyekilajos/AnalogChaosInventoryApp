package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

/**
 * DTO for the navigation drawer items
 *
 * @author Lajos Nyeki
 */
public class DrawerItem {

    private String openingFragmentTag;
    private int titleId;

    public DrawerItem(String openingFragmentTag, int titleId) {
        this.openingFragmentTag = openingFragmentTag;
        this.titleId = titleId;
    }

    public String getOpeningFragmentTag() {
        return openingFragmentTag;
    }

    public void setOpeningFragmentTag(String openingFragmentTag) {
        this.openingFragmentTag = openingFragmentTag;
    }

    public int getTitleId() {
        return titleId;
    }

    public void setTitleId(int titleId) {
        this.titleId = titleId;
    }
}
