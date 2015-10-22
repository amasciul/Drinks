package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    private static final String TAG = DrinkActivity.class.getSimpleName();

    public static final String EXTRA_DRINK = "extra_drink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drink drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setContentView(R.layout.activity_drink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(drink.name);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView historyView = (TextView) findViewById(R.id.history);

        Picasso.with(this).load(drink.imageUrl).into(imageView);
        historyView.setText(drink.history);
    }
}
