package fr.masciulli.drinks.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinksAdapter extends RecyclerView.Adapter<DrinksAdapter.ViewHolder> {
    private List<Drink> drinks = new ArrayList<>();

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
    }

    @Override
    public int getItemCount() {
        return drinks.size();
    }

    public void setDrinks(List<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
