package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

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

public class LiquorsListFragment extends Fragment implements AdapterView.OnItemClickListener, Callback<List<Liquor>>, ViewPagerScrollListener {
    private static final String STATE_LIST = "liquor";

    private ListView mListView;
    private LiquorListAdapter mListAdapter;

    private ProgressBar mProgressBar;

    private boolean mLoadingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_liquors_list, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setEmptyView(root.findViewById(android.R.id.empty));
        mListView.setOnItemClickListener(this);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LIST)) {
                Log.d(getTag(), "retrieved liquors from saved instance state");
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Liquor liquor = mListAdapter.getItem(i);

        Intent intent = new Intent(getActivity(), LiquorDetailActivity.class);
        intent.putExtra("liquor_name", liquor.name);
        intent.putExtra("liquor_imageurl", liquor.imageUrl);
        intent.putExtra("liquor_id", liquor.id);
        startActivity(intent);
    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        Log.d(getTag(), "Liquors list loading has succeeded");
        mLoadingError = false;

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        mListAdapter.update(liquors);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.e(getTag(), "Liquor list loading has failed");
        mLoadingError = true;

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        if (position == getResources().getInteger(R.integer.position_fragment_ingredients)) return;

        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = mListView.getChildAt(i);
            if (itemRoot == null) continue;

            TextView nameView = (TextView) itemRoot.findViewById(R.id.name);

            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(textWidth - (1 - positionOffset) * textWidth));
            nameView.setLeft(Math.round(-(1 - positionOffset) * textWidth));
        }
    }

    private void refresh() {
        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        ((MainActivity) getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getAllLiquors(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mLoadingError) {
            Log.d(getTag(), "loading error");
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
