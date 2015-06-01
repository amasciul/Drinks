package fr.masciulli.drinks.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrinksListAdapter extends RecyclerView.Adapter<DrinksListAdapter.ViewHolder> implements Filterable {
    private List<Drink> drinks = Collections.emptyList();
    private List<Drink> savedDrinks = Collections.emptyList();
    private OnItemClickListener onItemClickListener;
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drinks_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Drink drink = drinks.get(position);
        viewHolder.nameView.setText(drink.name);
        Picasso.with(viewHolder.imageView.getContext()).load(drink.imageUrl).into(viewHolder.imageView);
        if (onItemClickListener != null) {
            viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return drinks.size();
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
        return (ArrayList<Drink>) drinks;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView nameView;
        private final View rootView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameView = (TextView) itemView.findViewById(R.id.name);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
