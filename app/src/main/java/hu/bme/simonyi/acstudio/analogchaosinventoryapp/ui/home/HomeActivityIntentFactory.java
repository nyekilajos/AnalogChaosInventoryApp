package hu.bme.simonyi.acstudio.analogchaosinventoryapp.ui.home;

import android.content.Context;
import android.content.Intent;

/**
 * Class to create intents to HomeActivity
 *
 * @author Lajos Nyeki
 */
public class HomeActivityIntentFactory {

    private HomeActivityIntentFactory() {

    }

    /**
     * Creates basic HomeActivity Intent.
     *
     * @param context Android context.
     * @return Intent to poen HomeActivity.
     */
    public static Intent createHomeActivityIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }
}
