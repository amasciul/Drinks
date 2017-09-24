package fr.masciulli.drinks.ui.fragment;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import fr.masciulli.drinks.DrinksApplication;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.core.drinks.Drink;
import fr.masciulli.drinks.core.drinks.DrinksSource;
import fr.masciulli.drinks.drink.DrinkActivity;
import fr.masciulli.drinks.ui.adapter.DrinksAdapter;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.List;

public class DrinksFragment extends Fragment implements SearchView.OnQueryTextListener, ItemClickListener<Drink> {
    private static final String TAG = DrinksFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View emptyView;
    private View errorView;

    private DrinksSource drinksSource;
    private DrinksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drinksSource = DrinksApplication.get(getActivity()).getDrinksSource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View rootView = inflater.inflate(R.layout.fragment_drinks, container, false);
        recyclerView = rootView.findViewById(R.id.recycler);
        progressBar = rootView.findViewById(R.id.progress_bar);
        emptyView = rootView.findViewById(R.id.empty);
        errorView = rootView.findViewById(R.id.error);
        Button refreshButton = rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(v -> loadDrinks());

        adapter = new DrinksAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(adapter.craftLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        loadDrinks();


        return rootView;
    }

    private void loadDrinks() {
        displayLoadingState();
        drinksSource.getDrinks()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onDrinksRetrieved, this::onError);
    }

    private void onDrinksRetrieved(List<Drink> drinks) {
        adapter.setDrinks(drinks);
        displayNormalState();
    }

    private void onError(Throwable throwable) {
        Log.e(TAG, "Couldn't retrieve drinks", throwable);
        displayErrorState();
    }

    private void displayLoadingState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void displayErrorState() {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void displayNormalState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemClick(int position, Drink drink) {
        Intent intent = new Intent(getActivity(), DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINK_ID, drink.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TileViewHolder holder = (TileViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            String transition = getString(R.string.transition_drink);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), holder.getImageView(), transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_drinks, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.filter(newText);
        if (adapter.getItemCount() == 0) {
            showEmptyView();
        } else {
            hideEmptyView();
        }
        return false;
    }

    private void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
    }

    private void hideEmptyView() {
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // no-op
        return false;
    }

    @Override
    public void onDestroyOptionsMenu() {
        adapter.clearFilter();
        hideEmptyView();
        super.onDestroyOptionsMenu();
    }
}
