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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.LiquorDetailActivity;
import fr.masciulli.drinks.activity.MainActivity;
import fr.masciulli.drinks.adapter.LiquorListAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.view.DrinksOnScrollListener;
import fr.masciulli.drinks.view.ViewPagerScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.List;

public class LiquorsListFragment extends Fragment implements Callback<List<Liquor>>, ViewPagerScrollListener {
    private static final String STATE_LIST = "liquor";
    private static final String PREF_LIQUORS_JSON = "liquors_json";

    private ListView listView;
    private View emptyView;
    private ProgressBar progressBar;

    private LiquorListAdapter listAdapter;

    private boolean loadingError = false;

    public static LiquorsListFragment newInstance() {
        return new LiquorsListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        final View root = inflater.inflate(R.layout.fragment_liquors_list, container, false);

        listView = (ListView) root.findViewById(R.id.list);
        emptyView = root.findViewById(android.R.id.empty);
        progressBar = (ProgressBar) root.findViewById(R.id.progressbar);

        listAdapter = new LiquorListAdapter(getActivity());
        listView.setOnScrollListener(new DrinksOnScrollListener(listView, DrinksOnScrollListener.NAMEVIEW_POSITION_TOP));
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openLiquorDetail(position);
            }
        });

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_LIST)) {
                List<Liquor> savedLiquors = savedInstanceState.getParcelableArrayList(STATE_LIST);
                updateList(savedLiquors);
            } else {
                refresh();
            }
        } else {
            refresh();
        }

        return root;
    }

    private void updateList(List<Liquor> liquors) {
        listAdapter.update(liquors);
        if (listAdapter.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private void openLiquorDetail(int position) {
        Liquor liquor = listAdapter.getItem(position);
        Intent intent = new Intent(getActivity(), LiquorDetailActivity.class);
        intent.putExtra(LiquorDetailActivity.ARG_LIQUOR, liquor);
        startActivity(intent);
    }

    @Override
    public void success(List<Liquor> liquors, Response response) {
        loadingError = false;

        if (getActivity() == null) {
            return;
        }

        progressBar.setVisibility(View.GONE);
        updateList(liquors);

        Gson gson = new Gson();
        String json = gson.toJson(liquors);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
        editor.putString(PREF_LIQUORS_JSON, json);
        editor.apply();
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        loadingError = true;

        if (getActivity() == null) {
            return;
        }

        progressBar.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(int position, float positionOffset, int positionOffsetPixels) {
        if (position == getResources().getInteger(R.integer.position_fragment_ingredients)) {
            return;
        }

        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();

        for (int i = 0; i <= last - first; i++) {
            View itemRoot = listView.getChildAt(i);
            if (itemRoot == null) {
                continue;
            }

            TextView nameView = (TextView) itemRoot.findViewById(R.id.name);

            int textWidth = nameView.getWidth();

            nameView.setRight(Math.round(textWidth - (1 - positionOffset) * textWidth));
            nameView.setLeft(Math.round(-(1 - positionOffset) * textWidth));
        }
    }

    private void refresh() {
        progressBar.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        ((MainActivity) getActivity()).setRefreshActionVisible(false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains(PREF_LIQUORS_JSON)) {
            Gson gson = new Gson();
            //TODO async
            List<Liquor> liquors = gson.fromJson(preferences.getString(PREF_LIQUORS_JSON, "null"), new TypeToken<List<Liquor>>() {
            }.getType());
            updateList(liquors);
            progressBar.setVisibility(View.GONE);
        }
        DrinksProvider.getAllLiquors(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        if (loadingError) {
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

        if (listAdapter.getCount() > 0) {
            outState.putParcelableArrayList(STATE_LIST, listAdapter.getLiquors());
        }
    }
}
