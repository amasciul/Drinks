package fr.masciulli.drinks;

import android.app.Application;
import android.content.Context;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import fr.masciulli.drinks.net.Client;

public class DrinksApplication extends Application {
    private Client client;

    @Override
    public void onCreate() {
        super.onCreate();
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        Stetho.initializeWithDefaults(this);

        client = new Client(this);
    }

    public Client getClient() {
        return client;
    }

    public static DrinksApplication get(Context context) {
        return (DrinksApplication) context.getApplicationContext();
    }
}
