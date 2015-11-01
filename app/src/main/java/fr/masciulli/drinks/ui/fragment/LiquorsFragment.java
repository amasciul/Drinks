package fr.masciulli.drinks.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import android.widget.Button;
import android.widget.ProgressBar;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.net.DataProvider;
import fr.masciulli.drinks.ui.activity.LiquorActivity;
import fr.masciulli.drinks.ui.adapter.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.LiquorsAdapter;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LiquorsFragment extends Fragment implements Callback<List<Liquor>>,
        ItemClickListener<Liquor> {
    private static final String TAG = LiquorsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View errorView;

    private LiquorsAdapter adapter;
    private DataProvider provider = new DataProvider();
    private Call<List<Liquor>> call;
    private boolean liquorsLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liquors, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        errorView = rootView.findViewById(R.id.error);
        Button refreshButton = (Button) rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadLiquors();
            }
        });

        int columnCount = getResources().getInteger(R.integer.column_count);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL));
        adapter = new LiquorsAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!liquorsLoaded) {
            loadLiquors();
        }
    }

    private void loadLiquors() {
        displayLoadingState();
        cancelPreviousCall();
        liquorsLoaded = false;
        call = provider.getLiquors();
        call.enqueue(this);
    }

    private void cancelPreviousCall() {
        if (call != null) {
            call.cancel();
        }
    }

    @Override
    public void onResponse(Response<List<Liquor>> response, Retrofit retrofit) {
        Log.d(TAG, response.body().toString());
        if (response.isSuccess()) {
            liquorsLoaded = true;
            adapter.setLiquors(response.body());
            displayNormalState();
        } else {
            displayErrorState();
            Log.e(TAG, "Couldn't retrieve liquors : " + response.message());
        }
    }

    @Override
    public void onFailure(Throwable t) {
        Log.e(TAG, "Couldn't retrieve liquors", t);
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
    public void onItemClick(int position, Liquor liquor) {
        Intent intent = new Intent(getActivity(), LiquorActivity.class);
        intent.putExtra(LiquorActivity.EXTRA_LIQUOR, liquor);
        startActivity(intent);
    }
}
