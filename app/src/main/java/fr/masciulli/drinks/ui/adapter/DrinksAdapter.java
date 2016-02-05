package fr.masciulli.drinks.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.ui.view.RatioImageView;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.ViewHolder> {
    private static float[] ratios = new float[]{0.75f, 4.0f / 3.0f};

    private ArrayList<Drink> drinks = new ArrayList<>();
    private ArrayList<Drink> filteredDrinks = new ArrayList<>();

    private ItemClickListener<Drink> listener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Drink drink = filteredDrinks.get(position);
        holder.nameView.setText(drink.getName());
        holder.imageView.setRatio(drink.getRatio());
        Picasso.with(holder.itemView.getContext())
                .load(drink.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition(), drink);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredDrinks.size();
    }

    public void setItemClickListener(ItemClickListener<Drink> listener) {
        this.listener = listener;
    }

    public void setDrinks(List<Drink> drinks) {
        this.filteredDrinks.clear();
        this.drinks.clear();

        this.drinks.addAll(drinks);
        this.filteredDrinks.addAll(drinks);

        fakeRatios();
        notifyDataSetChanged();
    }

    private void fakeRatios() {
        // TODO remove this and use ratios given by server
        for (int i = 0, size = drinks.size(); i < size; i++) {
            Drink drink = drinks.get(i);
            drink.setRatio(ratios[i % ratios.length]);
        }
    }

    public void filter(String text) {
        filteredDrinks.clear();
        text = text.toLowerCase(Locale.US);
        for (Drink drink : drinks) {
            if (drink.getName().toLowerCase(Locale.US).contains(text)) {
                filteredDrinks.add(drink);
            } else {
                for (String ingredient : drink.getIngredients()) {
                    if (ingredient.toLowerCase(Locale.US).contains(text)) {
                        filteredDrinks.add(drink);
                        break;
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void clearFilter() {
        filteredDrinks.clear();
        filteredDrinks.addAll(drinks);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final RatioImageView imageView;
        private final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (RatioImageView) itemView.findViewById(R.id.image);
            nameView = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
