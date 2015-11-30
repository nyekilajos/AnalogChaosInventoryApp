package hu.bme.simonyi.acstudio.analogchaosinventoryapp;

import android.content.Context;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps.LoginTestStep;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps.LogoutTestStep;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps.StartAppTestStep;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps.SwitchPageToTestStep;

/**
 * Class for defining the UI test flow for the app.
 * 
 * @author Lajos_Nyeki
 */
public class AcAppUiTest extends InstrumentationTestCase {

    private static final String TEST_EMAIL = "nyekilajos@gmail.com";
    private static final String TEST_PASS = "N...1";

    private static final String NOT_VALID_EMAIL = "abc.hg.ljk";
    private static final String PASS = "aaa";

    private static final String VALID_EMAIL = "nyekilajos@gmail.com";
    private static final String WRONG_PASS = "111";

    private Context context;
    private UiDevice uiDevice;

    public void setUp() {
        context = getInstrumentation().getContext();
        uiDevice = UiDevice.getInstance(getInstrumentation());

        uiDevice.pressHome();
        uiDevice.wait(Until.hasObject(By.pkg(uiDevice.getLauncherPackageName())), 2000);
    }

    public void testApp() throws UiObjectNotFoundException {
        startApp();
        loginWithWrongPass();
        loginWithNotValidEmail();
        loginLogout();
        navigateToInventoryList();
    }

    private void startApp() throws UiObjectNotFoundException {
        new StartAppTestStep().runTestStep(context, uiDevice);
    }

    private void loginWithWrongPass() throws UiObjectNotFoundException {
        new LoginTestStep(VALID_EMAIL, WRONG_PASS).runTestStep(context, uiDevice);
        verifyLoginErrorMessageDisplayed();
    }

    private void loginWithNotValidEmail() throws UiObjectNotFoundException {
        new LoginTestStep(NOT_VALID_EMAIL, PASS).runTestStep(context, uiDevice);
        verifyLoginButtonVisible();
    }

    private void verifyLoginButtonVisible() throws UiObjectNotFoundException {
        uiDevice.wait(Until.hasObject(By.res("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/btn_login")), 2000);
        if (!uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/btn_login")).isClickable()) {
            throw new AssertionError("Login button should be visible.");
        }
    }

    private void verifyLoginErrorMessageDisplayed() throws UiObjectNotFoundException {
        uiDevice.wait(Until.hasObject(By.res("android:id/button3")), 2000);
        UiObject button = uiDevice.findObject(new UiSelector().resourceId("android:id/button3"));
        if (!button.exists()) {
            throw new AssertionError("Missing warning dialog or missing button on warning dialog.");
        } else {
            button.click();
            uiDevice.wait(Until.hasObject(By.res("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/btn_login")), 1000);
            verifyLoginButtonVisible();
        }
    }

    private void loginLogout() throws UiObjectNotFoundException {
        new LoginTestStep(TEST_EMAIL, TEST_PASS).runTestStep(context, uiDevice);
        new LogoutTestStep().runTestStep(context, uiDevice);
    }

    private void navigateToInventoryList() throws UiObjectNotFoundException {
        new StartAppTestStep().runTestStep(context, uiDevice);
        new LoginTestStep(TEST_EMAIL, TEST_PASS).runTestStep(context, uiDevice);
        new SwitchPageToTestStep("Eszközeink").runTestStep(context, uiDevice);
    }

}
