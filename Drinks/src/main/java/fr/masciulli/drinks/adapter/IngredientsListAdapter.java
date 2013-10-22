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
import fr.masciulli.drinks.model.Ingredient;

public class IngredientsListAdapter extends BaseAdapter {
    private List<Ingredient> mIngredients = new ArrayList<Ingredient>();
    private Context mContext;

    public IngredientsListAdapter(Context context) {
        mContext = context;
        for (int i=0; i<50; i++) {
            mIngredients.add(new Ingredient("Amaretto"));
        }

    }

    @Override
    public int getCount() {
        return mIngredients.size();
    }

    @Override
    public Ingredient getItem(int i) {
        return mIngredients.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {
        root = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);

        ((TextView) root.findViewById(R.id.name)).setText(getItem(i).getName());

        return root;
    }
}
