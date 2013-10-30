package fr.masciulli.drinks.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.Holder;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

import com.squareup.picasso.Picasso;

public class DrinksListAdapter extends BaseAdapter {
    private List<Drink> mDrinks = new ArrayList<Drink>();
    private Context mContext;


    public DrinksListAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 100; i++) {
            mDrinks.add(new Drink("Amaretto Frost", "http://www.smallscreennetwork.com/videos/cocktail_spirit/morgenthaler-method-amaretto-sour.jpg"));
            mDrinks.add(new Drink("Americano", "http://www.ganzomag.com/wp-content/uploads/2012/05/americano-cocktail1.jpg"));
            mDrinks.add(new Drink("Tom Collins", "http://www.vinumimporting.com/wp-content/uploads/2012/06/tom-collins.jpg"));
            mDrinks.add(new Drink("Mojito", "http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg"));
            mDrinks.add(new Drink("Dry Martini", "http://www.cocktailrendezvous.com/images.php?f=files/recipes/images/martini.jpg&w=616&h=347&c=1"));
        }
    }

    @Override
    public int getCount() {
        return mDrinks.size();
    }

    @Override
    public Drink getItem(int i) {
        return mDrinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View root, ViewGroup parent) {

        if (root == null) {
            root = LayoutInflater.from(mContext).inflate(R.layout.item_drink, parent, false);
        }

        final ImageView imageView = Holder.get(root, R.id.image);
        final TextView nameView = Holder.get(root, R.id.name);

        final Drink drink = getItem(i);

        nameView.setText(drink.getName());
        Picasso.with(mContext).load(drink.getImageURL()).into(imageView);

        return root;
    }
}
