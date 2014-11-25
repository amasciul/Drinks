package fr.masciulli.drinks.util;

import fr.masciulli.drinks.model.Drink;

public class HtmlUtils {
    public static String getIngredientsHtml(Drink drink) {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String ingredient : drink.ingredients) {
            builder.append("&#8226; ");
            builder.append(ingredient);
            if (++i < drink.ingredients.size()) {
                builder.append("<br>");
            }
        }
        return builder.toString();
    }
}
