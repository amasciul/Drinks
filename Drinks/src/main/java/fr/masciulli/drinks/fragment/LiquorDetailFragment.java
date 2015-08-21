package fr.masciulli.drinks.fragment;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
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

    private final TimeInterpolator decelerator = new DecelerateInterpolator();
    private static final String ARG_LIQUOR = "liquor";
    private static final String STATE_LIQUOR = "liquor";
    private static final String STATE_DRINKS = "drinks";
    //TODO move to specific class
    private static final String PREF_DRINKS_JSON = "drinks_json";

    private ImageView imageView;
    private ImageView blurredImageView;
    private TextView historyView;
    private ProgressBar progressBar;
    private Button wikipediaButton;
    private TextView drinksTitleView;
    private View colorBox;
    private View colorView1;
    private View colorView2;
    private View colorView3;
    private View colorView4;

    private ListView listView;
    private View headerView;
    private Toolbar toolbar;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            imageView.setImageBitmap(bitmap);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    colorView1.setBackgroundColor(palette.getVibrantColor(0));
                    colorView2.setBackgroundColor(palette.getLightVibrantColor(0));
                    colorView3.setBackgroundColor(palette.getDarkVibrantColor(0));
                    colorView4.setBackgroundColor(palette.getMutedColor(0));
                }
            });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    private LiquorDetailAdapter drinkAdapter;

    private DrinksOnScrollListener animationOnScrollListener;

    private Liquor liquor;

    private int imageViewHeight;

    private Callback<List<Drink>> drinksCallback = new Callback<List<Drink>>() {

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
                    if (ingredient.toLowerCase().contains(liquor.name.toLowerCase())) {
                        filteredDrinks.add(drink);
                        break;
                    }

                    //At this point, we know main name does not match
                    for (String otherName : liquor.otherNames) {
                        if (ingredient.toLowerCase().contains(otherName.toLowerCase())) {
                            filteredDrinks.add(drink);
                            break;
                        }
                    }

                }
            }

            if (filteredDrinks.size() > 0) {
                drinksTitleView.setVisibility(View.VISIBLE);
            } else {
                drinksTitleView.setVisibility(View.GONE);
            }

            drinkAdapter.update(filteredDrinks);
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

            View view = getView();
            if (view != null) {
                if (error.getKind() == RetrofitError.Kind.NETWORK) {
                    Snackbar.make(view, R.string.network_error, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, R.string.list_loading_failed, Snackbar.LENGTH_SHORT).show();
                }
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
        toolbar = ((ToolbarActivity) getActivity()).getToolbar();
        toolbar.getBackground().setAlpha(0);

        View root = inflater.inflate(R.layout.fragment_liquor_detail, container, false);

        setHasOptionsMenu(true);

        headerView = inflater.inflate(R.layout.header_liquor_detail, listView, false);

        listView = (ListView) root.findViewById(R.id.list);
        imageView = (ImageView) root.findViewById(R.id.image);
        blurredImageView = (ImageView) root.findViewById(R.id.image_blurred);
        progressBar = (ProgressBar) root.findViewById(R.id.progressbar);

        historyView = (TextView) headerView.findViewById(R.id.history);
        wikipediaButton = (Button) headerView.findViewById(R.id.wikipedia);
        drinksTitleView = (TextView) headerView.findViewById(R.id.drinks_title);
        colorBox = headerView.findViewById(R.id.colorbox);
        colorView1 = headerView.findViewById(R.id.color1);
        colorView2 = headerView.findViewById(R.id.color2);
        colorView3 = headerView.findViewById(R.id.color3);
        colorView4 = headerView.findViewById(R.id.color4);

        listView.addHeaderView(headerView);

        drinkAdapter = new LiquorDetailAdapter(getActivity());
        listView.setAdapter(drinkAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openRelatedDrinkDetail(position);
            }
        });

        liquor = getArguments().getParcelable(ARG_LIQUOR);

        getActivity().setTitle(liquor.name);
        Picasso.with(getActivity()).load(liquor.imageUrl).into(target);

        Transformation transformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(liquor.imageUrl).transform(transformation).into(blurredImageView);

        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWikipedia();
            }
        });

        imageViewHeight = (int) getResources().getDimension(R.dimen.liquor_detail_recipe_margin);
        listView.setOnScrollListener(this);
        //animationOnScrollListener = new DrinksOnScrollListener(listView);

        if (savedInstanceState != null) {
            colorBox.setAlpha(1);
            Liquor liquor = savedInstanceState.getParcelable(STATE_LIQUOR);
            List<Drink> drinks = savedInstanceState.getParcelableArrayList(STATE_DRINKS);
            if (liquor != null && drinks != null) {
                refreshUI(this.liquor);
                drinksCallback.success(drinks, null);
            } else {
                refresh(this.liquor);
            }
        } else {
            imageView.setVisibility(View.INVISIBLE);
            ViewTreeObserver observer = imageView.getViewTreeObserver();
            if (observer != null) {
                observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        runEnterAnimation();
                        return true;
                    }
                });
            } else {
                refresh(liquor);
            }
        }

        return root;
    }

    private void goToWikipedia() {
        if (liquor == null) {
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(liquor.wikipedia)));
    }

    private void openRelatedDrinkDetail(int position) {
        Drink drink = drinkAdapter.getItem(position - HEADERVIEWS_COUNT);

        Intent intent = new Intent(getActivity(), DrinkDetailActivity.class);
        intent.putExtra(DrinkDetailActivity.ARG_DRINK, drink);
        startActivity(intent);
    }

    @TargetApi(21)
    private void runEnterAnimation() {

        if (Build.VERSION.SDK_INT >= 21) {
            int cx = imageView.getWidth() / 2;
            int cy = imageView.getHeight() / 2;

            // OMG some Pythagorean theorem
            int finalRadius =
                    (int) Math.sqrt(Math.pow(imageView.getWidth(), 2) + Math.pow(imageView.getHeight(), 2)) / 2;

            Animator animator = ViewAnimationUtils.createCircularReveal(imageView, cx, cy, 0, finalRadius);
            animator.setDuration(ANIM_IMAGE_ENTER_DURATION);
            animator.setStartDelay(ANIM_IMAGE_ENTER_STARTDELAY);
            animator.setInterpolator(decelerator);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    imageView.setVisibility(View.VISIBLE);
                    refresh(liquor);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    colorBox.animate()
                            .alpha(1)
                            .setDuration(ANIM_COLORBOX_ENTER_DURATION)
                            .setInterpolator(decelerator);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animator.start();
        } else {
            imageView.setVisibility(View.VISIBLE);
            Runnable refreshRunnable = new Runnable() {
                @Override
                public void run() {
                    refresh(liquor);
                }
            };

            imageView.setTranslationY(-imageView.getHeight());

            ViewPropertyAnimator animator = imageView.animate().setDuration(ANIM_IMAGE_ENTER_DURATION).
                    setStartDelay(ANIM_IMAGE_ENTER_STARTDELAY).
                    translationY(0).
                    setInterpolator(decelerator);

            Runnable animateColorBoxRunnable = new Runnable() {
                @Override
                public void run() {
                    colorBox.animate()
                            .alpha(1)
                            .setDuration(ANIM_COLORBOX_ENTER_DURATION)
                            .setInterpolator(decelerator);
                }
            };

            AnimUtils.scheduleStartAction(animator, refreshRunnable, ANIM_IMAGE_ENTER_STARTDELAY);
            AnimUtils.scheduleEndAction(animator, animateColorBoxRunnable, ANIM_IMAGE_ENTER_DURATION, ANIM_IMAGE_ENTER_STARTDELAY);
        }
    }

    public void refreshUI(Liquor liquor) {
        this.liquor = liquor;
        if (getActivity() == null) {
            return;
        }

        progressBar.setVisibility(View.GONE);

        historyView.setText(liquor.history);
        wikipediaButton.setText(String.format(getString(R.string.liquor_detail_wikipedia), liquor.name));
        drinksTitleView.setText(String.format(getString(R.string.liquor_detail_drinks), liquor.name));

        ViewTreeObserver observer = listView.getViewTreeObserver();
        if (observer != null) {
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    listView.getViewTreeObserver().removeOnPreDrawListener(this);
                    listView.setAlpha(0);
                    listView.animate().setDuration(ANIM_TEXT_ENTER_DURATION).
                            alpha(1).
                            setInterpolator(decelerator);

                    return true;
                }
            });
        }
        listView.setVisibility(View.VISIBLE);
    }

    public void refresh(Liquor liquor) {
        if (getActivity() == null) {
            return;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (preferences.contains(PREF_DRINKS_JSON)) {
            Gson gson = new Gson();
            //TODO async
            List<Drink> drinks = gson.fromJson(preferences.getString(PREF_DRINKS_JSON, "null"), new TypeToken<List<Drink>>() {
            }.getType());

            //TODO do not use retrofit callback
            drinksCallback.success(drinks, null);
        } else {
            DrinksProvider.getAllDrinks(drinksCallback);
        }
        refreshUI(liquor);
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
                refresh(liquor);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView listView, int state) {
        if (animationOnScrollListener != null) {
            //TODO once recycler, call scroll
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (animationOnScrollListener != null) {
            //animationOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            //TODO once recycler, call scroll
        }
        float alpha = 2 * (float) -headerView.getTop() / (float) imageViewHeight;
        if (alpha > 1) {
            alpha = 1;
        } else if (alpha < 0) {
            alpha = 0;
        }
        blurredImageView.setAlpha(alpha);

        imageView.setTop(headerView.getTop() / 2);
        imageView.setBottom(imageViewHeight + headerView.getTop());
        blurredImageView.setTop(headerView.getTop() / 2);
        blurredImageView.setBottom(imageViewHeight + headerView.getTop());

        toolbar.getBackground().setAlpha((int) (alpha * 255));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO do not use getCount
        if (liquor != null && drinkAdapter.getCount() > 0) {
            outState.putParcelable(STATE_LIQUOR, liquor);
            outState.putParcelableArrayList(STATE_DRINKS, drinkAdapter.getDrinks());
        }
    }
}
