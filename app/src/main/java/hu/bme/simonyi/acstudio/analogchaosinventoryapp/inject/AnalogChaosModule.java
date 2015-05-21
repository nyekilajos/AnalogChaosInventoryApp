package hu.bme.simonyi.acstudio.analogchaosinventoryapp.inject;

import com.google.inject.AbstractModule;

import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.LocalSettingsService;
import hu.bme.simonyi.acstudio.analogchaosinventoryapp.settings.SharedPreferencesHelper;
import roboguice.inject.ContextSingleton;

/**
 * Main RoboGuice module class
 *
 * @author Lajos Nyeki
 */
public class AnalogChaosModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LocalSettingsService.class).to(SharedPreferencesHelper.class).in(ContextSingleton.class);
    }
}
