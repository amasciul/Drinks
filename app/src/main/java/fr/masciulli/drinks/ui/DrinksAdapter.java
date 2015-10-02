package fr.masciulli.drinks.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.ViewHolder> {
    private List<Drink> drinks = new ArrayList<>();
    private float[] ratios = new float[]{0.75f, 4.0f / 3.0f};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drink drink = drinks.get(position);
        holder.nameView.setText(drink.name);
        holder.imageView.setRatio(drink.ratio);
        Picasso.with(holder.itemView.getContext()).load(drink.imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
        fakeRatios();
        notifyDataSetChanged();
    }

    private void fakeRatios() {
        //TODO remove this and use ratios given by server
        for (Drink drink : drinks) {
            drink.ratio = ratios[new Random().nextInt(2)];
        }
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
