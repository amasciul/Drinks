package fr.masciulli.drinks;

import android.app.Application;
import android.preference.PreferenceManager;

import fr.masciulli.drinks.data.DrinksProvider;

public class DrinksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String apiPrefKey = getString(R.string.pref_apiendpoint_key);
        String apiDefaultValue = getString(R.string.pref_apiendpoint_default);
        String server = PreferenceManager.getDefaultSharedPreferences(this).getString(apiPrefKey, apiDefaultValue);
        DrinksProvider.updateServer(server);
    }
}
