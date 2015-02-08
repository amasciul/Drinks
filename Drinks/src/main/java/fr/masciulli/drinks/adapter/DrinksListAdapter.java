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
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.view.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrinksListAdapter extends BaseAdapter implements Filterable {
    private List<Drink> drinks = Collections.emptyList();
    private List<Drink> savedDrinks = Collections.emptyList();
    private Context context;
    private Filter filter = new Filter() {

        private List<Drink> filteredDrinks = new ArrayList<>();

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            filteredDrinks.clear();
            FilterResults results = new FilterResults();

            Log.d("adapter", "performing filtering with " + charSequence);

            final String query = charSequence.toString().toLowerCase();

            final List<Drink> ingredientMatchDrinks = new ArrayList<Drink>();

            for (Drink drink : savedDrinks) {
                if (drink.name.toLowerCase().contains(query)) {
                    filteredDrinks.add(drink);
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

            filteredDrinks.addAll(ingredientMatchDrinks);

            results.count = filteredDrinks.size();
            results.values = filteredDrinks;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            update((List<Drink>) filterResults.values, true);
        }
    };

    public DrinksListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Drink getItem(int i) {
        return drinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {

        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.item_drinks_list, parent, false);
        }

        final ImageView imageView = Holder.get(root, R.id.image);
        final TextView nameView = Holder.get(root, R.id.name);

        final Drink drink = getItem(i);

        nameView.setText(drink.name);
        Picasso.with(context).load(drink.imageUrl).into(imageView);

        return root;
    }

    public void update(List<Drink> drinks, boolean dueToFilterOperation) {
        this.drinks = drinks;
        notifyDataSetChanged();
        if (!dueToFilterOperation) {
            savedDrinks = new ArrayList<>(drinks);
        }
    }

    public void update(List<Drink> drinks) {
        update(drinks, false);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    public ArrayList<Drink> getDrinks() {
        return (ArrayList<Drink>) savedDrinks;
    }
}
