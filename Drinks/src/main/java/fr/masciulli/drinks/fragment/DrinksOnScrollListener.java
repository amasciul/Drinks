package fr.masciulli.drinks.fragment;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;

import butterknife.ButterKnife;
import fr.masciulli.drinks.R;

public class DrinksOnScrollListener implements AbsListView.OnScrollListener {
    private static final long DRINKNAME_ANIM_DURATION = 600;
    private final AbsListView mListView;

    private int mPreviousFirstVisibleItem = -1;
    private int mPreviousLastVisibleItem = -1;

    public DrinksOnScrollListener(AbsListView listView) {
        mListView = listView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0) return;

        int lastVisibleItem = firstVisibleItem + visibleItemCount - 1;

        if (firstVisibleItem < mPreviousFirstVisibleItem) {
            View root = mListView.getChildAt(0);
            View nameView = ButterKnife.findById(root, R.id.name);
            nameView.setTranslationX(nameView.getWidth());
            nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(new DecelerateInterpolator());
        }

        if (lastVisibleItem > mPreviousLastVisibleItem) {
            View root = mListView.getChildAt(visibleItemCount - 1);
            View nameView = ButterKnife.findById(root, R.id.name);
            nameView.setTranslationX(nameView.getWidth());
            nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(new DecelerateInterpolator());
        }

        mPreviousFirstVisibleItem = firstVisibleItem;
        mPreviousLastVisibleItem = lastVisibleItem;
    }
}
