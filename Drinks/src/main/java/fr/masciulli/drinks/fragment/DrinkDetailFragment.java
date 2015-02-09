package fr.masciulli.drinks.fragment;

import android.animation.TimeInterpolator;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import fr.masciulli.android_quantizer.lib.ColorQuantizer;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.activity.ToolbarActivity;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.util.AnimUtils;
import fr.masciulli.drinks.util.HtmlUtils;
import fr.masciulli.drinks.view.BlurTransformation;
import fr.masciulli.drinks.view.ObservableScrollView;
import fr.masciulli.drinks.view.ScrollViewListener;

import java.util.ArrayList;

public class DrinkDetailFragment extends Fragment implements ScrollViewListener {

    private static final String ARG_DRINK = "drink";
    private static final String STATE_DRINK = "drink";

    private static final long ANIM_IMAGE_ENTER_DURATION = 500;
    private static final long ANIM_TEXT_ENTER_DURATION = 500;
    private static final long ANIM_IMAGE_ENTER_STARTDELAY = 300;
    private static final long ANIM_COLORBOX_ENTER_DURATION = 200;

    private static final TimeInterpolator decelerator = new DecelerateInterpolator();

    private Toolbar toolbar;
    private ImageView imageView;
    private ImageView blurredImageView;
    private TextView historyView;
    private ObservableScrollView scrollView;
    private TextView ingredientsView;
    private TextView instructionsView;
    private Button wikipediaButton;
    private View colorBox;
    private View colorView1;
    private View colorView2;
    private View colorView3;
    private View colorView4;

    private int imageViewHeight;

    private Transformation transformation;

    private Drink drink;

    private Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            imageView.setImageBitmap(bitmap);
            new QuantizeBitmapTask().execute(bitmap);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public static DrinkDetailFragment newInstance(Drink drink) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARG_DRINK, drink);
        DrinkDetailFragment fragment =  new DrinkDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        toolbar = ((ToolbarActivity) getActivity()).getToolbar();
        toolbar.getBackground().setAlpha(0);

        View root = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        imageView = (ImageView) root.findViewById(R.id.image);
        blurredImageView = (ImageView) root.findViewById(R.id.image_blurred);
        historyView = (TextView) root.findViewById(R.id.history);
        scrollView = (ObservableScrollView) root.findViewById(R.id.scroll);
        ingredientsView = (TextView) root.findViewById(R.id.ingredients);
        instructionsView = (TextView) root.findViewById(R.id.instructions);
        wikipediaButton = (Button) root.findViewById(R.id.wikipedia);
        colorBox = root.findViewById(R.id.colorbox);
        colorView1 = root.findViewById(R.id.color1);
        colorView2 = root.findViewById(R.id.color2);
        colorView3 = root.findViewById(R.id.color3);
        colorView4 = root.findViewById(R.id.color4);

        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToWikipedia();
            }
        });

        setHasOptionsMenu(true);

        drink = getArguments().getParcelable(ARG_DRINK);

        getActivity().setTitle(drink.name);
        Picasso.with(getActivity()).load(drink.imageUrl).into(target);

        transformation = new BlurTransformation(getActivity(), getResources().getInteger(R.integer.blur_radius));
        Picasso.with(getActivity()).load(drink.imageUrl).transform(transformation).into(blurredImageView);

        imageViewHeight = (int) getResources().getDimension(R.dimen.drink_detail_recipe_margin);
        scrollView.setScrollViewListener(this);

        if (savedInstanceState != null) {
            colorBox.setAlpha(1);
            Drink drink = savedInstanceState.getParcelable(STATE_DRINK);
            if (drink != null) {
                refreshUI(drink);
            }
        } else {
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
            }
        }

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drink_detail, menu);
    }

    private void goToWikipedia() {
        if (drink == null) {
            return;
        }
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(drink.wikipedia)));
    }

    private void runEnterAnimation() {

        Runnable refreshRunnable = new Runnable() {
            @Override
            public void run() {
                refreshUI(drink);
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
        AnimUtils.scheduleEndAction(animator, animateColorBoxRunnable, ANIM_IMAGE_ENTER_STARTDELAY, ANIM_IMAGE_ENTER_DURATION);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (drink != null) {
            outState.putParcelable(STATE_DRINK, drink);
        }
    }

    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
        float alpha = 2 * (float) y / (float) imageViewHeight;
        if (alpha > 1) {
            alpha = 1;
        } else if (alpha < 0) {
            alpha = 0;
        }
        blurredImageView.setAlpha(alpha);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.setMargins(params.leftMargin, -y / 2, params.rightMargin, params.bottomMargin);
        imageView.setLayoutParams(params);

        params = (RelativeLayout.LayoutParams) blurredImageView.getLayoutParams();
        params.setMargins(params.leftMargin, -y / 2, params.rightMargin, params.bottomMargin);
        blurredImageView.setLayoutParams(params);

        toolbar.getBackground().setAlpha((int) (alpha * 255));
    }

    public void refreshUI(Drink drink) {
        this.drink = drink;

        if (getActivity() == null) {
            return;
        }

        historyView.setText(drink.history);

        ingredientsView.setText(Html.fromHtml(HtmlUtils.getIngredientsHtml(this.drink)));

        instructionsView.setText(drink.instructions);
        wikipediaButton.setText(String.format(getString(R.string.drink_detail_wikipedia), drink.name));

        ViewTreeObserver observer = scrollView.getViewTreeObserver();
        if (observer != null) {
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    scrollView.getViewTreeObserver().removeOnPreDrawListener(this);
                    scrollView.setAlpha(0);
                    scrollView.animate().setDuration(ANIM_TEXT_ENTER_DURATION).
                            alpha(1).
                            setInterpolator(decelerator);
                    // Fake a onScrollChangedCall to apply changes to blurredImageView and imageView.
                    fakeOnScrollChanged();
                    return true;
                }
            });
        }
        scrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            case R.id.menu_item_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, drink.name);
                sendIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(HtmlUtils.getIngredientsHtml(drink)));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void fakeOnScrollChanged() {
        onScrollChanged(scrollView, scrollView.getScrollX(),
                scrollView.getScrollY(), scrollView.getScrollX(), scrollView.getScrollY());
    }

    private class QuantizeBitmapTask extends AsyncTask<Bitmap, Void, ArrayList<Integer>> {

        @Override
        protected ArrayList<Integer> doInBackground(Bitmap... bitmaps) {

            Bitmap originalBitmap = bitmaps[0];

            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();

            Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, originalWidth / 16, originalHeight / 16, true);

            ArrayList<Integer> quantizedColors = new ColorQuantizer().load(bitmap).quantize().getQuantizedColors();

            return quantizedColors;
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> colors) {
            View[] colorViews = new View[]{colorView1, colorView2, colorView3, colorView4};
            for (int i = 0; i < colors.size() && i < 4; i++) {
                colorViews[i].setBackgroundColor(colors.get(i));
            }
        }
    }
}
