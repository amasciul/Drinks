package fr.masciulli.drinks.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import fr.masciulli.drinks.R;

public class LicensesActivity extends ToolbarActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = (WebView) findViewById(R.id.webview);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView.loadUrl("file:///android_asset/licenses.html");
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
