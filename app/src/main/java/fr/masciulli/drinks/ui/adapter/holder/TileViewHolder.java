package fr.masciulli.drinks.ui.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.view.RatioImageView;

public class TileViewHolder extends RecyclerView.ViewHolder {
    private final RatioImageView imageView;
    private final TextView nameView;

    public TileViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        nameView = itemView.findViewById(R.id.name);
    }

    public RatioImageView getImageView() {
        return imageView;
    }

    public TextView getNameView() {
        return nameView;
    }
}
