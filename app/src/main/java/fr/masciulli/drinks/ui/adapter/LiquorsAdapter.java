package fr.masciulli.drinks.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

import java.util.ArrayList;
import java.util.List;

public class LiquorsAdapter extends RecyclerView.Adapter<LiquorsAdapter.ViewHolder> {

    private ItemClickListener<Liquor> listener;
    private List<Liquor> liquors = new ArrayList<>();

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
        this.liquors = liquors;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.name);
        }
    }
}
