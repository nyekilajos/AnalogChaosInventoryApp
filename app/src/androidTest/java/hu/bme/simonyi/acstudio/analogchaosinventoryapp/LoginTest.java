package hu.bme.simonyi.acstudio.analogchaosinventoryapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home.HomeActivity;

/**
 * Created by Lajos_Nyeki on 10/15/2015.
 */
public class LoginTest extends InstrumentationTestCase {

    private UiDevice uiDevice;
    private Context context;

    public void setUp() {
        context = getInstrumentation().getContext();
        uiDevice = UiDevice.getInstance(getInstrumentation());

        uiDevice.pressHome();
        uiDevice.wait(Until.hasObject(By.pkg(uiDevice.getLauncherPackageName())), 2000);
    }

    public void testLoginLogout() throws UiObjectNotFoundException {
        startAcApp();
        login();
        logout();
    }

    private void startAcApp() {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setComponent(ComponentName.unflattenFromString(
                "hu.bme.simonyi.acstudio.analogchaosinventoryapp/hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.login.LoginActivity"));
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

        uiDevice.wait(Until.hasObject(By.pkg("hu.bme.simonyi.acstudio.analogchaosinventoryapp")), 10000);
        uiDevice.waitForIdle();
    }

    private void login() throws UiObjectNotFoundException {
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/edit_email_login"))
                .setText("nyekilajos@gmail.com");
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/edit_password_login"))
                .setText("NyL900211");
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/btn_login")).click();

        uiDevice.wait(Until.hasObject(By.clazz(HomeActivity.class)), 10000);
    }

    private void logout() throws UiObjectNotFoundException {
        uiDevice.findObject(new UiSelector().descriptionContains("More options")).click();
        uiDevice.wait(Until.hasObject(By.text("Logout")), 2000);

        uiDevice.findObject(new UiSelector().text("Logout")).click();
        uiDevice.wait(Until.hasObject(By.text("Login")), 5000);
    }

}
