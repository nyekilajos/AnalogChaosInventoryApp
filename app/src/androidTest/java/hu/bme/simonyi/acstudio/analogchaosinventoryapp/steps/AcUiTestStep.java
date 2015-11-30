package hu.bme.simonyi.acstudio.analogchaosinventoryapp.steps;

import android.content.Context;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObjectNotFoundException;

/**
 * Interface for defining a common format of the executable ui test steps.
 * 
 * @author Lajos_Nyeki
 */
public interface AcUiTestStep {

    /**
     * Runs the test step.
     *
     * @param context The Android context, mainly for creating Intents in this case.
     * @param uiDevice With this, UI objects can be found and interactions can be made with them.
     * @throws UiObjectNotFoundException Throe if a UI object not found but expected.
     */
    void runTestStep(Context context, UiDevice uiDevice) throws UiObjectNotFoundException;

}
