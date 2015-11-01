package fr.masciulli.drinks.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

public class LiquorRelatedAdapter extends RecyclerView.Adapter<LiquorRelatedAdapter.ViewHolder> {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_DRINK = 1;

    private final Liquor liquor;

    public LiquorRelatedAdapter(Liquor liquor) {
        this.liquor = liquor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root;
        switch (viewType) {
            case TYPE_HEADER:
                root = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_liquor_detail_header, parent, false);
                return new HeaderViewHolder(root);
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                headerViewHolder.historyView.setText(liquor.getHistory());
                headerViewHolder.wikipediaButton
                        .setText(context.getString(R.string.wikipedia, liquor.getName()));
                return;
            default:
                throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HEADER : TYPE_DRINK;
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
}
