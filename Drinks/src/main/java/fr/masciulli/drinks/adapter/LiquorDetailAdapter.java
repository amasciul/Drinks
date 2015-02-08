package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import fr.masciulli.drinks.view.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

import java.util.ArrayList;
import java.util.List;

public class LiquorDetailAdapter extends BaseAdapter {
    private List<Drink> drinks = new ArrayList<Drink>();
    private Context context;

    public LiquorDetailAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return drinks.size();
    }

    @Override
    public Drink getItem(int i) {
        return drinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {

        if (root == null) {
            root = LayoutInflater.from(context).inflate(R.layout.item_liquor_detail, parent, false);
        }

        final TextView nameView = Holder.get(root, R.id.name);
        final ImageView imageView = Holder.get(root, R.id.image);

        final Drink drink = getItem(i);

        nameView.setText(drink.name);
        Picasso.with(context).load(drink.imageUrl).into(imageView);

        return root;
    }

    public void update(List<Drink> drinks) {
        this.drinks = drinks;
        notifyDataSetChanged();
    }

    public ArrayList<Drink> getDrinks() {
        return (ArrayList<Drink>) drinks;
    }
}
