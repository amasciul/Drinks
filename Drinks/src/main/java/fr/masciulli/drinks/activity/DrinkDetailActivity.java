package fr.masciulli.drinks.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.DrinkDetailFragment;

public class DrinkDetailActivity extends FragmentActivity {

    private DrinkDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_detail);

        if (savedInstanceState == null) {
            mDetailFragment = new DrinkDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.drink_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (DrinkDetailFragment) getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        mDetailFragment.onBackPressed();
    }
}
