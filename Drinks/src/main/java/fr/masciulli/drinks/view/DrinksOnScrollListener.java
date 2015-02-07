package fr.masciulli.drinks.view;

import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import butterknife.ButterKnife;
import fr.masciulli.drinks.R;

public class DrinksOnScrollListener implements AbsListView.OnScrollListener {
    private static final long DRINKNAME_ANIM_DURATION = 600;

    public static final int NAMEVIEW_POSITION_TOP = 0;
    public static final int NAMEVIEW_POSITION_BOTTOM = 1;

    private final ListView mListView;

    private int mPreviousFirstVisibleItem = -1;
    private int mPreviousLastVisibleItem = -1;
    private TimeInterpolator mDecelerateInterpolator = new DecelerateInterpolator();

    private int mNameViewPosition = NAMEVIEW_POSITION_BOTTOM;

    public DrinksOnScrollListener(ListView listView) {
        mListView = listView;
    }

    public DrinksOnScrollListener(ListView listView, int nameViewPosition) {
        mListView = listView;
        if (nameViewPosition == NAMEVIEW_POSITION_TOP) {
            mNameViewPosition = nameViewPosition;
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0) {
            return;
        }

        int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

        if (firstVisibleItem < mPreviousFirstVisibleItem) {
            // first visible item index has decreased : we are scrolling up
            View root = mListView.getChildAt(0);
            View nameView = ButterKnife.findById(root, R.id.name);
            //if (nameView == null) return;
            if (nameView != null) {
                if (mNameViewPosition == NAMEVIEW_POSITION_BOTTOM) {
                    nameView.setTranslationX(nameView.getWidth());
                } else {
                    nameView.setTranslationX(0 - nameView.getWidth());
                }
                nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(mDecelerateInterpolator);
            }
        }

        if (lastVisibleItem > mPreviousLastVisibleItem) {
            // first visible item index has decreased : we are scrolling down
            View root = mListView.getChildAt(visibleItemCount - 1);
            View nameView = ButterKnife.findById(root, R.id.name);
            if (nameView != null) {
                if (mNameViewPosition == NAMEVIEW_POSITION_BOTTOM) {
                    nameView.setTranslationX(nameView.getWidth());
                } else {
                    nameView.setTranslationX(0 - nameView.getWidth());
                }
                nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(mDecelerateInterpolator);
            }
        }

        mPreviousFirstVisibleItem = firstVisibleItem;
        mPreviousLastVisibleItem = lastVisibleItem;
    }
}
