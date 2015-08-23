package fr.masciulli.drinks.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.AboutDialogFragment;
import fr.masciulli.drinks.fragment.DrinksListFragment;
import fr.masciulli.drinks.fragment.LiquorsListFragment;

import java.util.Locale;

public class MainActivity extends ToolbarActivity {
    private ViewPager viewPager;

    private DrinksListFragment drinksListFragment;
    private LiquorsListFragment liquorsListFragment;

    SectionsPagerAdapter sectionsPagerAdapter;


    private MenuItem retryAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = (ViewPager) findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        drinksListFragment = DrinksListFragment.newInstance();
        liquorsListFragment = LiquorsListFragment.newInstance();

        sectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(sectionsPagerAdapter);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        retryAction = menu.findItem(R.id.retry);
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
                return drinksListFragment;
            } else if (position == mLiquorsPosition) {
                return liquorsListFragment;
            }

            return null;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object fragment = super.instantiateItem(container, position);
            if (position == mDrinksPosition) {
                drinksListFragment = (DrinksListFragment) fragment;
            } else if (position == mLiquorsPosition) {
                liquorsListFragment = (LiquorsListFragment) fragment;
            }
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position == mDrinksPosition) {
                drinksListFragment = null;
            } else if (position == mLiquorsPosition) {
                liquorsListFragment = null;
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
            if (drinksListFragment != null) {
                drinksListFragment.onScroll(position, positionOffset, positionOffsetPixels);
            }
            if (liquorsListFragment != null) {
                liquorsListFragment.onScroll(position, positionOffset, positionOffsetPixels);
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
    public boolean onOptionsItemSelected(MenuItem item) {
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
                AboutDialogFragment aboutDialogFragment = AboutDialogFragment.newInstance();
                aboutDialogFragment.show(getFragmentManager(), "dialog_about");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setRefreshActionVisible(boolean visibility) {
        if (retryAction != null) {
            retryAction.setVisible(visibility);
        }
    }

}
