package fr.masciulli.drinks.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fr.masciulli.drinks.R;

public class DrinkDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
    }
}
