package hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps;

import android.content.Context;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.widget.ImageView;

/**
 * Test step for log out.
 * 
 * @author Lajos_Nyeki
 */
public class LogoutTestStep implements AcUiTestStep {

    private UiDevice uiDevice;

    @Override
    public void runTestStep(Context context, UiDevice uiDevice) throws UiObjectNotFoundException {
        this.uiDevice = uiDevice;
        logout();

    }

    private void logout() throws UiObjectNotFoundException {
        uiDevice.findObject(new UiSelector().className(ImageView.class).index(0)).click();
        uiDevice.wait(Until.hasObject(By.res("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/title")), 2000);

        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/title")).click();
        uiDevice.wait(Until.hasObject(By.text("Login")), 5000);
    }
}
