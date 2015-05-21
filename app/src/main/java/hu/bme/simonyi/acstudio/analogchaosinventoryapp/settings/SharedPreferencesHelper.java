package hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

import roboguice.inject.ContextSingleton;

/**
 * Helper class for saving and loading local settings to shared preferences.
 *
 * @author Lajos Nyeki
 */
@ContextSingleton
public class SharedPreferencesHelper implements LocalSettingsService {

    private static final String USER_DATA_SETTINGS = "UserInfoSettings";

    private static final String USER_EMAIL_ADDRESS = "UserName";

    private SharedPreferences userDataSettings;

    @Inject
    public SharedPreferencesHelper(Context context) {
        userDataSettings = context.getSharedPreferences(USER_DATA_SETTINGS, Context.MODE_MULTI_PROCESS);
    }

    @Override
    public String getEmailAddress() {
        return userDataSettings.getString(USER_EMAIL_ADDRESS, "");
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        userDataSettings.edit().putString(USER_EMAIL_ADDRESS, emailAddress).apply();
    }
}
