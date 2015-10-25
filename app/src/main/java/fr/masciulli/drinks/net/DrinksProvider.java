package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DrinksProvider {
    private DrinksService retrofit = new Retrofit.Builder()
            .baseUrl("http://drinks-api.appspot.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DrinksService.class);

    public Call<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }
}
