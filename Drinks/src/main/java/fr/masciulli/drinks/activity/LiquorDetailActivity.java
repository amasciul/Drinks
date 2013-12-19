package fr.masciulli.drinks.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.LiquorDetailFragment;

public class LiquorDetailActivity extends FragmentActivity {
    private LiquorDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_detail);

        if (savedInstanceState == null) {
            mDetailFragment = new LiquorDetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.liquor_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (LiquorDetailFragment) getSupportFragmentManager().findFragmentById(R.id.drink_detail_container);
        }

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void finish() {
        super.finish();

        // override transitions to skip the standard window animations
        overridePendingTransition(0, 0);
    }

    @Override
    public void onBackPressed() {
        mDetailFragment.onBackPressed();
    }
}
