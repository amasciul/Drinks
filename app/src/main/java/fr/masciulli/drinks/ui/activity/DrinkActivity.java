package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRINK = "extra_drink";

    private TextView historyView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drink drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setTitle(drink.name);
        setContentView(R.layout.activity_drink);

        imageView = (ImageView) findViewById(R.id.image);
        historyView = (TextView) findViewById(R.id.history);

        Picasso.with(this).load(drink.imageUrl).into(imageView);
        historyView.setText(drink.history);
    }
}
