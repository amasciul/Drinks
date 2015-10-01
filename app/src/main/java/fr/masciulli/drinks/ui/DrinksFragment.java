package fr.masciulli.drinks.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        adapter.setDrinks(response.body());
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Couldn't retrieve drinks", t);
    }
}
