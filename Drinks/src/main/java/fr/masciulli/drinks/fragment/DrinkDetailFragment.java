package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.view.ObservableScrollView;
import fr.masciulli.drinks.view.ScrollViewListener;

public class DrinkDetailFragment extends Fragment implements ScrollViewListener {

    private ImageView mImageView;
    private TextView mRecipeView;
    private TextView mNameView;
    private ObservableScrollView mScrollView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        mNameView = (TextView)root.findViewById(R.id.name);
        mImageView = (ImageView)root.findViewById(R.id.image);
        mRecipeView = (TextView)root.findViewById(R.id.history);
        mScrollView = (ObservableScrollView)root.findViewById(R.id.scroll);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");

        mNameView.setText(name);
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);
        for (int i=0; i< 10; i++) {
            mRecipeView.setText(mRecipeView.getText() + getResources().getString(R.string.loremipsum) + "\n\n");
        }

        mScrollView.setScrollViewListener(this);


        return root;
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        mImageView.setTop((0-y)/2);
    }

}
