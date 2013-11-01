package fr.masciulli.drinks.data;

import java.util.List;

import fr.masciulli.drinks.model.DrinkDetailItem;
import fr.masciulli.drinks.model.DrinksListItem;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface DrinksService {
    @GET("/list.json")
    public void listDrinks(Callback<List<DrinksListItem>> callback);
    @GET("/detail/{drinkid}.json")
    public void detailDrink(@Path("drinkid") String drinkId, Callback<DrinkDetailItem> callback);
}
