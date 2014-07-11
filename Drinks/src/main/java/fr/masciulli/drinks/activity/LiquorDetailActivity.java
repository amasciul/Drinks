package fr.masciulli.drinks.activity;

import android.app.Activity;
import android.os.Bundle;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.LiquorDetailFragment;

public class LiquorDetailActivity extends Activity {
    private LiquorDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_detail);

        if (savedInstanceState == null) {
            mDetailFragment = new LiquorDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.liquor_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (LiquorDetailFragment) getFragmentManager().findFragmentById(R.id.liquor_detail_container);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
