package fr.masciulli.drinks.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import fr.masciulli.drinks.R;
import fr.masciulli.drinks.model.Liquor;
import fr.masciulli.drinks.ui.adapter.LiquorRelatedAdapter;

public class LiquorActivity extends AppCompatActivity {
    public static final String EXTRA_LIQUOR = "extra_liquor";

    private RecyclerView recyclerView;
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

        ImageView imageView = (ImageView) findViewById(R.id.image);
        Picasso.with(this).load(liquor.getImageUrl()).into(imageView);

        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        initRecyclerView();
    }

    private void initRecyclerView() {
        final LiquorRelatedAdapter adapter = new LiquorRelatedAdapter(liquor);
        final int columnCount = getResources().getInteger(R.integer.column_count);
        GridLayoutManager layoutManager = new GridLayoutManager(this, columnCount);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case LiquorRelatedAdapter.TYPE_HEADER:
                        return columnCount;
                    case LiquorRelatedAdapter.TYPE_DRINK:
                        return 1;
                    default:
                        throw new IllegalArgumentException("Unknown view type");
                }
            }
        });

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
