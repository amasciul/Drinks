package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DataProvider {
    private WebApi retrofit = new Retrofit.Builder()
            .baseUrl("http://drinks-api.appspot.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WebApi.class);

    public Call<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    public Call<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
