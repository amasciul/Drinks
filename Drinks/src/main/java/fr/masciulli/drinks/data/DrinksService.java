package fr.masciulli.drinks.data;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface DrinksService {
    @GET("/Drinks")
    public void listDrinks(Callback<List<Drink>> callback);
    @GET("/Drinks/{drinkid}")
    public void detailDrink(@Path("drinkid") int drinkId, Callback<Drink> callback);
}
