package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.adapter.DrinksListAdapter;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.DrinksListItem;

public class DrinksListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private DrinksListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_drinks, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new DrinksListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DrinksListItem drink = mListAdapter.getItem(i);

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.putExtra("drink_name", drink.name);
        intent.putExtra("drink_imageurl", drink.imageURL);
        intent.putExtra("drink_id", drink.id);
        startActivity(intent);
    }
}
