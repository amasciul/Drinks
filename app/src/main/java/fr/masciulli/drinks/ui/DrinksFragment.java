package fr.masciulli.drinks.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.net.DrinksProvider;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DrinksFragment extends Fragment implements Callback<List<Drink>> {
    private static final String TAG = DrinksFragment.class.getSimpleName();

    private RecyclerView recyclerView;

    private DrinksProvider provider;
    private DrinksAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        provider = new DrinksProvider();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drinks, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler);
        int columnCount = getResources().getInteger(R.integer.column_count);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
        adapter = new DrinksAdapter();
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        provider.getDrinks(this);
    }

    @Override
    public void onResponse(Response<List<Drink>> response, Retrofit retrofit) {
        if (response.isSuccess()) {
            adapter.setDrinks(response.body());
        } else {
            Log.e(TAG, "Couldn't retrieve drinks : " + response.message());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Couldn't retrieve drinks", t);
    }
}
