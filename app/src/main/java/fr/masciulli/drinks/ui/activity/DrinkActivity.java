package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Drink;

public class DrinkActivity extends AppCompatActivity {
    private static final String TAG = DrinkActivity.class.getSimpleName();

    public static final String EXTRA_DRINK = "extra_drink";

    private TextView historyView;
    private ImageView imageView;
    private TextView nameView;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Drink drink = getIntent().getParcelableExtra(EXTRA_DRINK);
        setContentView(R.layout.activity_drink);

        imageView = (ImageView) findViewById(R.id.image);
        historyView = (TextView) findViewById(R.id.history);
        nameView = (TextView) findViewById(R.id.name);
        scrollView = (ScrollView) findViewById(R.id.scroll);

        Picasso.with(this).load(drink.imageUrl).into(imageView);
        historyView.setText(drink.history);
        nameView.setText(drink.name);

        scrollView.getViewTreeObserver()
                .addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int y = scrollView.getScrollY();
                        imageView.setTop(-y / 2);
                    }
                });
    }
}
