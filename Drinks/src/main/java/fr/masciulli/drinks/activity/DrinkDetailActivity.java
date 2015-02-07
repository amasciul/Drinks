package fr.masciulli.drinks.activity;

import android.os.Bundle;
import fr.masciulli.drinks.R;
import fr.masciulli.drinks.fragment.DrinkDetailFragment;
import fr.masciulli.drinks.model.Drink;

public class DrinkDetailActivity extends ToolbarActivity {

    public static final String ARG_DRINK = "drink";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            DrinkDetailFragment fragment = DrinkDetailFragment.newInstance((Drink) getIntent().getParcelableExtra(ARG_DRINK));
            getFragmentManager().beginTransaction()
                    .add(R.id.drink_detail_container, fragment)
                    .commit();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_drink_detail;
    }
}
