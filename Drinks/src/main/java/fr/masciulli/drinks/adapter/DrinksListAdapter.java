package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinksListAdapter extends BaseAdapter {
    private List<Drink> mDrinks = new ArrayList<Drink>();
    private Context mContext;


    public DrinksListAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 100; i++) {
            mDrinks.add(new Drink("Amaretto Frost"));
        }
    }

    @Override
    public int getCount() {
        return mDrinks.size();
    }

    @Override
    public Drink getItem(int i) {
        return mDrinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {
        root = LayoutInflater.from(mContext).inflate(R.layout.item_drink, parent, false);

        ((TextView) root.findViewById(R.id.name)).setText(getItem(i).getName());

        return root;
    }
}
