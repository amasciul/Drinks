package fr.masciulli.drinks.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.adapter.IngredientsListAdapter;

public class IngredientsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private IngredientsListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_ingredients, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new IngredientsListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
