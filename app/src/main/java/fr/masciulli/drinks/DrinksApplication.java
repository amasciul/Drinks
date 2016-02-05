package fr.masciulli.drinks;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.picasso.Picasso;

public class DrinksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        Picasso picasso = Picasso.with(this);
        picasso.setLoggingEnabled(BuildConfig.DEBUG);
    }
}
