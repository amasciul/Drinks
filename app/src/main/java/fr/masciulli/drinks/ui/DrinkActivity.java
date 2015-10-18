package fr.masciulli.drinks.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRINK = "extra_drink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drink drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setTitle(drink.name);
    }
}
