package fr.masciulli.drinks.ui.activity;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.DataProvider;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.LiquorRelatedAdapter;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LiquorActivity extends AppCompatActivity implements Callback<List<Drink>> {
    private static final String TAG = LiquorActivity.class.getSimpleName();

    public static final String EXTRA_LIQUOR = "extra_liquor";
    private static final String STATE_DRINKS = "state_drinks";

    private Liquor liquor;
    private DataProvider provider;
    private Call<List<Drink>> call;
    private LiquorRelatedAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liquor = getIntent().getParcelableExtra(EXTRA_LIQUOR);
        provider = new DataProvider(this);

        setContentView(R.layout.activity_liquor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(liquor.getName());

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this)
                .load(liquor.getImageUrl())
                .noFade()
                .into(imageView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setupRecyclerView();

        if (savedInstanceState == null) {
            loadDrinks();
        } else {
            List<Drink> drinks = savedInstanceState.getParcelableArrayList(STATE_DRINKS);
            onDrinksRetrieved(drinks);
        }
    }

    private void setupRecyclerView() {
        adapter = new LiquorRelatedAdapter();
        adapter.setLiquor(liquor);
        adapter.setWikipediaClickListener((position, liquor) -> onWikipediaClick());

        adapter.setDrinkClickListener(this::onDrinkClick);

        final int columnCount = getResources().getInteger(R.integer.column_count);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnCount);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case LiquorRelatedAdapter.TYPE_HEADER:
                        return columnCount;
                    case LiquorRelatedAdapter.TYPE_DRINK:
                        return 1;
                    default:
                        throw new IllegalArgumentException("Unknown view type");
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
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
        intent.putExtra(DrinkActivity.EXTRA_DRINK, drink);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        cancelPreviousCall();
        call = provider.getDrinks();
        call.enqueue(this);
    }

    private void cancelPreviousCall() {
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public void onResponse(Call<List<Drink>> call, Response<List<Drink>> response) {
        if (response.isSuccessful()) {
            onDrinksRetrieved(filterRelatedDrinks(response.body()));
        } else {
            Log.e(TAG, "Couldn't retrieve liquors : " + response.message());
        }
    }

    private void onDrinksRetrieved(List<Drink> drinks) {
        adapter.setRelatedDrinks(drinks);
    }

    @Override
    public void onFailure(Call<List<Drink>> call, Throwable t) {
        Log.d(TAG, "Couldn't load related drinks", t);
    }

    private List<Drink> filterRelatedDrinks(List<Drink> drinks) {
        List<Drink> related = new ArrayList<>();
        for (Drink drink : drinks) {
            for (String ingredient : drink.getIngredients()) {
                String lowerCaseIngredient = ingredient.toLowerCase(Locale.US);
                if (lowerCaseIngredient.contains(liquor.getName().toLowerCase(Locale.US))) {
                    related.add(drink);
                    break;
                }
                boolean matches = false;
                for (String name : liquor.getOtherNames()) {
                    if (lowerCaseIngredient.contains(name.toLowerCase(Locale.US))) {
                        related.add(drink);
                        matches = true;
                        break;
                    }
                }
                if (matches) {
                    break;
                }
            }
        }
        return related;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelableArrayList(STATE_DRINKS, adapter.getDrinks());
    }

    @Override
    protected void onStop() {
        cancelPreviousCall();
        super.onStop();
    }
}
