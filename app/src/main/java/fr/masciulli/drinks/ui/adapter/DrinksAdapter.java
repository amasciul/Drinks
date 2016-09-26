package fr.masciulli.drinks.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import fr.masciulli.drinks.ui.view.RatioImageView;

public class DrinksAdapter extends RecyclerView.Adapter<TileViewHolder> {
    private static final int TYPE_34 = 0;
    private static final int TYPE_43 = 1;

    private static final float RATIO_34 = 3.0f / 4.0f;
    private static final float RATIO_43 = 4.0f / 3.0f;

    private List<Drink> drinks = new ArrayList<>();
    private List<Drink> filteredDrinks = new ArrayList<>();
    private Map<Drink, Integer> ratioMap = new HashMap<>();

    private ItemClickListener<Drink> listener;
    private Placeholders placeHolders = new Placeholders();

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

        Context context = holder.itemView.getContext();

        Picasso.with(context)
                .load(drink.getImageUrl())
                .fit()
                .placeholder(placeHolders.get(context, position))
                .centerCrop()
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
        filteredDrinks.clear();
        this.drinks.clear();

        this.drinks.addAll(drinks);
        filteredDrinks.addAll(drinks);

        fakeRatios();
        notifyDataSetChanged();
    }

    private void fakeRatios() {
        ratioMap.clear();
        for (int i = 0, size = drinks.size(); i < size; i++) {
            Drink drink = drinks.get(i);
            ratioMap.put(drink, i % 2 == 0 ? TYPE_34 : TYPE_43);
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

    public RecyclerView.LayoutManager craftLayoutManager(Context context) {
        int columnCount = context.getResources().getInteger(R.integer.column_count);

        return new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
    }
}
