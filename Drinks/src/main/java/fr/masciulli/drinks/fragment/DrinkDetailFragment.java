package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.masciulli.drinks.R;

public class DrinkDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);
        ((TextView)root.findViewById(R.id.name)).setText(getActivity().getIntent().getStringExtra("drink_name"));
        return root;
    }
}
