package fr.masciulli.drinks.data;

import fr.masciulli.drinks.model.DrinkDetailItem;

public class DrinkProvider {
    public static DrinkDetailItem getDrink(String drinkId) {

        //TODO retrieve full Drink detail using drinkId

        DrinkDetailItem mojito = new DrinkDetailItem();
        mojito.setName("Mojito");
        mojito.setImageURL("http://2eat2drink.files.wordpress.com/2011/04/mojito-final2.jpg");
        mojito.setHistory("The mojito is one of the most famous rum-based highballs. There are several versions of the mojito.");
        mojito.addIngredient("3 cl lime juice");
        mojito.addIngredient("6 leaves  of mint");
        mojito.addIngredient("2 teaspoons sugar");
        mojito.addIngredient("Soda water");
        mojito.setIntructions("Mint sprigs muddled with sugar and lime juice. Rum added and topped with soda water. Garnished with sprig of mint leaves. Served with a straw.");
        return mojito;
    }
}
