package hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps;

import android.content.Context;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.widget.TextView;

/**
 * Test step for starting the given page on the main activity.
 * 
 * @author Lajos_Nyeki
 */
public class SwitchPageToTestStep implements AcUiTestStep {

    private final String pageTitle;

    public SwitchPageToTestStep(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    @Override
    public void runTestStep(Context context, UiDevice uiDevice) throws UiObjectNotFoundException {
        uiDevice.swipe(0, uiDevice.getDisplayHeight() / 2, uiDevice.getDisplayWidth(), uiDevice.getDisplayHeight() / 2, 40);
        uiDevice.wait(Until.hasObject(By.clazz(TextView.class).text(pageTitle)), 2000);
        uiDevice.findObject(new UiSelector().className(TextView.class).text(pageTitle)).clickAndWaitForNewWindow(1500);
        uiDevice.wait(Until.hasObject(By.text("4")), 2000);
        if (!uiDevice.findObject(new UiSelector().text("4")).exists()) {
            throw new AssertionError("Inventory item for idf \"4\" was not shown.");
        }
    }
}
