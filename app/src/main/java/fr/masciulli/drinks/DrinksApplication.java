package fr.masciulli.drinks;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import fr.masciulli.drinks.core.Client;
import fr.masciulli.drinks.core.DrinksSource;
import fr.masciulli.drinks.core.LiquorsSource;

public class DrinksApplication extends Application {
    private DrinksSource drinksSource;
    private LiquorsSource liquorsSource;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Client client = new Client(BuildConfig.SERVER_URL);
        drinksSource = client;
        liquorsSource = client;
    }

    public DrinksSource getDrinksSource() {
        return drinksSource;
    }

    public LiquorsSource getLiquorsSource() {
        return liquorsSource;
    }

    public static DrinksApplication get(Context context) {
        return (DrinksApplication) context.getApplicationContext();
    }
}
