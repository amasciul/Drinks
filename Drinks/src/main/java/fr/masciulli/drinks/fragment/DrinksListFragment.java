package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.adapter.DrinksListAdapter;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinksListFragment extends Fragment implements AdapterView.OnItemClickListener, Callback<List<Drink>>, View.OnClickListener {
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

        load();

        return root;
    }

    private void load() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRefreshButton.setVisibility(View.GONE);
        DrinksProvider.getDrinksList(this);
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
        Log.d(this.getClass().getName(), "Drinks list loading has succeeded");
        mListView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mListAdapter.update(drinks);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mProgressBar.setVisibility(View.GONE);
        mRefreshButton.setVisibility(View.VISIBLE);
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
        load();
    }
}
