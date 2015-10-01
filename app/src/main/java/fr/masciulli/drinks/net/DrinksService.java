package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import retrofit.Call;
import retrofit.http.GET;

public interface DrinksService {
    @GET("/api/Drinks")
    Call<List<Drink>> getDrinks();
}
