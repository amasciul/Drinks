package fr.masciulli.drinks.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;

public class LiquorRelatedAdapter extends RecyclerView.Adapter<LiquorRelatedAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_DRINK = 1;

    private Liquor liquor;
    private List<Drink> drinks = new ArrayList<>();

    private ItemClickListener<Liquor> wikipediaClickListener;
    private ItemClickListener<Drink> drinkClickListener;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root;
        switch (viewType) {
            case TYPE_HEADER:
                root = inflater.inflate(R.layout.item_liquor_detail_header, parent, false);
                return new HeaderViewHolder(root);
            case TYPE_DRINK:
                root = inflater.inflate(R.layout.item_liquor_detail_drink, parent, false);
                return new DrinkViewHolder(root);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Context context = holder.itemView.getContext();
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.historyView.setText(liquor.getHistory());
                headerViewHolder.wikipediaButton
                        .setText(context.getString(R.string.wikipedia, liquor.getName()));
                if (wikipediaClickListener != null) {
                    headerViewHolder.wikipediaButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wikipediaClickListener.onItemClick(position, liquor);
                        }
                    });
                }
                return;

            case TYPE_DRINK:
                final Drink drink = drinks.get(position - 1);
                DrinkViewHolder drinkViewHolder = (DrinkViewHolder) holder;
                drinkViewHolder.nameView.setText(drink.getName());
                if (drinkClickListener != null) {
                    drinkViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            drinkClickListener.onItemClick(position, drink);
                        }
                    });
                }
                Picasso.with(context).load(drink.getImageUrl()).into(drinkViewHolder.imageView);
                return;
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public int getItemCount() {
        return drinks.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_DRINK;
    }

    public void setLiquor(Liquor liquor) {
        this.liquor = liquor;
        notifyItemChanged(0);
    }

    public void setRelatedDrinks(List<Drink> drinks) {
        this.drinks.clear();
        this.drinks.addAll(drinks);
        notifyItemRangeChanged(1, drinks.size());
    }

    public void setWikipediaClickListener(ItemClickListener<Liquor> listener) {
        wikipediaClickListener = listener;
    }

    public void setDrinkClickListener(ItemClickListener<Drink> listener) {
        drinkClickListener = listener;
    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    private class HeaderViewHolder extends ViewHolder {
        private final TextView historyView;
        private final Button wikipediaButton;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            historyView = (TextView) itemView.findViewById(R.id.history);
            wikipediaButton = (Button) itemView.findViewById(R.id.wikipedia);
        }
    }

    private class DrinkViewHolder extends ViewHolder {
        private final TextView nameView;
        private final ImageView imageView;

        public DrinkViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
