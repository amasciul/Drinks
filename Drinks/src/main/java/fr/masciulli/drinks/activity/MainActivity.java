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
import android.view.ViewGroup;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
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
    @Optional @InjectView(R.id.pager) ViewPager mViewPager;

    private MenuItem mRetryAction;
    private boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mDualPane = getResources().getBoolean(R.bool.dualpane);

        if (mDualPane && savedInstanceState != null) {
            mDrinksFragment = (DrinksListFragment) getSupportFragmentManager().findFragmentById(R.id.drinks_list_container);
            mLiquorsFragment = (LiquorsListFragment) getSupportFragmentManager().findFragmentById(R.id.liquors_list_container);
        } else {
            mDrinksFragment = new DrinksListFragment();
            mLiquorsFragment = new LiquorsListFragment();
        }

        if (mDualPane) {
            if (savedInstanceState == null) {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.drinks_list_container, mDrinksFragment).
                            add(R.id.liquors_list_container, mLiquorsFragment).
                            commit();
            }
        } else {
            // Create the adapter that will return a fragment for each of the three
            // primary sections of the app.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

            // Set up the ViewPager with the sections adapter.
            mViewPager.setAdapter(mSectionsPagerAdapter);
            mViewPager.setOnPageChangeListener(mSectionsPagerAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mRetryAction = menu.findItem(R.id.retry);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        
        final int mDrinksPosition = getResources().getInteger(R.integer.position_fragment_drinks);
        final int mLiquorsPosition = getResources().getInteger(R.integer.position_fragment_ingredients);

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.

            if (position == mDrinksPosition) {
                return mDrinksFragment;
            } else if (position == mLiquorsPosition) {
                return mLiquorsFragment;
            }

            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object fragment = super.instantiateItem(container, position);
            if (position == mDrinksPosition) {
                mDrinksFragment = (DrinksListFragment) fragment;
            } else if (position == mLiquorsPosition) {
                mLiquorsFragment = (LiquorsListFragment) fragment;
            }
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == mDrinksPosition) {
                mDrinksFragment = null;
            } else if (position == mLiquorsPosition) {
                mLiquorsFragment = null;
            }
            super.destroyItem(container, position, object);
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
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(getString(R.string.report_mail)) +
                        "?subject=" + Uri.encode(getString(R.string.report_default_subject));
                Uri uri = Uri.parse(uriText);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.report)));
                return true;
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
