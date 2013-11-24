package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class LiquorDetailAdapter extends BaseAdapter {
    private List<Drink> mDrinks = new ArrayList<Drink>();
    private Context mContext;

    public LiquorDetailAdapter(Context context) {
        mContext = context;

        for (int i = 0; i < 4; i++) {
            Drink drink = new Drink();
            drink.name = "Mojito";
            mDrinks.add(drink);
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

        if (root == null) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_liquor_detail, parent, false);
        }

        final TextView nameView = Holder.get(root, R.id.name);

        final Drink drink = getItem(i);

        nameView.setText(drink.name);

        return root;
    }
}
