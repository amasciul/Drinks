package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

import java.util.List;

public class LiquorsListFragment extends Fragment implements Callback<List<Liquor>>, ViewPagerScrollListener {
    private static final String STATE_LIST = "liquor";
    private static final String PREF_LIQUORS_JSON = "liquors_json";

    @InjectView(R.id.list)
    ListView mListView;
    @InjectView(android.R.id.empty)
    View mEmptyView;
    @InjectView(R.id.progressbar)
    ProgressBar mProgressBar;

    private LiquorListAdapter mListAdapter;

    private boolean mLoadingError = false;

    public static LiquorsListFragment newInstance() {
        return new LiquorsListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_liquors_list, container, false);
        ButterKnife.inject(this, root);

        mListView.setEmptyView(mEmptyView);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setOnScrollListener(new DrinksOnScrollListener(mListView, DrinksOnScrollListener.NAMEVIEW_POSITION_TOP));
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

    @SuppressWarnings("unused")
    @OnItemClick(R.id.list)
    public void openLiquorDetail(View view, int position) {
        Liquor liquor = mListAdapter.getItem(position);

        Intent intent = new Intent(getActivity(), LiquorDetailActivity.class);
        intent.putExtra("liquor", liquor);
        startActivity(intent);

    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        mLoadingError = false;

        if (getActivity() == null) {
            return;
        }

        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mListAdapter.update(liquors);

        Gson gson = new Gson();
        String json = gson.toJson(liquors);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(PREF_LIQUORS_JSON, json);
        editor.apply();
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        mLoadingError = true;

        if (getActivity() == null) {
            return;
        }

        mProgressBar.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        if (position == getResources().getInteger(R.integer.position_fragment_ingredients)) {
            return;
        }

        int first = mListView.getFirstVisiblePosition();
        int last = mListView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = mListView.getChildAt(i);
            if (itemRoot == null) {
                continue;
            }

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

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains(PREF_LIQUORS_JSON)) {
            Gson gson = new Gson();
            //TODO async
            List<Liquor> liquors = gson.fromJson(preferences.getString(PREF_LIQUORS_JSON, "null"), new TypeToken<List<Liquor>>() {
            }.getType());
            mListAdapter.update(liquors);
            mListView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
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
