package fr.masciulli.drinks.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.ui.view.RatioImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquorsAdapter extends RecyclerView.Adapter<LiquorsAdapter.ViewHolder> {
    private static final int TYPE_34 = 0;
    private static final int TYPE_43 = 1;
    private static final float RATIO_34 = 3.0f / 4.0f;
    private static final float RATIO_43 = 4.0f / 3.0f;

    private static int[] ratios = new int[]{TYPE_34, TYPE_43};

    private ItemClickListener<Liquor> listener;
    private ArrayList<Liquor> liquors = new ArrayList<>();
    private Map<Liquor, Integer> ratioMap = new HashMap<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_liquor, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Liquor liquor = liquors.get(position);
        holder.nameView.setText(liquor.getName());
        switch (getItemViewType(position)) {
            case TYPE_34:
                holder.imageView.setRatio(RATIO_34);
                break;
            case TYPE_43:
                holder.imageView.setRatio(RATIO_43);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }
        Picasso.with(holder.itemView.getContext())
                .load(liquor.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(holder.getAdapterPosition(), liquor);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liquors.size();
    }

    @Override
    public int getItemViewType(int position) {
        Liquor liquor = liquors.get(position);
        return ratioMap.get(liquor);
    }

    public void setItemClickListener(ItemClickListener<Liquor> listener) {
        this.listener = listener;
    }

    public void setLiquors(List<Liquor> liquors) {
        this.liquors.clear();
        this.liquors.addAll(liquors);
        fakeRatios();
        notifyDataSetChanged();
    }

    private void fakeRatios() {
        ratioMap.clear();
        for (int i = 0, size = liquors.size(); i < size; i++) {
            Liquor liquor = liquors.get(i);
            ratioMap.put(liquor, ratios[i % ratios.length]);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;
        private final RatioImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
            imageView = (RatioImageView) itemView.findViewById(R.id.image);
        }
    }
}
