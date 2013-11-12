package fr.masciulli.drinks.data;

import java.util.List;


import fr.masciulli.drinks.model.DrinkDetailItem;
import fr.masciulli.drinks.model.DrinksListItem;
import retrofit.Callback;
import retrofit.RestAdapter;
public class DrinksProvider {

    private static RestAdapter mRestAdapter = new RestAdapter.Builder()
            .setServer("http://192.168.1.54/api")
            .build();
    private static DrinksService mService = mRestAdapter.create(DrinksService.class);

    public static void getDrinksList(Callback<List<DrinksListItem>> callback) {
        mService.listDrinks(callback);
    }

    public static void getDrink(int drinkId, Callback<DrinkDetailItem> callback) {
        mService.detailDrink(drinkId, callback);
    }

}
