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
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;
import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.R;

public class AboutDialogFragment extends DialogFragment {

    private static final long ANIM_REVEAL_DURATION = 300;
    private static final int FRANCE_STRING_START = 8;
    private static final int FRANCE_STRING_END = 14;

    private final Interpolator decelerateInterpolator = new DecelerateInterpolator();

    private TextView versionNameView;
    private Button okButton;
    private TextView madeInFranceView;

    public static AboutDialogFragment newInstance() {
        return new AboutDialogFragment();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.dialog_about, null);

        versionNameView = (TextView) root.findViewById(R.id.version_name);
        okButton = (Button) root.findViewById(R.id.ok);
        madeInFranceView = (TextView) root.findViewById(R.id.madeinfrance);

        ImageSpan span = new ImageSpan(getActivity(), R.drawable.france);
        SpannableString text = new SpannableString(getString(R.string.about_madeinfrance));
        text.setSpan(span, FRANCE_STRING_START, FRANCE_STRING_END, 0);

        madeInFranceView.setText(text);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String versionNameText = pInfo.versionName;
            if (BuildConfig.DEBUG) {
                versionNameText += ".dev";
            }
            versionNameView.setText(versionNameText);
        } catch (PackageManager.NameNotFoundException e) {
            versionNameView.setText(getString(R.string.unknown_version));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(root);

        okButton.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
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

        okButton.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 21) {
            int cx = okButton.getWidth() / 2;
            int cy = okButton.getHeight() / 2;

            int finalRadius = Math.max(okButton.getWidth()/2, okButton.getHeight()/2);

            Animator anim = ViewAnimationUtils.createCircularReveal(okButton, cx, cy, 0, finalRadius);
            anim.setDuration(ANIM_REVEAL_DURATION)
                    .setInterpolator(decelerateInterpolator);
            anim.start();
        }

    }

    private void runExitAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            int cx = okButton.getWidth() / 2;
            int cy = okButton.getHeight() / 2;

            int initialRadius = Math.max(okButton.getWidth() / 2, okButton.getHeight() / 2);
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(okButton, cx, cy, initialRadius, 0);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    okButton.setVisibility(View.INVISIBLE);
                    dismiss();
                }
            });
            anim.start();
        } else {
            dismiss();
        }
    }

    private void dismissDialog() {
        runExitAnimation();
    }
}
