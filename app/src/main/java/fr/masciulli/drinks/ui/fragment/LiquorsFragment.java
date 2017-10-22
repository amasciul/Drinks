package fr.masciulli.drinks.ui.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import fr.masciulli.drinks.DrinksApplication;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.core.liquors.Liquor;
import fr.masciulli.drinks.core.liquors.LiquorsSource;
import fr.masciulli.drinks.ui.activity.LiquorActivity;
import fr.masciulli.drinks.view.ItemClickListener;
import fr.masciulli.drinks.ui.adapter.LiquorsAdapter;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class LiquorsFragment extends Fragment implements ItemClickListener<Liquor> {
    private static final String TAG = LiquorsFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View errorView;

    private LiquorsSource liquorsSource;
    private LiquorsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liquorsSource = DrinksApplication.get(getActivity()).getLiquorsSource();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_liquors, container, false);
        recyclerView = rootView.findViewById(R.id.recycler);
        progressBar = rootView.findViewById(R.id.progress_bar);
        errorView = rootView.findViewById(R.id.error);
        Button refreshButton = rootView.findViewById(R.id.refresh);

        refreshButton.setOnClickListener(v -> loadLiquors());

        adapter = new LiquorsAdapter();
        adapter.setItemClickListener(this);
        recyclerView.setLayoutManager(adapter.craftLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        loadLiquors();

        return rootView;
    }

    private void loadLiquors() {
        displayLoadingState();
        liquorsSource.getLiquors()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLiquorsRetrieved, this::onError);
    }

    private void onError(Throwable throwable) {
        Log.e(TAG, "Couldn't retrieve liquors", throwable);
        displayErrorState();
    }

    private void onLiquorsRetrieved(List<Liquor> liquors) {
        adapter.setLiquors(liquors);
        displayNormalState();
    }

    private void displayLoadingState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void displayErrorState() {
        errorView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }

    private void displayNormalState() {
        errorView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(int position, Liquor liquor) {
        Intent intent = new Intent(getActivity(), LiquorActivity.class)
                .putExtra(LiquorActivity.EXTRA_LIQUOR_ID, liquor.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            TileViewHolder holder = (TileViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
            String transition = getString(R.string.transition_liquor);
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(getActivity(), holder.getImageView(), transition);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }
}
