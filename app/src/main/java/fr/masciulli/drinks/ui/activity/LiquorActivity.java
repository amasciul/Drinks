package fr.masciulli.drinks.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.DataProvider;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.LiquorRelatedAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LiquorActivity extends AppCompatActivity implements Callback<List<Drink>> {
    private static final String TAG = LiquorActivity.class.getSimpleName();
    public static final String EXTRA_LIQUOR = "extra_liquor";

    private Liquor liquor;
    private DataProvider provider = new DataProvider();
    private Call<List<Drink>> call;
    private boolean drinksLoaded = false;
    private LiquorRelatedAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liquor = getIntent().getParcelableExtra(EXTRA_LIQUOR);

        setContentView(R.layout.activity_liquor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(liquor.getName());

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(liquor.getImageUrl()).into(imageView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new LiquorRelatedAdapter();
        adapter.setLiquor(liquor);
        adapter.setWikipediaClickListener(new ItemClickListener<Liquor>() {
            @Override
            public void onItemClick(int position, Liquor liquor) {
                onWikipediaClick();
            }
        });

        adapter.setDrinkClickListener(new ItemClickListener<Drink>() {
            @Override
            public void onItemClick(int position, Drink drink) {
                onDrinkClick(drink);
            }
        });

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

    private void onDrinkClick(Drink drink) {
        Intent intent = new Intent(this, DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINK, drink);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!drinksLoaded) {
            loadDrinks();
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
    public void onResponse(Response<List<Drink>> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            adapter.setRelatedDrinks(filterRelatedDrinks(response.body()));
        } else {
            Log.e(TAG, "Couldn't retrieve liquors : " + response.message());
        }
    }

    @Override
    public void onFailure(Throwable t) {
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
    protected void onStop() {
        cancelPreviousCall();
        super.onStop();
    }
}
