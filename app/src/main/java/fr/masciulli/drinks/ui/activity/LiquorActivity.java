package fr.masciulli.drinks.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;

public class LiquorActivity extends AppCompatActivity {
    public static final String EXTRA_LIQUOR = "extra_liquor";

    private ImageView imageView;
    private TextView historyView;
    private Button wikipediaButton;
    private Liquor liquor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        liquor = getIntent().getParcelableExtra(EXTRA_LIQUOR);

        setContentView(R.layout.activity_liquor);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(liquor.getName());

        imageView = (ImageView) findViewById(R.id.image);
        historyView = (TextView) findViewById(R.id.history);
        wikipediaButton = (Button) findViewById(R.id.wikipedia);

        setupViews();
    }

    private void setupViews() {
        historyView.setText(liquor.getHistory());
        wikipediaButton.setText(getString(R.string.wikipedia, liquor.getName()));
        wikipediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(liquor.getWikipedia()));
                startActivity(intent);
            }
        });
        Picasso.with(this).load(liquor.getImageUrl()).into(imageView);
    }
}
