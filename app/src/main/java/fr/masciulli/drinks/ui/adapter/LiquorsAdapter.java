package fr.masciulli.drinks.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.ui.adapter.holder.TileViewHolder;
import fr.masciulli.drinks.ui.view.RatioImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiquorsAdapter extends RecyclerView.Adapter<TileViewHolder> {
    private static final int TYPE_34 = 0;
    private static final int TYPE_43 = 1;
    private static final float RATIO_34 = 3.0f / 4.0f;
    private static final float RATIO_43 = 4.0f / 3.0f;

    private static int[] ratios = new int[]{TYPE_34, TYPE_43};

    private ItemClickListener<Liquor> listener;
    private List<Liquor> liquors = new ArrayList<>();
    private Map<Liquor, Integer> ratioMap = new HashMap<>();
    private Placeholders placeHolders = new Placeholders();

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tile, parent, false);
        return new TileViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final TileViewHolder holder, int position) {
        final Liquor liquor = liquors.get(position);
        holder.getNameView().setText(liquor.getName());

        RatioImageView imageView = holder.getImageView();
        switch (getItemViewType(position)) {
            case TYPE_34:
                imageView.setRatio(RATIO_34);
                break;
            case TYPE_43:
                imageView.setRatio(RATIO_43);
                break;
            default:
                throw new IllegalArgumentException("Unknown type");
        }

        Context context = holder.itemView.getContext();

        Picasso.with(context)
                .load(liquor.getImageUrl())
                .fit()
                .placeholder(placeHolders.get(context, position))
                .centerCrop()
                .into(imageView);

        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onItemClick(holder.getAdapterPosition(), liquor));
        }
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

    public ArrayList<Liquor> getLiquors() {
        return new ArrayList<>(liquors);
    }

    public RecyclerView.LayoutManager craftLayoutManager(Context context) {
        int columnCount = context.getResources().getInteger(R.integer.column_count);
        return new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
    }
}
