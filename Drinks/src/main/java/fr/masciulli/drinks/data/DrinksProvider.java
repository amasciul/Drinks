package fr.masciulli.drinks.data;

import java.util.List;


import fr.masciulli.drinks.model.DrinksListItem;
import retrofit.Callback;
import retrofit.RestAdapter;
public class DrinksProvider {

    private static RestAdapter mRestAdapter = new RestAdapter.Builder()
            .setServer("http://masciulli.fr/drinks")
            .build();
    private static DrinksService mService = mRestAdapter.create(DrinksService.class);

    public static void getDrinks(Callback<List<DrinksListItem>> callback) {
        mService.listDrinks(callback);
    }

}
