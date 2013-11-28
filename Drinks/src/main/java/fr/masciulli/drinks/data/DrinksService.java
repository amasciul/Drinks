package fr.masciulli.drinks.data;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface DrinksService {
    @GET("/Drinks")
    public void listDrinks(Callback<List<Drink>> callback);
    @GET("/Drinks?ingredient={ingredient}")
    public void listDrinksByIngredient(@Path("ingredient") String ingredient, Callback<List<Drink>> callback);
    @GET("/Drinks/{drinkid}")
    public void detailDrink(@Path("drinkid") int drinkId, Callback<Drink> callback);
    @GET("/Liquors")
    public void listLiquors(Callback<List<Liquor>> callback);
    @GET("/Liquors/{liquorid}")
    public void detailLiquor(@Path("liquorid") int liquorId, Callback<Liquor> callback);
}
