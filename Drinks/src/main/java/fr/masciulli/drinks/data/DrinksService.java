package fr.masciulli.drinks.data;

import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface DrinksService {
    @GET("/Drinks")
    public void getAllDrinks(Callback<List<Drink>> callback);

    @GET("/Drinks/{drinkid}")
    public void getDrink(@Path("drinkid") int drinkId, Callback<Drink> callback);

    @GET("/Liquors")
    public void getAllLiquors(Callback<List<Liquor>> callback);

    @GET("/Liquors/{liquorid}")
    public void getLiquor(@Path("liquorid") int liquorId, Callback<Liquor> callback);
}
