package fr.masciulli.drinks.view;

import android.animation.TimeInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ListView;
import fr.masciulli.drinks.R;

public class DrinksOnScrollListener extends RecyclerView.OnScrollListener {
    private static final long DRINKNAME_ANIM_DURATION = 600;

    public static final int NAMEVIEW_POSITION_TOP = 0;
    public static final int NAMEVIEW_POSITION_BOTTOM = 1;

    private int previousFirstVisibleItem = -1;
    private int previousLastVisibleItem = -1;
    private TimeInterpolator decelerateInterpolator = new DecelerateInterpolator();

    private int nameViewPosition = NAMEVIEW_POSITION_BOTTOM;

    public DrinksOnScrollListener() {
    }

    public DrinksOnScrollListener(int nameViewPosition) {
        if (nameViewPosition == NAMEVIEW_POSITION_TOP) {
            this.nameViewPosition = nameViewPosition;
        }
    }

    @Override
    public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {

        int totalItemCount = recyclerView.getAdapter().getItemCount();

        if (totalItemCount == 0) {
            return;
        }

        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int firstVisibleItem = manager.findFirstVisibleItemPosition();
        int lastVisibleItem = manager.findLastVisibleItemPosition();
        int visibleItemCount = lastVisibleItem - firstVisibleItem + 1;

        if (firstVisibleItem < previousFirstVisibleItem) {
            // first visible item index has decreased : we are scrolling up
            View root = recyclerView.getChildAt(0);
            View nameView = root.findViewById(R.id.name);
            //if (nameView == null) return;
            if (nameView != null) {
                if (nameViewPosition == NAMEVIEW_POSITION_BOTTOM) {
                    nameView.setTranslationX(nameView.getWidth());
                } else {
                    nameView.setTranslationX(0 - nameView.getWidth());
                }
                nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(decelerateInterpolator);
            }
        }

        if (lastVisibleItem > previousLastVisibleItem) {
            // first visible item index has decreased : we are scrolling down
            View root = recyclerView.getChildAt(visibleItemCount - 1);
            View nameView = root.findViewById(R.id.name);
            if (nameView != null) {
                if (nameViewPosition == NAMEVIEW_POSITION_BOTTOM) {
                    nameView.setTranslationX(nameView.getWidth());
                } else {
                    nameView.setTranslationX(0 - nameView.getWidth());
                }
                nameView.animate().translationX(0).setDuration(DRINKNAME_ANIM_DURATION).setInterpolator(decelerateInterpolator);
            }
        }

        previousFirstVisibleItem = firstVisibleItem;
        previousLastVisibleItem = lastVisibleItem;
    }
}
