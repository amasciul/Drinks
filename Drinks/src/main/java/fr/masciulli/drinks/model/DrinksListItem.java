package fr.masciulli.drinks.model;

import java.util.LinkedList;
import java.util.List;

public class DrinksListItem {
    public int id;
    public String name;
    public String imageUrl;
    public String history;
    public String instructions;
    public List<String> ingredients = new LinkedList<String>();

    public DrinksListItem() {
    }
}
