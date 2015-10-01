package fr.masciulli.drinks.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import fr.masciulli.drinks.R;

public class MainActivity extends AppCompatActivity {
    private static final int POSITION_DRINKS = 0;
    private static final int POSITION_LIQUORS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
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
            public int getCount() {
                return 2;
            }
        });
    }
}
