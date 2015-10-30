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
import java.util.List;

public class LiquorsAdapter extends RecyclerView.Adapter<LiquorsAdapter.ViewHolder> {
    private static float[] ratios = new float[]{0.75f, 4.0f / 3.0f};

    private ItemClickListener<Liquor> listener;
    private ArrayList<Liquor> liquors = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_liquor, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Liquor liquor = liquors.get(position);
        holder.nameView.setText(liquor.getName());
        holder.imageView.setRatio(liquor.getRatio());
        Picasso.with(holder.itemView.getContext())
                .load(liquor.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(position, liquor);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return liquors.size();
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
        // TODO remove this and use ratios given by server
        for (int i = 0, size = liquors.size(); i < size; i++) {
            Liquor liquor = liquors.get(i);
            liquor.setRatio(ratios[i % ratios.length]);
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
