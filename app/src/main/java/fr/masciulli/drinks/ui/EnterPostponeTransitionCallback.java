package fr.masciulli.drinks.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;

import com.squareup.picasso.Callback;

public class EnterPostponeTransitionCallback implements Callback {
    private static final boolean TRANSITIONS_AVAILABLE = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    private final Activity activity;

    public EnterPostponeTransitionCallback(Activity activity) {
        this.activity = activity;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSuccess() {
        if (TRANSITIONS_AVAILABLE) {
            activity.startPostponedEnterTransition();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onError() {
        if (TRANSITIONS_AVAILABLE) {
            activity.startPostponedEnterTransition();
        }
    }
}
