package fr.masciulli.drinks;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

import fr.masciulli.drinks.core.Client;

public class DrinksApplication extends Application {
    private Client client;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        client = new Client();
    }

    public Client getClient() {
        return client;
    }

    public static DrinksApplication get(Context context) {
        return (DrinksApplication) context.getApplicationContext();
    }
}
