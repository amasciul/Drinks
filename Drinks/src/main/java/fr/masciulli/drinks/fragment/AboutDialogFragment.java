package fr.masciulli.drinks.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.R;

public class AboutDialogFragment extends DialogFragment {

    private static final long ANIM_REVEAL_DURATION = 300;
    private final Interpolator mDecelerateInterpolator = new DecelerateInterpolator();

    @InjectView(R.id.version_name)
    TextView mVersionNameView;
    @InjectView(R.id.ok)
    Button mOkButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.dialog_about, null);
        ButterKnife.inject(this, root);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionNameText = pInfo.versionName;
            if (BuildConfig.DEBUG) {
                versionNameText += ".dev";
            }
            mVersionNameView.setText(versionNameText);
        } catch (PackageManager.NameNotFoundException e) {
            mVersionNameView.setText(getString(R.string.unknown_version));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(root);

        mOkButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                runEnterAnimation();
            }
        });

        return builder.create();
    }

    private void runEnterAnimation() {

        mOkButton.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 21) {
            int cx = mOkButton.getWidth() / 2;
            int cy = mOkButton.getHeight() / 2;

            int finalRadius = Math.max(mOkButton.getWidth()/2, mOkButton.getHeight()/2);

            Animator anim = ViewAnimationUtils.createCircularReveal(mOkButton, cx, cy, 0, finalRadius);
            anim.setDuration(ANIM_REVEAL_DURATION)
                    .setInterpolator(mDecelerateInterpolator);
            anim.start();
        }

    }

    private void runExitAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            int cx = mOkButton.getWidth() / 2;
            int cy = mOkButton.getHeight() / 2;

            int initialRadius = Math.max(mOkButton.getWidth() / 2, mOkButton.getHeight() / 2);
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(mOkButton, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mOkButton.setVisibility(View.INVISIBLE);
                    dismiss();
                }
            });
            anim.start();
        } else {
            dismiss();
        }
    }

    @OnClick(R.id.ok)
    void dismissDialog() {
        runExitAnimation();
    }
}
