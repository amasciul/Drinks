package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

import java.util.ArrayList;
import java.util.List;

public class LiquorListAdapter extends BaseAdapter {
    private List<Liquor> mLiquors = new ArrayList<Liquor>();
    private Context mContext;

    public LiquorListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mLiquors.size();
    }

    @Override
    public Liquor getItem(int i) {
        return mLiquors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {
        if (root == null) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_liquors_list, parent, false);
        }

        final Liquor liquor = getItem(i);

        final TextView nameView = Holder.get(root, R.id.name);
        final ImageView imageView = Holder.get(root, R.id.image);

        nameView.setText(liquor.name);
        Picasso.with(mContext).load(liquor.imageUrl).into(imageView);

        return root;
    }

    public void update(List<Liquor> liquors) {
        mLiquors = liquors;
        notifyDataSetChanged();
    }

    public ArrayList<Liquor> getLiquors() {
        return (ArrayList<Liquor>) mLiquors;
    }
}
