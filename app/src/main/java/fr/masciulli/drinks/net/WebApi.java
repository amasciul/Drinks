package fr.masciulli.drinks.net;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit2.Call;
import retrofit2.http.GET;

public interface WebApi {
    @GET("/api/Drinks")
    Call<List<Drink>> getDrinks();

    @GET("/api/Liquors")
    Call<List<Liquor>> getLiquors();
}
