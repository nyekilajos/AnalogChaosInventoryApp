package hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps;

import android.content.Context;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

/**
 * Test step to log in.
 * 
 * @author Lajos_Nyeki
 */
public class LoginTestStep implements AcUiTestStep {

    private final String email;
    private final String pass;

    private UiDevice uiDevice;

    /**
     * Constructor.
     *
     * @param email Email address to log in.
     * @param pass Password to use for login.
     */
    public LoginTestStep(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    @Override
    public void runTestStep(Context context, UiDevice uiDevice) throws UiObjectNotFoundException {
        this.uiDevice = uiDevice;
        login();
    }

    private void login() throws UiObjectNotFoundException {
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/edit_email_login")).setText(email);
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/edit_password_login")).setText(pass);
        uiDevice.findObject(new UiSelector().resourceId("hu.bme.simonyi.acstudio.analogchaosinventoryapp:id/btn_login"))
                .clickAndWaitForNewWindow(5000);
    }
}
