package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.activity.MainActivity;
import fr.masciulli.drinks.adapter.DrinksListAdapter;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.view.ViewPagerScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinksListFragment extends RefreshableFragment implements AdapterView.OnItemClickListener, Callback<List<Drink>>,  ViewPagerScrollListener, SearchView.OnQueryTextListener {
    private ListView mListView;
    private ProgressBar mProgressBar;

    private DrinksListAdapter mListAdapter;

    private boolean mLoadingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_drinks_list, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mProgressBar = (ProgressBar)root.findViewById(R.id.progressbar);

        mListView.setEmptyView(root.findViewById(android.R.id.empty));
        mListView.setOnItemClickListener(this);
        mListAdapter = new DrinksListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        refresh();

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Drink drink = mListAdapter.getItem(i);

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.putExtra("drink_name", drink.name);
        intent.putExtra("drink_imageurl", drink.imageUrl);
        intent.putExtra("drink_id", drink.id);
        startActivity(intent);
    }

    @Override
    public void success(List<Drink> drinks, Response response) {
        mLoadingError = false;
        Log.d(getTag(), "Drinks list loading has succeeded");
        mListView.setVisibility(View.VISIBLE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListAdapter.update(drinks);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mLoadingError = true;
        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).setRefreshActionVisible(true);
        Log.e(getTag(), "Drinks list loading has failed");
        if (isAdded()) {
            if (retrofitError.isNetworkError()) {
                Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
            } else {
                Crouton.makeText(getActivity(), getString(R.string.list_loading_failed), Style.ALERT).show();
            }
        }
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = mListView.getChildAt(i);
            if (itemRoot == null) continue;

            TextView nameView = (TextView)itemRoot.findViewById(R.id.name);

            // TODO get screenWidth somewhere else (always the same)
            int screenWidth = itemRoot.getWidth();
            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(screenWidth + positionOffset * textWidth));
            nameView.setLeft(Math.round(screenWidth - textWidth + positionOffset * textWidth));
        }

    }

    @Override
    public void refresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        mListView.getEmptyView().setVisibility(View.GONE);
        ((MainActivity)getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getDrinksList(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.drinks_list, menu);

        // SearchView configuration
        final MenuItem searchMenuItem = menu.findItem(R.id.search);

        if(mLoadingError) {
            ((MainActivity)getActivity()).setRefreshActionVisible(true);
        }

        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (mListView != null) {
            mListAdapter.getFilter().filter(s);
        }
        return true;
    }
}
