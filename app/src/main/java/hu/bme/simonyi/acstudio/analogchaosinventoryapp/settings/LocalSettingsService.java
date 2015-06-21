package hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings;

import java.util.Set;

/**
 * Service to save and load local settings for the app
 *
 * @author Lajos Nyeki
 */
public interface LocalSettingsService {

    void reset();

    String getEmailAddress();

    void setEmailAddress(String emailAddress);

    String getPassword();

    void setPassword(String password);

    String getSessionCode();

    void setSessionCode(String sessionCode);

    Set<String> getItemsCouchbaseLiteDocumentIds();

    void setItemsCouchbaseLiteDocumentIds(Set<String> documentIds);
}
