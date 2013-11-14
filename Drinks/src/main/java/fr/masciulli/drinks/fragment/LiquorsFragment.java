package fr.masciulli.drinks.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.adapter.LiquorListAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.data.DrinksService;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiquorsFragment extends Fragment implements AdapterView.OnItemClickListener, Callback<List<Liquor>> {
    private ListView mListView;
    private LiquorListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_liquors, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        DrinksProvider.getLiquorsList(this);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        Log.d(getTag(), "Liquors list loading has succeeded");
        mListAdapter.update(liquors);
    }

    @Override
    public void failure(RetrofitError retrofitError) {

    }
}
