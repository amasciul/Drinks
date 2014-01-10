package fr.masciulli.drinks.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.masciulli.drinks.R;

public class AboutDialogFragment extends DialogFragment {

    @InjectView(R.id.version_name) TextView versionNameView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View root = inflater.inflate(R.layout.dialog_about, null);
        ButterKnife.inject(this, root);

        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionNameView.setText(pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            versionNameView.setText(getString(R.string.unknown_version));
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder
                .setPositiveButton(getString(R.string.about_ok), null)
                .setView(root);

        return builder.create();
    }
}
