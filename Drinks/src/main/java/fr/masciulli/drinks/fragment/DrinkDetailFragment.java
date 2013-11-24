package fr.masciulli.drinks.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.view.BlurTransformation;
import fr.masciulli.drinks.view.ObservableScrollView;
import fr.masciulli.drinks.view.ScrollViewListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DrinkDetailFragment extends RefreshableFragment implements ScrollViewListener, Callback<Drink> {

    private ImageView mImageView;
    private ImageView mBlurredImageView;
    private TextView mHistoryView;
    private ObservableScrollView mScrollView;
    private TextView mIngredientsView;
    private TextView mInstructionsView;
    private ProgressBar mProgressBar;
    private Button mWikipediaButton;

    private MenuItem mRetryAction;

    private int mImageViewHeight;

    private int mDrinkId;
    private Transformation mTransformation;

    private Drink mDrink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        setHasOptionsMenu(true);

        mImageView = (ImageView)root.findViewById(R.id.image);
        mBlurredImageView = (ImageView)root.findViewById(R.id.image_blurred);
        mHistoryView = (TextView)root.findViewById(R.id.history);
        mIngredientsView = (TextView)root.findViewById(R.id.ingredients);
        mInstructionsView = (TextView)root.findViewById(R.id.instructions);
        mScrollView = (ObservableScrollView)root.findViewById(R.id.scroll);
        mProgressBar = (ProgressBar)root.findViewById(R.id.progressbar);
        mWikipediaButton = (Button)root.findViewById(R.id.wikipedia);

        Intent intent = getActivity().getIntent();
        mDrinkId = intent.getIntExtra("drink_id", 1);
        String name = intent.getStringExtra("drink_name");
        String imageUrl = intent.getStringExtra("drink_imageurl");

        getActivity().setTitle(name);
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);

        mTransformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(imageUrl).transform(mTransformation).into(mBlurredImageView);

        mImageViewHeight = (int)getResources().getDimension(R.dimen.drink_detail_recipe_margin);
        mScrollView.setScrollViewListener(this);
        mWikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrink == null) return;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mDrink.wikipedia)));
            }
        });

        if (savedInstanceState != null) {
            Drink drink = savedInstanceState.getParcelable("drink");
            if (drink != null) {
                success(drink, null);
            }
            else {
                refresh();
            }
        } else {
            refresh();
        }

        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mDrink != null) {
            outState.putParcelable("drink", mDrink);
            Log.d(getTag(), "Instance state saved");
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        float alpha = 2 * (float) y / (float) mImageViewHeight;
        if (alpha > 1) {
            alpha = 1;
        }
        mBlurredImageView.setAlpha(alpha);

        mImageView.setTop((0-y)/2);
        mImageView.setBottom(mImageViewHeight - y);
        mBlurredImageView.setTop((0-y)/2);
        mBlurredImageView.setBottom(mImageViewHeight - y);
    }

    @Override
    public void success(Drink drink, Response response) {
        Log.d(getTag(), "Drink detail loading has succeeded");

        mDrink = drink;

        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);

        getActivity().setTitle(drink.name);

        Picasso.with(getActivity()).load(drink.imageUrl).into(mImageView);
        Picasso.with(getActivity()).load(drink.imageUrl).transform(mTransformation).into(mBlurredImageView);

        mHistoryView.setText(drink.history);


        String htmlString = "";
        int i = 0;
        for (String ingredient : drink.ingredients) {
            if (++i == drink.ingredients.size()) {
                htmlString += "&#8226; " + ingredient;
            } else {
                htmlString += "&#8226; " + ingredient + "<br>";
            }
        }
        mIngredientsView.setText(Html.fromHtml(htmlString));

        mInstructionsView.setText(drink.instructions);
        mWikipediaButton.setText(String.format(getString(R.string.drink_detail_wikipedia), drink.name));
    }

    @Override
    public void failure(RetrofitError error) {
        mProgressBar.setVisibility(View.GONE);
        if (mRetryAction != null) mRetryAction.setVisible(true);
        if (error.isNetworkError()) {
            Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), R.string.drink_detail_loading_failed, Style.ALERT).show();
        }

        Response resp = error.getResponse();
        String message;
        if(resp != null) {
            message = "response status : " + resp.getStatus();
        } else {
            message = "no response";
        }
        Log.e(getTag(), "Drink detail loading has failed : " + message);
    }

    @Override
    public void refresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mRetryAction != null) mRetryAction.setVisible(false);
        DrinksProvider.getDrink(mDrinkId, this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drink_detail, menu);
        mRetryAction = menu.findItem(R.id.retry);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.retry:
                refresh();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
