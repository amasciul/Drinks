package fr.masciulli.drinks.activity;

import android.os.Bundle;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.LiquorDetailFragment;

public class LiquorDetailActivity extends ToolbarActivity {
    private LiquorDetailFragment mDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            mDetailFragment = new LiquorDetailFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.liquor_detail_container, mDetailFragment)
                    .commit();
        } else {
            mDetailFragment = (LiquorDetailFragment) getFragmentManager().findFragmentById(R.id.liquor_detail_container);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_liquor_detail;
    }
}
