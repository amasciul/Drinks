package fr.masciulli.drinks.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import fr.masciulli.drinks.ui.view.RatioImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DrinksAdapter extends RecyclerView.Adapter<TileViewHolder> {
    private static final int TYPE_34 = 0;
    private static final int TYPE_43 = 1;
    private static final float RATIO_34 = 3.0f / 4.0f;
    private static final float RATIO_43 = 4.0f / 3.0f;

    private static int[] ratios = new int[]{TYPE_34, TYPE_43};

    private List<Drink> drinks = new ArrayList<>();
    private List<Drink> filteredDrinks = new ArrayList<>();
    private Map<Drink, Integer> ratioMap = new HashMap<>();

    private ItemClickListener<Drink> listener;

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink, parent, false);
        return new TileViewHolder(root);
    }

    @Override
    public void onBindViewHolder(final TileViewHolder holder, int position) {
        final Drink drink = filteredDrinks.get(position);
        holder.getNameView().setText(drink.getName());

        RatioImageView imageView = holder.getImageView();
        switch (getItemViewType(position)) {
            case TYPE_34:
                imageView.setRatio(RATIO_34);
                break;
            case TYPE_43:
                imageView.setRatio(RATIO_43);
                break;
            default:
                throw new IllegalArgumentException("Unknown ratio type");
        }

        Picasso.with(holder.itemView.getContext())
                .load(drink.getImageUrl())
                .fit()
                .centerCrop()
                .noFade()
                .into(imageView);

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> holder.getNameView().animate()
                    .translationY(holder.getNameView().getHeight())
                    .withEndAction(() -> {
                        holder.getNameView().setTranslationY(0);
                        listener.onItemClick(holder.getAdapterPosition(), drink);
                    }).start());
        }
    }

    @Override
    public int getItemCount() {
        return filteredDrinks.size();
    }

    @Override
    public int getItemViewType(int position) {
        Drink drink = drinks.get(position);
        return ratioMap.get(drink);
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
        ratioMap.clear();
        for (int i = 0, size = drinks.size(); i < size; i++) {
            Drink drink = drinks.get(i);
            ratioMap.put(drink, ratios[i % ratios.length]);
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

    public ArrayList<Drink> getDrinks() {
        return new ArrayList<>(drinks);
    }
}
