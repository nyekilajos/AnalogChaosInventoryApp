package hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings;

/**
 * Service to save and load local settings for the app
 *
 * @author Lajos Nyeki
 */
public interface LocalSettingsService {

    String getEmailAddress();

    void setEmailAddress(String emailAddress);

}
