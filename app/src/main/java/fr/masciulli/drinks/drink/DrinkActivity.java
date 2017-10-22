package fr.masciulli.drinks.drink;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.DrinksApplication;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.core.drinks.Drink;
import fr.masciulli.drinks.core.drinks.DrinksSource;
import fr.masciulli.drinks.ui.EnterPostponeTransitionCallback;
import io.reactivex.disposables.Disposable;

public class DrinkActivity extends AppCompatActivity {
    private static final String TAG = DrinkActivity.class.getSimpleName();

    public static final String EXTRA_DRINK_ID = "extra_drink_id";
    private static final boolean TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

    private ImageView imageView;
    private TextView historyView;
    private TextView instructionsView;
    private TextView ingredientsView;
    private Button wikipediaButton;

    private DrinkViewModel viewModel;

    private Disposable drinkDisposable;
    private Disposable errorDisposable;
    private Disposable shareDrinkDisposable;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TRANSITIONS_AVAILABLE) {
            postponeEnterTransition();
        }
        setContentView(R.layout.activity_drink);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViews();

        DrinksSource drinksSource = DrinksApplication.get(this).getDrinksSource();
        viewModel = new DrinkViewModel(drinksSource, getIntent().getStringExtra(EXTRA_DRINK_ID));
        viewModel.start();

        drinkDisposable = viewModel.getDrink().subscribe(this::showDrink);
        errorDisposable = viewModel.getError().subscribe(this::showError);
        shareDrinkDisposable = viewModel.getShareDrink().subscribe(this::showShareDrink);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupViews() {
        imageView = findViewById(R.id.image);
        historyView = findViewById(R.id.history);
        instructionsView = findViewById(R.id.instructions);
        ingredientsView = findViewById(R.id.ingredients);
        wikipediaButton = findViewById(R.id.wikipedia);
    }

    private void showDrink(@NonNull Drink drink) {
        setTitle(drink.getName());
        Picasso.with(this)
                .load(drink.getImageUrl())
                .noFade()
                .into(imageView, new EnterPostponeTransitionCallback(this));

        historyView.setText(drink.getHistory());
        instructionsView.setText(drink.getInstructions());
        ingredientsView.setText(parseIngredients(drink));
        wikipediaButton.setText(getString(R.string.wikipedia, drink.getName()));
        wikipediaButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(drink.getWikipedia()));
            startActivity(intent);
        });
    }

    public void showError(Throwable throwable) {
        //TODO show error
        Log.d(TAG, "Error loading drink", throwable);
        Toast.makeText(this, "Error loading drink", Toast.LENGTH_LONG).show();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Spanned parseIngredients(Drink drink) {
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
            viewModel.openShareDrink();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showShareDrink(@NonNull Drink drink) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        sendIntent.putExtra(Intent.EXTRA_TEXT, parseIngredients(drink));
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    protected void onDestroy() {
        viewModel.stop();

        drinkDisposable.dispose();
        errorDisposable.dispose();
        shareDrinkDisposable.dispose();

        super.onDestroy();
    }
}
