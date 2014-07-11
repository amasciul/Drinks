package fr.masciulli.drinks.activity;

import android.app.Activity;
import android.os.Bundle;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.DrinkDetailFragment;

public class DrinkDetailActivity extends Activity {

    private DrinkDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        if (savedInstanceState == null) {
            mDetailFragment = new DrinkDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.drink_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (DrinkDetailFragment) getFragmentManager().findFragmentById(R.id.drink_detail_container);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
