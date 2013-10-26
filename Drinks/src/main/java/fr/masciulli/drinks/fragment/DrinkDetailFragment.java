package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;

public class DrinkDetailFragment extends Fragment implements AbsListView.OnScrollListener {

    private ImageView mImageView;
    private TextView mRecipeView;
    private TextView mNameView;
    private ListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        mNameView = (TextView)root.findViewById(R.id.name);
        mImageView = (ImageView)root.findViewById(R.id.image);
        mRecipeView = (TextView)inflater.inflate(R.layout.recipe, null);
        mListView = (ListView)root.findViewById(R.id.list);

        Intent intent = getActivity().getIntent();
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");

        mNameView.setText(name);
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);
        for (int i=0; i< 10; i++) {
            mRecipeView.setText(mRecipeView.getText() + getResources().getString(R.string.loremipsum) + "\n\n");
        }

        mListView.addHeaderView(mRecipeView);

        String[] names = new String[] { "Amaretto" };

        // WARNING : always call setAdapter after addHeaderView
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_single_choice,
                android.R.id.text1, names));
        mListView.setOnScrollListener(this);

        return root;
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mImageView.setTop(mRecipeView.getTop()/2);
    }
}
