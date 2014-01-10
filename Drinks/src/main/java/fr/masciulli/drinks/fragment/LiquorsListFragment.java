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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.LiquorDetailActivity;
import fr.masciulli.drinks.activity.MainActivity;
import fr.masciulli.drinks.adapter.LiquorListAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.view.ViewPagerScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiquorsListFragment extends Fragment implements Callback<List<Liquor>>, ViewPagerScrollListener {
    private static final String STATE_LIST = "liquor";

    @InjectView(R.id.list) ListView mListView;
    @InjectView(android.R.id.empty) View mEmptyView;
    @InjectView(R.id.progressbar) ProgressBar mProgressBar;

    private LiquorListAdapter mListAdapter;

    private boolean mLoadingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_liquors_list, container, false);
        ButterKnife.inject(this, root);

        mListView.setEmptyView(mEmptyView);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LIST)) {
                List<Liquor> savedLiquors = savedInstanceState.getParcelableArrayList(STATE_LIST);
                mListAdapter.update(savedLiquors);
            } else {
                refresh();
            }
        } else {
            refresh();
        }

        return root;
    }

    @OnItemClick(R.id.list)
    public void openLiquorDetail(View view, int position) {
        Liquor liquor = mListAdapter.getItem(position);

        // Data needed for animations in sub activity
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        int orientation = getResources().getConfiguration().orientation;

        Intent intent = new Intent(getActivity(), LiquorDetailActivity.class);
        intent.putExtra("liquor_name", liquor.name).
                putExtra("liquor_imageurl", liquor.imageUrl).
                putExtra("liquor_id", liquor.id).
                putExtra("top", screenLocation[1]).
                putExtra("height", view.getHeight()).
                putExtra("orientation", orientation);
        startActivity(intent);

        if (!getResources().getBoolean(R.bool.dualpane)) {
            getActivity().overridePendingTransition(0, 0);
        }
    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        mLoadingError = false;

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mListAdapter.update(liquors);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mLoadingError = true;

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        if (position == getResources().getInteger(R.integer.position_fragment_ingredients)) return;

        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = mListView.getChildAt(i);
            if (itemRoot == null) continue;

            TextView nameView = ButterKnife.findById(itemRoot, R.id.name);

            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(textWidth - (1 - positionOffset) * textWidth));
            nameView.setLeft(Math.round(-(1 - positionOffset) * textWidth));
        }
    }

    private void refresh() {
        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getAllLiquors(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mLoadingError) {
            ((MainActivity) getActivity()).setRefreshActionVisible(true);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mListAdapter.getCount() > 0) {
            outState.putParcelableArrayList(STATE_LIST, mListAdapter.getLiquors());
        }
    }
}
