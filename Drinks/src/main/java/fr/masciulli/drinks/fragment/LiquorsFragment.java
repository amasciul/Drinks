package fr.masciulli.drinks.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.adapter.LiquorListAdapter;

public class LiquorsFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ListView mListView;
    private LiquorListAdapter mListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_liquors, container, false);

        mListView = (ListView) root.findViewById(R.id.list);
        mListView.setOnItemClickListener(this);
        mListAdapter = new LiquorListAdapter(getActivity());
        mListView.setAdapter(mListAdapter);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
