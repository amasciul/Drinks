package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.ui.fragment.DrinksFragment;
import fr.masciulli.drinks.ui.fragment.LiquorsFragment;

public class MainActivity extends AppCompatActivity {
    private static final int POSITION_DRINKS = 0;
    private static final int POSITION_LIQUORS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        setSupportActionBar(toolbar);
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case POSITION_DRINKS:
                        return new DrinksFragment();
                    case POSITION_LIQUORS:
                        return new LiquorsFragment();
                }
                throw new IndexOutOfBoundsException("No fragment for position " + position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case POSITION_DRINKS:
                        return getString(R.string.title_drinks);
                    case POSITION_LIQUORS:
                        return getString(R.string.title_liquors);
                }
                throw new IndexOutOfBoundsException("No fragment for position " + position);
            }

            @Override
            public int getCount() {
                return 2;
            }
        });

        tabLayout.setupWithViewPager(pager);
    }
}
