package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView versionView = (TextView) findViewById(R.id.version);
        versionView.setText(BuildConfig.VERSION_NAME);
    }
}
