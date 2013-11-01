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
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.DrinksListItem;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.squareup.picasso.Picasso;

public class DrinksListAdapter extends BaseAdapter implements Callback<List<DrinksListItem>> {
    private List<DrinksListItem> mDrinks = new ArrayList<DrinksListItem>();
    private Context mContext;


    public DrinksListAdapter(Context context) {
        mContext = context;
        DrinksProvider.getDrinksList(this);
    }

    @Override
    public int getCount() {
        return mDrinks.size();
    }

    @Override
    public DrinksListItem getItem(int i) {
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

        final DrinksListItem drink = getItem(i);

        nameView.setText(drink.name);
        Picasso.with(mContext).load(drink.imageURL).into(imageView);

        return root;
    }

    @Override
    public void success(List<DrinksListItem> drinksListItems, Response response) {
        Log.d(this.getClass().getName(), "Drinks list loading has succeeded");
        mDrinks = drinksListItems;
        notifyDataSetChanged();
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.e(this.getClass().getName(), "Drinks list loading has failed");
    }
}
