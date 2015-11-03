package fr.masciulli.drinks.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    private static final String TAG = DrinkActivity.class.getSimpleName();

    public static final String EXTRA_DRINK = "extra_drink";
    private Drink drink;

    private ImageView imageView;
    private TextView historyView;
    private TextView instructionsView;
    private TextView ingredientsView;
    private Button wikipediaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setContentView(R.layout.activity_drink);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(drink.getName());

        imageView = (ImageView) findViewById(R.id.image);
        historyView = (TextView) findViewById(R.id.history);
        instructionsView = (TextView) findViewById(R.id.instructions);
        ingredientsView = (TextView) findViewById(R.id.ingredients);
        wikipediaButton = (Button) findViewById(R.id.wikipedia);

        setupViews();
    }

    private void setupViews() {
        Picasso.with(this).load(drink.getImageUrl()).into(imageView);
        historyView.setText(drink.getHistory());
        instructionsView.setText(drink.getInstructions());
        ingredientsView.setText(parseIngredients());
        wikipediaButton.setText(getString(R.string.wikipedia, drink.getName()));
        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(drink.getWikipedia()));
                startActivity(intent);
            }
        });
    }

    private Spanned parseIngredients() {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String ingredient : drink.getIngredients()) {
            builder.append("&#8226; ");
            builder.append(ingredient);
            if (++i < drink.getIngredients().size()) {
                builder.append("<br>");
            }
        }
        return Html.fromHtml(builder.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drink, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareDrink();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareDrink() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        sendIntent.putExtra(Intent.EXTRA_TEXT, parseIngredients());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}
