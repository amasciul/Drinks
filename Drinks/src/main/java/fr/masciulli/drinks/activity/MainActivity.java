package fr.masciulli.drinks.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.AboutDialogFragment;
import fr.masciulli.drinks.fragment.DrinksListFragment;
import fr.masciulli.drinks.fragment.LiquorsListFragment;

public class MainActivity extends FragmentActivity {

    private DrinksListFragment mDrinksFragment;
    private LiquorsListFragment mLiquorsFragment;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    private MenuItem mRetryAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(mSectionsPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mRetryAction = menu.findItem(R.id.retry);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        // TODO uncast to object once Android Studio bug fixed
        if (((Object) fragment).getClass() == DrinksListFragment.class) {
            mDrinksFragment = (DrinksListFragment) fragment;
        } else if (((Object) fragment).getClass() == LiquorsListFragment.class) {
            mLiquorsFragment = (LiquorsListFragment) fragment;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            final int drinks = getResources().getInteger(R.integer.position_fragment_drinks);
            final int ingredients = getResources().getInteger(R.integer.position_fragment_ingredients);

            if (position == drinks) {
                return new DrinksListFragment();
            } else if (position == ingredients) {
                return new LiquorsListFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            //TODO repace 0 and 1 by values in resources
            switch (position) {
                case 0:
                    return getString(R.string.title_drinks).toUpperCase(l);
                case 1:
                    return getString(R.string.title_liquors).toUpperCase(l);
            }
            return null;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mDrinksFragment != null) {
                mDrinksFragment.onScroll(position, positionOffset, positionOffsetPixels);
            }
            if (mLiquorsFragment != null) {
                mLiquorsFragment.onScroll(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int i) {

        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_report:
                final Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:alexandre.masciulli@gmail.com"));
                sendIntent.setType("message/rfc822");
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.report_default_subject);
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.report)));
            case R.id.licenses:
                startActivity(new Intent(this, LicensesActivity.class));
                return true;
            case R.id.about:
                (new AboutDialogFragment()).show(getSupportFragmentManager(), "dialog_about");
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    public void setRefreshActionVisible(boolean visibility) {
        if (mRetryAction != null) {
            mRetryAction.setVisible(visibility);
        }
    }

}
