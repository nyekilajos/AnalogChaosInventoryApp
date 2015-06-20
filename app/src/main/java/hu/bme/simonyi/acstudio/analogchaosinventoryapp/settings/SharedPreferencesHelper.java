package hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.inject.Inject;

import java.util.HashSet;
import java.util.Set;

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
    private static final String USER_PASSWORD = "UserPassword";
    private static final String USER_SESSION_CODE = "UserSessionCode";

    private static final String DATABASE_DATA_SETTINGS = "DatabaseDataSettings";

    private static final String COUCHBASE_ITEMS_DOC_ID = "ItemsCouchbaseItemsId";

    private SharedPreferences userDataSettings;
    private SharedPreferences databaseDataSettings;

    @Inject
    public SharedPreferencesHelper(Context context) {
        userDataSettings = context.getSharedPreferences(USER_DATA_SETTINGS, Context.MODE_MULTI_PROCESS);
        databaseDataSettings = context.getSharedPreferences(DATABASE_DATA_SETTINGS, Context.MODE_MULTI_PROCESS);
    }

    @Override
    public void reset() {
        userDataSettings.edit().clear().apply();
    }

    @Override
    public String getEmailAddress() {
        return userDataSettings.getString(USER_EMAIL_ADDRESS, "");
    }

    @Override
    public void setEmailAddress(String emailAddress) {
        userDataSettings.edit().putString(USER_EMAIL_ADDRESS, emailAddress).apply();
    }

    @Override
    public String getPassword() {
        return userDataSettings.getString(USER_PASSWORD, "");
    }

    @Override
    public void setPassword(String password) {
        userDataSettings.edit().putString(USER_PASSWORD, password).apply();
    }

    @Override
    public String getSessionCode() {
        return userDataSettings.getString(USER_SESSION_CODE, "");
    }

    @Override
    public void setSessionCode(String sessionCode) {
        userDataSettings.edit().putString(USER_SESSION_CODE, sessionCode).apply();
    }

    @Override
    public Set<String> getItemsCouchbaseLiteDocumentIds() {
        return databaseDataSettings.getStringSet(COUCHBASE_ITEMS_DOC_ID, new HashSet<String>());
    }

    @Override
    public void setItemsCouchbaseLiteDocumentIds(Set<String> documentIds) {
        databaseDataSettings.edit().putStringSet(COUCHBASE_ITEMS_DOC_ID, documentIds).apply();
    }
}
