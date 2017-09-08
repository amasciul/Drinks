package fr.masciulli.drinks.net;

import fr.masciulli.drinks.core.Drink;
import fr.masciulli.drinks.core.Liquor;
import retrofit2.http.GET;
import rx.Observable;

import java.util.List;

public interface WebApi {
    @GET("/api/Drinks")
    Observable<List<Drink>> getDrinks();

    @GET("/api/Liquors")
    Observable<List<Liquor>> getLiquors();
}
