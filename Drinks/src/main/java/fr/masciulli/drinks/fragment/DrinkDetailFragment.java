package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.DrinkDetailItem;
import fr.masciulli.drinks.view.ObservableScrollView;
import fr.masciulli.drinks.view.ScrollViewListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinkDetailFragment extends Fragment implements ScrollViewListener, Callback<DrinkDetailItem> {

    private ImageView mImageView;
    private TextView mHistoryView;
    private ObservableScrollView mScrollView;
    private TextView mIngredientsView;
    private TextView mInstructionsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        mImageView = (ImageView)root.findViewById(R.id.image);
        mHistoryView = (TextView)root.findViewById(R.id.history);
        mIngredientsView = (TextView)root.findViewById(R.id.ingredients);
        mInstructionsView = (TextView)root.findViewById(R.id.instructions);
        mScrollView = (ObservableScrollView)root.findViewById(R.id.scroll);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");
        String id = intent.getStringExtra("drink_id");

        DrinksProvider.getDrink(id, this);
        Toast.makeText(getActivity(), "Retrieving drink with id : " + id, Toast.LENGTH_LONG).show();

        Picasso.with(getActivity()).load(imageUrl).into(mImageView);

        mScrollView.setScrollViewListener(this);

        return root;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        mImageView.setTop((0-y)/2);
    }

    @Override
    public void success(DrinkDetailItem drink, Response response) {
        Log.d(this.getClass().getName(), "Drink detail loading has succeeded");

        getActivity().setTitle(drink.getName());
        Picasso.with(getActivity()).load(drink.getImageURL()).into(mImageView);
        mHistoryView.setText(drink.getHistory());


        String htmlString = "";
        int i = 0;
        for (String ingredient : drink.getIngredients()) {
            if (++i == drink.getIngredients().size()) {
                htmlString += "&#8226; " + ingredient;
            } else {
                htmlString += "&#8226; " + ingredient + "<br>";
            }
        }
        mIngredientsView.setText(Html.fromHtml(htmlString));

        mInstructionsView.setText(drink.getInstructions());
    }

    @Override
    public void failure(RetrofitError retrofitError) {
        Log.e(this.getClass().getName(), "Drink detail loading has failed");
    }
}
