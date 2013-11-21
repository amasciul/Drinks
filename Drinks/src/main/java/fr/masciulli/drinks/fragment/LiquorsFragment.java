package fr.masciulli.drinks.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.MainActivity;
import fr.masciulli.drinks.adapter.LiquorListAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.view.ViewPagerScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiquorsFragment extends RefreshableFragment implements AdapterView.OnItemClickListener, Callback<List<Liquor>>,ViewPagerScrollListener {
    private ListView mListView;
    private LiquorListAdapter mListAdapter;

    private ProgressBar mProgressBar;

    private boolean mLoadingError = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_liquors, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setEmptyView(root.findViewById(android.R.id.empty));
        mListView.setOnItemClickListener(this);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        mProgressBar = (ProgressBar)root.findViewById(R.id.progressbar);

        refresh();

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        Log.d(getTag(), "Liquors list loading has succeeded");
        mLoadingError = false;
        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        mListAdapter.update(liquors);
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.e(getTag(), "Liquor list loading has failed");
        mLoadingError = true;
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

            TextView nameView = (TextView)itemRoot.findViewById(R.id.name);

            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(textWidth - (1 - positionOffset) * textWidth));
            nameView.setLeft(Math.round(- (1 - positionOffset) * textWidth));
        }
    }

    @Override
    public void refresh() {
        mProgressBar.setVisibility(View.GONE);
        mListView.getEmptyView().setVisibility(View.VISIBLE);
        ((MainActivity)getActivity()).setRefreshActionVisible(false);
        DrinksProvider.getLiquorsList(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (mLoadingError) {
            Log.d(getTag(), "loading error");
            ((MainActivity)getActivity()).setRefreshActionVisible(true);
        }
    }
}
