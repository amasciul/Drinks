package fr.masciulli.drinks;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import fr.masciulli.drinks.data.DrinksProvider;

public class DrinksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String apiPrefKey = getString(R.string.pref_apiendpoint_key);
        String versionCodePrefKey = getString(R.string.pref_versioncode_key);
        String apiDefaultValue = getString(R.string.pref_apiendpoint_default);

        String server;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            int lastVersionCode = PreferenceManager.getDefaultSharedPreferences(this).getInt(versionCodePrefKey, 0);
            if (lastVersionCode < versionCode) {
                // Update detected
                server = apiDefaultValue;
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putInt(versionCodePrefKey, versionCode);
                editor.putString(apiPrefKey, apiDefaultValue);
                editor.commit();
            } else {
                server = PreferenceManager.getDefaultSharedPreferences(this).getString(apiPrefKey, apiDefaultValue);
            }
        } catch (PackageManager.NameNotFoundException e) {
            server = apiDefaultValue;
        }

        DrinksProvider.updateServer(server);
    }
}
