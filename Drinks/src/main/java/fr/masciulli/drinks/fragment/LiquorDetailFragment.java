package fr.masciulli.drinks.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.view.BlurTransformation;
import fr.masciulli.drinks.view.ObservableScrollView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiquorDetailFragment extends Fragment implements Callback<Liquor> {
    private ImageView mImageView;
    private ImageView mBlurredImageView;
    private TextView mHistoryView;
    private ObservableScrollView mScrollView;
    private ProgressBar mProgressBar;
    private Button mWikipediaButton;

    private MenuItem mRetryAction;

    private int mLiquorId;
    private Transformation mTransformation;
    private Liquor mLiquor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_liquor_detail, container, false);

        mImageView = (ImageView)root.findViewById(R.id.image);
        mBlurredImageView = (ImageView)root.findViewById(R.id.image_blurred);
        mHistoryView = (TextView)root.findViewById(R.id.history);
        mScrollView = (ObservableScrollView)root.findViewById(R.id.scroll);
        mProgressBar = (ProgressBar)root.findViewById(R.id.progressbar);
        mWikipediaButton = (Button)root.findViewById(R.id.wikipedia);

        Intent intent = getActivity().getIntent();
        mLiquorId = intent.getIntExtra("liquor_id", 1);
        String name = intent.getStringExtra("liquor_name");
        String imageUrl = intent.getStringExtra("liquor_imageurl");

        getActivity().setTitle(name);
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);

        mTransformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(imageUrl).transform(mTransformation).into(mBlurredImageView);

        //mImageViewHeight = (int)getResources().getDimension(R.dimen.drink_detail_recipe_margin);
        //mScrollView.setScrollViewListener(this);
        mWikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLiquor == null) return;
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLiquor.wikipedia)));
            }
        });

        if (savedInstanceState != null) {
            Liquor liquor = savedInstanceState.getParcelable("liquor");
            if (liquor != null) {
                success(liquor, null);
            }
            else {
                refresh();
            }
        } else {
            refresh();
        }

        mWikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLiquor == null) return;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLiquor.wikipedia)));
            }
        });

        return root;
    }

    private void refresh() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (mRetryAction != null) mRetryAction.setVisible(false);
        DrinksProvider.getLiquor(mLiquorId, this);
    }

    @Override
    public void success(Liquor liquor, Response response) {
        Log.d(getTag(), "Liquor detail loading has succeeded");

        mLiquor = liquor;

        mProgressBar.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);

        getActivity().setTitle(liquor.name);

        Picasso.with(getActivity()).load(liquor.imageUrl).into(mImageView);
        Picasso.with(getActivity()).load(liquor.imageUrl).transform(mTransformation).into(mBlurredImageView);

        mHistoryView.setText(liquor.history);
        mWikipediaButton.setText(String.format(String.format(getString(R.string.liquor_detail_wikipedia), liquor.name)));
    }

    @Override
    public void failure(RetrofitError error) {
        mProgressBar.setVisibility(View.GONE);
        if (mRetryAction != null) mRetryAction.setVisible(true);
        if (error.isNetworkError()) {
            Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), R.string.liquor_detail_loading_failed, Style.ALERT).show();
        }

        Response resp = error.getResponse();
        String message;
        if(resp != null) {
            message = "response status : " + resp.getStatus();
        } else {
            message = "no response";
        }
        Log.e(getTag(), "Liquor detail loading has failed : " + message);

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
