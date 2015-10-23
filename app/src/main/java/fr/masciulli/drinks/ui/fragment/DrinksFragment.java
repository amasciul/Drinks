package fr.masciulli.drinks.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.DrinksProvider;
import fr.masciulli.drinks.ui.activity.DrinkActivity;
import fr.masciulli.drinks.ui.adapter.DrinksAdapter;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DrinksFragment extends Fragment implements Callback<List<Drink>>, SearchView.OnQueryTextListener, ItemClickListener<Drink> {
    private static final String TAG = DrinksFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View emptyView;
    private View errorView;
    private Button refreshButton;

    private DrinksProvider provider;
    private DrinksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        provider = new DrinksProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drinks, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler);
        progressBar = (ProgressBar) root.findViewById(R.id.progress_bar);
        emptyView = root.findViewById(R.id.empty);
        errorView = root.findViewById(R.id.error);
        refreshButton = (Button) root.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDrinks();
            }
        });

        int columnCount = getResources().getInteger(R.integer.column_count);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DrinksAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDrinks();
    }

    private void loadDrinks() {
        displayLoadingState();
        provider.getDrinks(this);
    }

    @Override
    public void onResponse(Response<List<Drink>> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            adapter.setDrinks(response.body());
            displayNormalState();
        } else {
            displayErrorState();
            Log.e(TAG, "Couldn't retrieve drinks : " + response.message());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Couldn't retrieve drinks", t);
        displayErrorState();
    }

    void displayLoadingState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    void displayErrorState() {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    void displayNormalState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position, Drink drink) {
        Intent intent = new Intent(getActivity(), DrinkActivity.class);
        intent.putExtra(DrinkActivity.EXTRA_DRINK, drink);
        startActivity(intent);
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
        super.onDestroyOptionsMenu();
    }
}
