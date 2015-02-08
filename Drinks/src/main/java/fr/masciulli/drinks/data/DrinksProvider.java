package fr.masciulli.drinks.data;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Callback;
import retrofit.RestAdapter;

import java.util.List;

public class DrinksProvider {

    private static RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("http://drinks-api.appspot.com/api")
            .build();
    private static DrinksService service = restAdapter.create(DrinksService.class);

    public static void getAllDrinks(Callback<List<Drink>> callback) {
        service.getAllDrinks(callback);
    }

    public static void getAllLiquors(Callback<List<Liquor>> callback) {
        service.getAllLiquors(callback);
    }

    public static void updateServer(String server) {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(server)
                .build();
        service = restAdapter.create(DrinksService.class);
    }

}
