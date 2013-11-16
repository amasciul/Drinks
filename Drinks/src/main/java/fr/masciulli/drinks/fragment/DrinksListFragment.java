package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class DrinksListFragment extends RefreshableFragment implements AdapterView.OnItemClickListener, Callback<List<Drink>>, View.OnClickListener, ViewPagerScrollListener {
    private ListView mListView;
    private ProgressBar mProgressBar;

    private DrinksListAdapter mListAdapter;
    private Button mRefreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_drinks, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mProgressBar = (ProgressBar)root.findViewById(R.id.progressbar);
        mRefreshButton = (Button)root.findViewById(R.id.refresh);

        mRefreshButton.setOnClickListener(this);

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
        Log.d(getTag(), "Drinks list loading has succeeded");
        mListView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListAdapter.update(drinks);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshButton.setVisibility(View.VISIBLE);
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
    public void onClick(View view) {
        refresh();
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
        mRefreshButton.setVisibility(View.GONE);
        ((MainActivity)getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getDrinksList(this);
    }
}
