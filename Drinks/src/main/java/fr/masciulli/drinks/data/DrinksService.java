package fr.masciulli.drinks.data;

import java.util.List;

import fr.masciulli.drinks.model.DrinksListItem;
import retrofit.Callback;
import retrofit.http.GET;

public interface DrinksService {
    @GET("/list.json")
    public void listDrinks(Callback<List<DrinksListItem>> callback);
}
