package fr.masciulli.drinks.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import fr.masciulli.drinks.R;

public class AboutDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder//.setTitle(getString(R.string.about_title))
               .setPositiveButton(getString(R.string.about_ok), null)
               .setView(inflater.inflate(R.layout.dialog_about, null));

        return builder.create();
    }
}
