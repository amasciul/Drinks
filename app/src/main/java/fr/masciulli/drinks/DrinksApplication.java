package fr.masciulli.drinks;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public class DrinksApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
