package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.view.ObservableScrollView;
import fr.masciulli.drinks.view.ScrollViewListener;

public class DrinkDetailFragment extends Fragment implements ScrollViewListener {

    private ImageView mImageView;
    private TextView mHistoryView;
    private TextView mNameView;
    private ObservableScrollView mScrollView;
    private LinearLayout mIngredientsLayout;
    private TextView mInstructionsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        mNameView = (TextView)root.findViewById(R.id.name);
        mImageView = (ImageView)root.findViewById(R.id.image);
        mHistoryView = (TextView)root.findViewById(R.id.history);
        mIngredientsLayout = (LinearLayout)root.findViewById(R.id.ingredients);
        mInstructionsView = (TextView)root.findViewById(R.id.instructions);
        mScrollView = (ObservableScrollView)root.findViewById(R.id.scroll);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");

        //TODO retrieve drink from provider with given name
        Drink mojito = new Drink("Mojito", "http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg");
        mojito.setHistory("The mojito is one of the most famous rum-based highballs. There are several versions of the mojito.");
        mojito.addIngredient("3 cl lime juice");
        mojito.addIngredient("6 leaves  of mint");
        mojito.addIngredient("2 teaspoons sugar");
        mojito.addIngredient("Soda water");
        mojito.setIntructions("Mint sprigs muddled with sugar and lime juice. Rum added and topped with soda water. Garnished with sprig of mint leaves. Served with a straw.");


        mNameView.setText(name);
        mHistoryView.setText(mojito.getHistory());
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);

        for (String ingredient : mojito.getIngredients()) {
            //TODO inflate TextView for each ingredient
        }

        mInstructionsView.setText(mojito.getInstructions());

        mScrollView.setScrollViewListener(this);

        return root;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        mImageView.setTop((0-y)/2);
    }

}
