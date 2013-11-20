package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.masciulli.drinks.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

import com.squareup.picasso.Picasso;

public class DrinksListAdapter extends BaseAdapter implements Filterable {
    private List<Drink> mDrinks = Collections.emptyList();
    private List<Drink> mSavedDrinks = Collections.emptyList();
    private Context mContext;
    private Filter mFilter = new Filter() {

        private List<Drink> mFilteredDrinks = new ArrayList<Drink>();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            mFilteredDrinks.clear();
            FilterResults results = new FilterResults();

            Log.d("adapter","performing filtering with " + charSequence);

            final String query = charSequence.toString().toLowerCase();

            final List<Drink> ingredientMatchDrinks = new ArrayList<Drink>();

            for (Drink drink : mSavedDrinks) {
                if(drink.name.toLowerCase().contains(query)) {
                    mFilteredDrinks.add(drink);
                } else {
                    // drink name does not match, we check the ingredients
                    for (String ingredient : drink.ingredients) {
                        if (ingredient.toLowerCase().contains(query)) {
                            ingredientMatchDrinks.add(drink);
                            break;
                        }
                    }
                }
            }

            mFilteredDrinks.addAll(ingredientMatchDrinks);

            results.count = mFilteredDrinks.size();
            results.values = mFilteredDrinks;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            update((List<Drink>)filterResults.values, true);
        }
    };


    public DrinksListAdapter(Context context) {
        mContext = context;
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
            root = LayoutInflater.from(mContext).inflate(R.layout.item_drink, parent, false);
        }

        final ImageView imageView = Holder.get(root, R.id.image);
        final TextView nameView = Holder.get(root, R.id.name);

        final Drink drink = getItem(i);

        nameView.setText(drink.name);
        Picasso.with(mContext).load(drink.imageUrl).into(imageView);

        return root;
    }

    public void update(List<Drink> drinks, boolean dueToFilterOperation) {
        mDrinks = drinks;
        notifyDataSetChanged();
        if(!dueToFilterOperation) {
            mSavedDrinks = new ArrayList<Drink>(drinks);
        }
    }

    public void update(List<Drink> drinks) {
        update(drinks, false);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
