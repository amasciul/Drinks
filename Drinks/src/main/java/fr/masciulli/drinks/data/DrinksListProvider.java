package fr.masciulli.drinks.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.masciulli.drinks.model.DrinksListItem;

public class DrinksListProvider {
    public static List<DrinksListItem> getDrinks() throws IOException {
        List<DrinksListItem> drinks;

        String json = "[{\"name\":\"Amaretto Frost\",\"id\":\"amarettofrost\",\"imageURL\":\"http://www.smallscreennetwork.com/videos/cocktail_spirit/morgenthaler-method-amaretto-sour.jpg\"}," +
                "{\"name\":\"Americano\",\"id\":\"americano\",\"imageURL\":\"http://www.ganzomag.com/wp-content/uploads/2012/05/americano-cocktail1.jpg\"}," +
                "{\"name\":\"Tom Collins\",\"id\":\"tomcollins\",\"imageURL\":\"http://www.vinumimporting.com/wp-content/uploads/2012/06/tom-collins.jpg\"}," +
                "{\"name\":\"Mojito\",\"id\":\"mojito\",\"imageURL\":\"http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg\"}," +
                "{\"name\":\"Dry Martini\",\"id\":\"drymartini\",\"imageURL\":\"http://www.cocktailrendezvous.com/images.php?f=files/recipes/images/martini.jpg&w=616&h=347&c=1\"}," +
                "{\"name\":\"Blue Lagoon\",\"id\":\"bluelagoon\",\"imageURL\":\"http://www.youman.dp.ua/images/stories/Nastmygchina/gurman/cocktails/laguna/Blue_Lagoon_Cocktail_Photo.gif\"}]";

        ObjectMapper mapper = new ObjectMapper();
        drinks = mapper.readValue(json, new TypeReference<ArrayList<DrinksListItem>>() { });

//        DrinksListItem amarettoFrost = new DrinksListItem();
//        amarettoFrost.setId("amarettofrost");
//        amarettoFrost.setName("Amaretto Frost");
//        amarettoFrost.setImageURL("http://www.smallscreennetwork.com/videos/cocktail_spirit/morgenthaler-method-amaretto-sour.jpg");
//
//        DrinksListItem americano = new DrinksListItem();
//        americano.setId("americano");
//        americano.setName("Americano");
//        americano.setImageURL("http://www.ganzomag.com/wp-content/uploads/2012/05/americano-cocktail1.jpg");
//
//        DrinksListItem tomCollins = new DrinksListItem();
//        tomCollins.setId("tomcollins");
//        tomCollins.setName("Tom Collins");
//        tomCollins.setImageURL("http://www.vinumimporting.com/wp-content/uploads/2012/06/tom-collins.jpg");
//
//        DrinksListItem mojito = new DrinksListItem();
//        mojito.setId("mojito");
//        mojito.setName("Mojito");
//        mojito.setImageURL("http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg");
//
//        DrinksListItem dryMartini = new DrinksListItem();
//        dryMartini.setId("drymartini");
//        dryMartini.setName("Dry Martini");
//        dryMartini.setImageURL("http://www.cocktailrendezvous.com/images.php?f=files/recipes/images/martini.jpg&w=616&h=347&c=1");
//
//        DrinksListItem blueLagoon = new DrinksListItem();
//        blueLagoon.setId("bluelagoon");
//        blueLagoon.setName("Blue Lagoon");
//        blueLagoon.setImageURL("http://www.youman.dp.ua/images/stories/Nastmygchina/gurman/cocktails/laguna/Blue_Lagoon_Cocktail_Photo.gif");

//        drinks.add(amarettoFrost);
//        drinks.add(americano);
//        drinks.add(tomCollins);
//        drinks.add(mojito);
//        drinks.add(dryMartini);
//        drinks.add(blueLagoon);

        return drinks;
    }
}
