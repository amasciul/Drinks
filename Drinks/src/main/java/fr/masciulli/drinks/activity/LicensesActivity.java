package fr.masciulli.drinks.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.masciulli.drinks.R;

public class LicensesActivity extends ToolbarActivity {

    @InjectView(R.id.webview)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mWebView.loadUrl("file:///android_asset/licenses.html");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_licenses;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
