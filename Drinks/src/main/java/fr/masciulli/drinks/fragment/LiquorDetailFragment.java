package fr.masciulli.drinks.fragment;

import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.android_quantizer.lib.ColorQuantizer;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.activity.ToolbarActivity;
import fr.masciulli.drinks.adapter.LiquorDetailAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.util.AnimUtils;
import fr.masciulli.drinks.view.BlurTransformation;
import fr.masciulli.drinks.view.DrinksOnScrollListener;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

public class LiquorDetailFragment extends Fragment implements AbsListView.OnScrollListener {
    private static final int HEADERVIEWS_COUNT = 1;

    private static final long ANIM_IMAGE_ENTER_DURATION = 500;
    private static final long ANIM_TEXT_ENTER_DURATION = 500;
    private static final long ANIM_IMAGE_ENTER_STARTDELAY = 300;
    private static final long ANIM_COLORBOX_ENTER_DURATION = 200;

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();
    private static final String ARG_LIQUOR = "liquor";
    private static final String STATE_LIQUOR = "liquor";
    private static final String STATE_DRINKS = "drinks";
    //TODO move to specific class
    private static final String PREF_DRINKS_JSON = "drinks_json";

    @InjectView(R.id.image)
    ImageView mImageView;
    @InjectView(R.id.image_blurred)
    ImageView mBlurredImageView;
    @InjectView(R.id.history)
    TextView mHistoryView;
    @InjectView(R.id.progressbar)
    ProgressBar mProgressBar;
    @InjectView(R.id.wikipedia)
    Button mWikipediaButton;
    @InjectView(R.id.drinks_title)
    TextView mDrinksTitleView;
    @InjectView(R.id.colorbox)
    View mColorBox;
    @InjectView(R.id.color1)
    View mColorView1;
    @InjectView(R.id.color2)
    View mColorView2;
    @InjectView(R.id.color3)
    View mColorView3;
    @InjectView(R.id.color4)
    View mColorView4;

    private ListView mListView;
    private View mHeaderView;
    private Toolbar mToolbar;

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            mImageView.setImageBitmap(bitmap);
            new QuantizeBitmapTask().execute(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private LiquorDetailAdapter mDrinkAdapter;

    private DrinksOnScrollListener mAnimationOnScrollListener;

    private Liquor mLiquor;

    private int mImageViewHeight;

    private Callback<List<Drink>> mDrinksCallback = new Callback<List<Drink>>() {

        /**
         * Retrofit callback when drinks loaded
         * @param drinks
         * @param response
         */
        @Override
        public void success(List<Drink> drinks, Response response) {
            Log.d(getTag(), "Liquor detail related drinks loading/retrieving has succeeded");

            if (getActivity() == null) {
                return;
            }

            List<Drink> filteredDrinks = new ArrayList<Drink>();

            for (Drink drink : drinks) {
                for (String ingredient : drink.ingredients) {
                    if (ingredient.toLowerCase().contains(mLiquor.name.toLowerCase())) {
                        filteredDrinks.add(drink);
                        break;
                    }

                    //At this point, we know main name does not match
                    for (String otherName : mLiquor.otherNames) {
                        if (ingredient.toLowerCase().contains(otherName.toLowerCase())) {
                            filteredDrinks.add(drink);
                            break;
                        }
                    }

                }
            }

            if (filteredDrinks.size() > 0) {
                mDrinksTitleView.setVisibility(View.VISIBLE);
            } else {
                mDrinksTitleView.setVisibility(View.GONE);
            }

            mDrinkAdapter.update(filteredDrinks);
        }

        @Override
        public void failure(RetrofitError error) {

            Response resp = error.getResponse();
            String message;
            if (resp != null) {
                message = "response status : " + resp.getStatus();
            } else {
                message = "no response";
            }
            Log.e(getTag(), "Liquor detail related drinks loading has failed : " + message);

            if (getActivity() == null) {
                return;
            }

            if (error.isNetworkError()) {
                Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
            } else {
                String croutonText = String.format(getString(R.string.liquor_detail_drinks_loading_failed), mLiquor.name);
                Crouton.makeText(getActivity(), croutonText, Style.ALERT).show();
            }
        }
    };

    public static LiquorDetailFragment newInstance(Liquor liquor) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_LIQUOR, liquor);
        LiquorDetailFragment fragment = new LiquorDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mToolbar = ((ToolbarActivity) getActivity()).getToolbar();
        mToolbar.getBackground().setAlpha(0);

        View root = inflater.inflate(R.layout.fragment_liquor_detail, container, false);

        setHasOptionsMenu(true);

        mHeaderView = inflater.inflate(R.layout.header_liquor_detail, null);

        mListView = ButterKnife.findById(root, R.id.list);
        mListView.addHeaderView(mHeaderView);

        //View injection only done here because header has to be added before
        ButterKnife.inject(this, root);

        mDrinkAdapter = new LiquorDetailAdapter(getActivity());
        mListView.setAdapter(mDrinkAdapter);

        mLiquor = getArguments().getParcelable(ARG_LIQUOR);

        getActivity().setTitle(mLiquor.name);
        Picasso.with(getActivity()).load(mLiquor.imageUrl).into(mTarget);

        Transformation transformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(mLiquor.imageUrl).transform(transformation).into(mBlurredImageView);

        mImageViewHeight = (int) getResources().getDimension(R.dimen.liquor_detail_recipe_margin);
        mListView.setOnScrollListener(this);
        mAnimationOnScrollListener = new DrinksOnScrollListener(mListView);

