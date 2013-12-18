package fr.masciulli.drinks.fragment;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.DrinkDetailActivity;
import fr.masciulli.drinks.adapter.LiquorDetailAdapter;
import fr.masciulli.drinks.data.DrinksProvider;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.view.BlurTransformation;
import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LiquorDetailFragment extends Fragment implements Callback<Liquor>, AbsListView.OnScrollListener, AdapterView.OnItemClickListener {
    private static final int HEADERVIEWS_COUNT = 1;

    private static final long ANIM_IMAGE_ENTER_DURATION = 500;
    private static final long ANIM_IMAGE_EXIT_DURATION = 500;
    private static final long ANIM_TEXT_ENTER_DURATION = 500;
    private static final long ANIM_TEXT_EXIT_DURATION = 300;

    private static final TimeInterpolator sDecelerator = new DecelerateInterpolator();

    private ImageView mImageView;
    private ImageView mBlurredImageView;
    private TextView mHistoryView;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private Button mWikipediaButton;
    private View mHeaderView;
    private TextView mDrinksTitleView;

    private LiquorDetailAdapter mDrinkAdapter;

    private MenuItem mRetryAction;

    private int mLiquorId;
    private Transformation mTransformation;
    private Liquor mLiquor;

    private int mImageViewHeight;

    private int mTopDelta;
    private int mPreviousItemTop;
    private Drawable mBackground;
    private long mPreviousItemHeight;
    private int mPreviousOrientation;

    private Callback<List<Drink>> mDrinksCallback = new Callback<List<Drink>>() {
        @Override
        public void success(List<Drink> drinks, Response response) {
            Log.d(getTag(), "Liquor detail related drinks loading has succeeded");

            if (getActivity() == null) return;

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

            if (getActivity() == null) return;

            if (error.isNetworkError()) {
                Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
            } else {
                Crouton.makeText(getActivity(), R.string.liquor_detail_drinks_loading_failed, Style.ALERT).show();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_liquor_detail, container, false);

        mImageView = (ImageView) root.findViewById(R.id.image);
        mBlurredImageView = (ImageView) root.findViewById(R.id.image_blurred);
        mListView = (ListView) root.findViewById(R.id.scroll);
        mProgressBar = (ProgressBar) root.findViewById(R.id.progressbar);

        mHeaderView = inflater.inflate(R.layout.header_liquor_detail, null);
        mListView.addHeaderView(mHeaderView);

        mHistoryView = (TextView) mHeaderView.findViewById(R.id.history);
        mWikipediaButton = (Button) mHeaderView.findViewById(R.id.wikipedia);
        mDrinksTitleView = (TextView) mHeaderView.findViewById(R.id.drinks_title);

        mDrinkAdapter = new LiquorDetailAdapter(getActivity());
        mListView.setAdapter(mDrinkAdapter);
        mListView.setOnItemClickListener(this);

        Intent intent = getActivity().getIntent();
        mLiquorId = intent.getIntExtra("liquor_id", 1);
        String name = intent.getStringExtra("liquor_name");
        String imageUrl = intent.getStringExtra("liquor_imageurl");

        // Data needed for animations
        mPreviousItemHeight = intent.getIntExtra("height", 0);
        mPreviousItemTop = intent.getIntExtra("top", 0);
        mPreviousOrientation = intent.getIntExtra("orientation", 0);

        mBackground = root.getBackground();

        getActivity().setTitle(name);
        Picasso.with(getActivity()).load(imageUrl).into(mImageView);

        mTransformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(imageUrl).transform(mTransformation).into(mBlurredImageView);

        mImageViewHeight = (int) getResources().getDimension(R.dimen.liquor_detail_recipe_margin);
        mListView.setOnScrollListener(this);

        mWikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLiquor == null) return;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mLiquor.wikipedia)));
            }
        });

        if (savedInstanceState != null) {
            Liquor liquor = savedInstanceState.getParcelable("liquor");
            if (liquor != null) {
                success(liquor, null);
            } else {
                refresh();
            }
        } else {
            ViewTreeObserver observer = mImageView.getViewTreeObserver();
            if (observer != null) {
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);

                        int[] screenLocation = new int[2];
                        mImageView.getLocationOnScreen(screenLocation);
                        mTopDelta = mPreviousItemTop - screenLocation[1];

                        runEnterAnimation();

                        return true;
                    }
                });
            }
        }

        return root;
    }

    @DebugLog
    private void runEnterAnimation() {
        mImageView.setTranslationY(mTopDelta);

        ViewPropertyAnimator animator = mImageView.animate().setDuration(ANIM_IMAGE_ENTER_DURATION).
                translationX(0).translationY(0).
                setInterpolator(sDecelerator);

        if (Build.VERSION.SDK_INT >= 16) {
            animator.withEndAction(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }

        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mBackground, "alpha", 0, 255);
        bgAnim.setDuration(ANIM_IMAGE_ENTER_DURATION);
        bgAnim.start();

        if (Build.VERSION.SDK_INT < 16) {
            Timer timer = new Timer();
            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    refresh();
                }
            };
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    handler.obtainMessage().sendToTarget();
                }
            };
            timer.schedule(task, ANIM_IMAGE_ENTER_DURATION);
        }

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

        if (getActivity() == null) return;

        DrinksProvider.getAllDrinks(mDrinksCallback);

        mProgressBar.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);

        getActivity().setTitle(liquor.name);

        Picasso.with(getActivity()).load(liquor.imageUrl).into(mImageView);
        Picasso.with(getActivity()).load(liquor.imageUrl).transform(mTransformation).into(mBlurredImageView);

        mHistoryView.setText(liquor.history);
        mWikipediaButton.setText(String.format(getString(R.string.liquor_detail_wikipedia), liquor.name));
        mDrinksTitleView.setText(String.format(getString(R.string.liquor_detail_drinks), liquor.name));
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
        Log.e(getTag(), "Liquor detail loading has failed : " + message);

        if (getActivity() == null) return;

        mProgressBar.setVisibility(View.GONE);
        if (mRetryAction != null) mRetryAction.setVisible(true);
        if (error.isNetworkError()) {
            Crouton.makeText(getActivity(), getString(R.string.network_error), Style.ALERT).show();
        } else {
            Crouton.makeText(getActivity(), R.string.liquor_detail_loading_failed, Style.ALERT).show();
        }

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

    @Override
    public void onScrollStateChanged(AbsListView listView, int state) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        float alpha = 2 * (float) -mHeaderView.getTop() / (float) mImageViewHeight;
        if (alpha > 1) {
            alpha = 1;
        }
        mBlurredImageView.setAlpha(alpha);

        mImageView.setTop(mHeaderView.getTop() / 2);
        mImageView.setBottom(mImageViewHeight + mHeaderView.getTop());
        mBlurredImageView.setTop(mHeaderView.getTop() / 2);
        mBlurredImageView.setBottom(mImageViewHeight + mHeaderView.getTop());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Drink drink = mDrinkAdapter.getItem(position - HEADERVIEWS_COUNT);

        // Data needed for animations in sub activity
        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);
        int orientation = getResources().getConfiguration().orientation;

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.
                putExtra("drink_name", drink.name).
                putExtra("drink_imageurl", drink.imageUrl).
                putExtra("drink_id", drink.id).
                putExtra("top", screenLocation[1]).
                putExtra("height", view.getHeight()).
                putExtra("orientation", orientation);
        startActivity(intent);

        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mLiquor != null) {
            outState.putParcelable("liquor", mLiquor);
        }
    }
}
