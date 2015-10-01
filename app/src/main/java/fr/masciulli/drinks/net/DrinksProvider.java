package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DrinksProvider {
    private DrinksService retrofit = new Retrofit.Builder()
            .baseUrl("http://drinks-api.appspot.com/api")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrinksService.class);

    public void getDrinks(Callback<List<Drink>> callback) {
        retrofit.getDrinks().enqueue(callback);
    }
}
