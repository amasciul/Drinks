package fr.masciulli.drinks.data;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Callback;
import retrofit.http.GET;

import java.util.List;

public interface DrinksService {
    @GET("/Drinks")
    public void getAllDrinks(Callback<List<Drink>> callback);

    @GET("/Liquors")
    public void getAllLiquors(Callback<List<Liquor>> callback);
}
