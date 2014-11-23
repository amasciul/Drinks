package fr.masciulli.drinks.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.DrinkDetailFragment;

public class DrinkDetailActivity extends ToolbarActivity {

    private DrinkDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mDetailFragment = new DrinkDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.drink_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (DrinkDetailFragment) getFragmentManager().findFragmentById(R.id.drink_detail_container);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drink_detail;
    }
}
