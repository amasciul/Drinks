package fr.masciulli.drinks.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import fr.masciulli.drinks.R;

public class LiquorDetailActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liquor_detail);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
