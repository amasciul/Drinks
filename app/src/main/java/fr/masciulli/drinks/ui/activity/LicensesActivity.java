package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import fr.masciulli.drinks.R;

public class LicensesActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_licenses);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/licenses.html");
    }
}
