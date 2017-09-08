package fr.masciulli.drinks.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.core.Drink;
import fr.masciulli.drinks.ui.EnterPostponeTransitionCallback;

public class DrinkActivity extends AppCompatActivity {
    public static final String EXTRA_DRINK = "extra_drink";
    private static final boolean TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    private Drink drink;

    private ImageView imageView;
    private TextView historyView;
    private TextView instructionsView;
    private TextView ingredientsView;
    private Button wikipediaButton;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TRANSITIONS_AVAILABLE) {
            postponeEnterTransition();
        }

        drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setContentView(R.layout.activity_drink);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(drink.getName());

        imageView = findViewById(R.id.image);
        historyView = findViewById(R.id.history);
        instructionsView = findViewById(R.id.instructions);
        ingredientsView = findViewById(R.id.ingredients);
        wikipediaButton = findViewById(R.id.wikipedia);

        setupViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupViews() {
        Picasso.with(this)
                .load(drink.getImageUrl())
                .noFade()
                .into(imageView, new EnterPostponeTransitionCallback(this));

        historyView.setText(drink.getHistory());
        instructionsView.setText(drink.getInstructions());
        ingredientsView.setText(parseIngredients());
        wikipediaButton.setText(getString(R.string.wikipedia, drink.getName()));
        wikipediaButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(drink.getWikipedia()));
            startActivity(intent);
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return Html.fromHtml(builder.toString());
        } else {
            return Html.fromHtml(builder.toString(), 0);
        }
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
