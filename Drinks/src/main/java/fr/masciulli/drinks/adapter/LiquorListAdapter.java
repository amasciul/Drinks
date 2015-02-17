package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

import java.util.ArrayList;
import java.util.List;

public class LiquorListAdapter extends RecyclerView.Adapter<LiquorListAdapter.ViewHolder> {
    private List<Liquor> liquors = new ArrayList<Liquor>();
    private Context context;
    private OnItemClickListener onItemClickListener;

    public LiquorListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_liquors_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Liquor liquor = liquors.get(position);

        holder.nameView.setText(liquor.name);
        Picasso.with(context).load(liquor.imageUrl).into(holder.imageView);
        if (onItemClickListener != null) {
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return liquors.size();
    }

    public void update(List<Liquor> liquors) {
        this.liquors = liquors;
        notifyDataSetChanged();
    }

    public ArrayList<Liquor> getLiquors() {
        return (ArrayList<Liquor>) liquors;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final TextView nameView;
        private final ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            nameView = (TextView) itemView.findViewById(R.id.name);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }
}
