package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.adapter.DrinksListAdapter;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.DrinksListItem;
import fr.masciulli.drinks.util.ConnectionUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinksListFragment extends Fragment implements AdapterView.OnItemClickListener, Callback<List<DrinksListItem>> {
    private ListView mListView;
    private DrinksListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_drinks, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new DrinksListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);
        if (ConnectionUtils.isOnline(getActivity())) {
            DrinksProvider.getDrinksList(this);
        } else {
            Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
        }

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DrinksListItem drink = mListAdapter.getItem(i);

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.putExtra("drink_name", drink.name);
        intent.putExtra("drink_imageurl", drink.imageURL);
        intent.putExtra("drink_id", drink.id);
        startActivity(intent);
    }

    @Override
    public void success(List<DrinksListItem> drinks, Response response) {
        Log.d(this.getClass().getName(), "Drinks list loading has succeeded");
        mListAdapter.update(drinks);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.e(this.getClass().getName(), "Drinks list loading has failed");
        Crouton.makeText(getActivity(), getString(R.string.list_loading_failed), Style.ALERT).show();
    }
}
