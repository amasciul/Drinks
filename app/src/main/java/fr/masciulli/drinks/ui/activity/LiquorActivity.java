package fr.masciulli.drinks.ui.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import fr.masciulli.drinks.DrinksApplication;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.core.drinks.Drink;
import fr.masciulli.drinks.core.liquors.Liquor;
import fr.masciulli.drinks.drink.DrinkActivity;
import fr.masciulli.drinks.ui.EnterPostponeTransitionCallback;
import fr.masciulli.drinks.ui.adapter.LiquorRelatedAdapter;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LiquorActivity extends AppCompatActivity {
    private static final boolean TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    public static final String EXTRA_LIQUOR_ID = "extra_liquor_id";

    private Liquor liquor;
    private LiquorRelatedAdapter adapter;

    private RecyclerView recyclerView;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor);

        if (TRANSITIONS_AVAILABLE) {
            postponeEnterTransition();
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String liquorId = getIntent().getStringExtra(EXTRA_LIQUOR_ID);
        loadLiquor(liquorId);
    }

    private void loadLiquor(String liquorId) {
        DrinksApplication.get(this).getLiquorsSource()
                .getLiquor(liquorId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::liquorRetrieved, this::errorRetrievingLiquor);
    }

    private void errorRetrievingLiquor(Throwable throwable) {
        //TODO handle
        Timber.e(throwable, "Error retrieving liquor");
    }

    private void liquorRetrieved(Liquor liquor) {
        this.liquor = liquor;
        setupViews();
        loadDrinks();
    }

    private void setupViews() {
        setTitle(liquor.getName());

        ImageView imageView = findViewById(R.id.image);
        Picasso.with(this)
                .load(liquor.getImageUrl())
                .noFade()
                .into(imageView, new EnterPostponeTransitionCallback(this));

        recyclerView = findViewById(R.id.recycler);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new LiquorRelatedAdapter();
        adapter.setLiquor(liquor);
        adapter.setWikipediaClickListener((position, liquor) -> onWikipediaClick());

        adapter.setDrinkClickListener(this::onDrinkClick);

        recyclerView.setLayoutManager(adapter.craftLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void onWikipediaClick() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(liquor.getWikipedia()));
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onDrinkClick(int position, Drink drink) {
        Intent intent = new Intent(this, DrinkActivity.class);
        //TODO add drink id to intent
        if (TRANSITIONS_AVAILABLE) {
            TileViewHolder holder = (TileViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            String transition = getString(R.string.transition_drink);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, holder.getImageView(), transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    private void loadDrinks() {
        DrinksApplication.get(this).getDrinksSource()
                .getDrinks()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .filter(this::matches)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::drinksRetrieved, this::errorRetrievingDrinks);
    }

    private void drinksRetrieved(List<Drink> drinks) {
        adapter.setRelatedDrinks(drinks);
    }

    private void errorRetrievingDrinks(Throwable throwable) {
        Timber.e(throwable, "Couldn't retrieve liquors");
    }

    private boolean matches(Drink drink) {
        for (String ingredient : drink.getIngredients()) {
            String lowerCaseIngredient = ingredient.toLowerCase(Locale.US);
            if (lowerCaseIngredient.contains(liquor.getName().toLowerCase(Locale.US))) {
                return true;
            }
            for (String name : liquor.getOtherNames()) {
                if (lowerCaseIngredient.contains(name.toLowerCase(Locale.US))) {
                    return true;
                }
            }
        }

        return false;
    }
}
