package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    private static final String TAG = DrinkActivity.class.getSimpleName();

    public static final String EXTRA_DRINK = "extra_drink";
    private Drink drink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setContentView(R.layout.activity_drink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(drink.name);

        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView historyView = (TextView) findViewById(R.id.history);
        TextView instructionsView = (TextView) findViewById(R.id.instructions);
        TextView ingredientsView = (TextView) findViewById(R.id.ingredients);

        Picasso.with(this).load(drink.imageUrl).into(imageView);
        historyView.setText(drink.history);
        instructionsView.setText(drink.instructions);
        ingredientsView.setText(parseIngredients());
    }

    private Spanned parseIngredients() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String ingredient : drink.ingredients) {
            builder.append("&#8226; ");
            builder.append(ingredient);
            if (++i < drink.ingredients.size()) {
                builder.append("<br>");
            }
        }
        return Html.fromHtml(builder.toString());
    }
}
