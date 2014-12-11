package fr.masciulli.drinks.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.R;

public class AboutDialogFragment extends DialogFragment {

    @InjectView(R.id.version_name)
    TextView mVersionNameView;

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

        return builder.create();
    }

    @OnClick(R.id.ok)
    void dismissDialog() {
        dismiss();
    }
}
