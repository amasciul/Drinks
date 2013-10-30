package fr.masciulli.drinks.data;


import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.model.Drink;

public class DrinksListProvider {
    public static List<Drink> getDrinks() {
        ArrayList<Drink> drinks = new ArrayList<Drink>();
        drinks.add(new Drink("Amaretto Frost", "http://www.smallscreennetwork.com/videos/cocktail_spirit/morgenthaler-method-amaretto-sour.jpg"));
        drinks.add(new Drink("Americano", "http://www.ganzomag.com/wp-content/uploads/2012/05/americano-cocktail1.jpg"));
        drinks.add(new Drink("Tom Collins", "http://www.vinumimporting.com/wp-content/uploads/2012/06/tom-collins.jpg"));
        drinks.add(new Drink("Mojito", "http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg"));
        drinks.add(new Drink("Dry Martini", "http://www.cocktailrendezvous.com/images.php?f=files/recipes/images/martini.jpg&w=616&h=347&c=1"));
        return drinks;
    }
}
