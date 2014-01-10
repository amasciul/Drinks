package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.activity.MainActivity;
import fr.masciulli.drinks.adapter.DrinksListAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.view.ViewPagerScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinksListFragment extends Fragment implements AdapterView.OnItemClickListener, Callback<List<Drink>>, ViewPagerScrollListener, SearchView.OnQueryTextListener {
    private static final String STATE_LIST = "drinks_list";

    @InjectView(R.id.list) ListView mListView;
    @InjectView(R.id.progressbar) ProgressBar mProgressBar;
    @InjectView(android.R.id.empty) View mEmptyView;

    private DrinksListAdapter mListAdapter;

    private boolean mLoadingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_drinks_list, container, false);
        ButterKnife.inject(this, root);

        mListView.setEmptyView(mEmptyView);
        mListView.setOnItemClickListener(this);
        mListAdapter = new DrinksListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LIST)) {
                List<Drink> savedDrinks = savedInstanceState.getParcelableArrayList(STATE_LIST);
                mListAdapter.update(savedDrinks);
            } else {
                refresh();
            }
        } else {
            refresh();
        }

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Drink drink = mListAdapter.getItem(i);

        // Data needed for animations in sub activity
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        int orientation = getResources().getConfiguration().orientation;

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.
                putExtra("drink_name", drink.name).
                putExtra("drink_imageurl", drink.imageUrl).
                putExtra("drink_id", drink.id).
                putExtra("top", screenLocation[1]).
                putExtra("height", view.getHeight()).
                putExtra("orientation", orientation);
        startActivity(intent);

        if (!getResources().getBoolean(R.bool.dualpane)) {
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void success(List<Drink> drinks, Response response) {
        mLoadingError = false;

        if (getActivity() == null) return;

        mListView.setVisibility(View.VISIBLE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListAdapter.update(drinks);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mLoadingError = true;

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setRefreshActionVisible(true);

        if (retrofitError.isNetworkError()) {
            Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), getString(R.string.list_loading_failed), Style.ALERT).show();
        }
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = mListView.getChildAt(i);
            if (itemRoot == null) continue;

            TextView nameView = (TextView) itemRoot.findViewById(R.id.name);

            // TODO get screenWidth somewhere else (always the same)
            int screenWidth = itemRoot.getWidth();
            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(screenWidth + positionOffset * textWidth));
            nameView.setLeft(Math.round(screenWidth - textWidth + positionOffset * textWidth));
        }

    }

    private void refresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getAllDrinks(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.drinks_list, menu);

        // SearchView configuration
        final MenuItem searchMenuItem = menu.findItem(R.id.search);

        if (mLoadingError) {
            ((MainActivity) getActivity()).setRefreshActionVisible(true);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mListAdapter.getCount() > 0) {
            outState.putParcelableArrayList(STATE_LIST, mListAdapter.getDrinks());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retry:
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
