package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

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
            root = LayoutInflater.from(mContext).inflate(R.layout.item_ingredient, parent, false);
        }

        final TextView nameView = Holder.get(root, R.id.name);
        nameView.setText(getItem(i).name);

        return root;
    }

    public void update(List<Liquor> liquors) {
        mLiquors = liquors;
        notifyDataSetChanged();
    }
}
