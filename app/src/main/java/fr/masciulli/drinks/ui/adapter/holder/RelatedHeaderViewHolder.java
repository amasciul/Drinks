package fr.masciulli.drinks.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import fr.masciulli.drinks.R;

public class RelatedHeaderViewHolder extends RecyclerView.ViewHolder {
    private final TextView historyView;
    private final Button wikipediaButton;
    private final TextView relatedDrinksTitle;

    public RelatedHeaderViewHolder(View itemView) {
        super(itemView);
        historyView = itemView.findViewById(R.id.history);
        wikipediaButton = itemView.findViewById(R.id.wikipedia);
        relatedDrinksTitle = itemView.findViewById(R.id.related_drinks_title);
    }

    public TextView getHistoryView() {
        return historyView;
    }

    public Button getWikipediaButton() {
        return wikipediaButton;
    }

    public TextView getRelatedDrinksTitle() {
        return relatedDrinksTitle;
    }
}