        if (savedInstanceState != null) {
            mColorBox.setAlpha(1);
            Liquor liquor = savedInstanceState.getParcelable(STATE_LIQUOR);
            List<Drink> drinks = savedInstanceState.getParcelableArrayList(STATE_DRINKS);
            if (liquor != null && drinks != null) {
                onLiquorFound(mLiquor);
                mDrinksCallback.success(drinks, null);
            } else {
                refresh(mLiquor);
            }
        } else {
            ViewTreeObserver observer = mImageView.getViewTreeObserver();
            if (observer != null) {
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        runEnterAnimation();

                        return true;
                    }
                });
            } else {
                refresh(mLiquor);
            }
        }

        return root;
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.wikipedia)
    void goToWikipedia() {
        if (mLiquor == null) {
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLiquor.wikipedia)));
    }

    @SuppressWarnings("unused")
    @OnItemClick(R.id.list)
    void openRelatedDrinkDetail(int position, View view) {
        Drink drink = mDrinkAdapter.getItem(position - HEADERVIEWS_COUNT);

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.putExtra(DrinkDetailActivity.ARG_DRINK, drink);
        startActivity(intent);
    }

    private void runEnterAnimation() {

        Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                refresh(mLiquor);
            }
        };

        mImageView.setTranslationY(-mImageView.getHeight());

        ViewPropertyAnimator animator = mImageView.animate().setDuration(ANIM_IMAGE_ENTER_DURATION).
                setStartDelay(ANIM_IMAGE_ENTER_STARTDELAY).
                translationY(0).
                setInterpolator(sDecelerator);

        Runnable animateColorBoxRunnable = new Runnable() {
            @Override
            public void run() {
                mColorBox.animate()
                        .alpha(1)
                        .setDuration(ANIM_COLORBOX_ENTER_DURATION)
                        .setInterpolator(sDecelerator);
            }
        };

        AnimUtils.scheduleStartAction(animator, refreshRunnable, ANIM_IMAGE_ENTER_STARTDELAY);
        AnimUtils.scheduleEndAction(animator, animateColorBoxRunnable, ANIM_IMAGE_ENTER_DURATION, ANIM_IMAGE_ENTER_STARTDELAY);
    }

    public void onLiquorFound(Liquor liquor) {
        mLiquor = liquor;
        if (getActivity() == null) {
            return;
        }

        mProgressBar.setVisibility(View.GONE);

        mHistoryView.setText(liquor.history);
        mWikipediaButton.setText(String.format(getString(R.string.liquor_detail_wikipedia), liquor.name));
        mDrinksTitleView.setText(String.format(getString(R.string.liquor_detail_drinks), liquor.name));

        ViewTreeObserver observer = mListView.getViewTreeObserver();
        if (observer != null) {
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    mListView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mListView.setAlpha(0);
                    mListView.animate().setDuration(ANIM_TEXT_ENTER_DURATION).
                            alpha(1).
                            setInterpolator(sDecelerator);

                    return true;
                }
            });
        }
        mListView.setVisibility(View.VISIBLE);
    }

    public void refresh(Liquor liquor) {
        if (getActivity() == null) {
            return;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains(PREF_DRINKS_JSON)) {
            Gson gson = new Gson();
            //TODO async
            List<Drink> drinks = gson.fromJson(preferences.getString(PREF_DRINKS_JSON, "null"), new TypeToken<List<Drink>>(){}.getType());

            //TODO do not use retrofit callback
            mDrinksCallback.success(drinks, null);
        } else {
            DrinksProvider.getAllDrinks(mDrinksCallback);
        }
        onLiquorFound(liquor);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.liquor_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.retry:
                refresh(mLiquor);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int state) {
        if (mAnimationOnScrollListener != null) {
            mAnimationOnScrollListener.onScrollStateChanged(listView, state);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mAnimationOnScrollListener != null) {
            mAnimationOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
        float alpha = 2 * (float) -mHeaderView.getTop() / (float) mImageViewHeight;
        if (alpha > 1) {
            alpha = 1;
        } else if (alpha < 0) {
            alpha = 0;
        }
        mBlurredImageView.setAlpha(alpha);

        mImageView.setTop(mHeaderView.getTop() / 2);
        mImageView.setBottom(mImageViewHeight + mHeaderView.getTop());
        mBlurredImageView.setTop(mHeaderView.getTop() / 2);
        mBlurredImageView.setBottom(mImageViewHeight + mHeaderView.getTop());

        mToolbar.getBackground().setAlpha((int) (alpha * 255));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // TODO do not use getCount
        if (mLiquor != null && mDrinkAdapter.getCount() > 0) {
            outState.putParcelable(STATE_LIQUOR, mLiquor);
            outState.putParcelableArrayList(STATE_DRINKS, mDrinkAdapter.getDrinks());
        }
    }

    private class QuantizeBitmapTask extends AsyncTask<Bitmap, Void, ArrayList<Integer>> {
        @Override
        protected ArrayList<Integer> doInBackground(Bitmap... bitmaps) {

            Bitmap originalBitmap = bitmaps[0];

            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();

            Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, originalWidth / 16, originalHeight / 16, true);

            ArrayList<Integer> quantizedColors = new ColorQuantizer().load(bitmap).quantize().getQuantizedColors();

            //TODO figure out why only two colors for some images

            return quantizedColors;
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> colors) {
            View[] colorViews = new View[]{mColorView1, mColorView2, mColorView3, mColorView4};
            for (int i = 0; i < colors.size() && i < 4; i++) {
                colorViews[i].setBackgroundColor(colors.get(i));
            }
        }
    }
}
