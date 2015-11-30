package hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.Until;

/**
 * Test step for starting the app.
 * 
 * @author Lajos_Nyeki
 */
public class StartAppTestStep implements AcUiTestStep {
    @Override
    public void runTestStep(Context context, UiDevice uiDevice) throws UiObjectNotFoundException {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(ComponentName.unflattenFromString(
                "hu.bme.simonyi.acstudio.analogchaosinventoryapp/hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.login.LoginActivity"));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        uiDevice.wait(Until.hasObject(By.pkg("hu.bme.simonyi.acstudio.analogchaosinventoryapp")), 10000);
        uiDevice.waitForIdle();
    }
}
