package fr.masciulli.drinks.model;

import java.util.LinkedList;
import java.util.List;

public class DrinkDetailItem {
    public String id;
    public String name;
    public String imageURL;
    public String history;
    public String instructions;
    public List<String> ingredients = new LinkedList<String>();

    public DrinkDetailItem() {
    }

    //TODO remove getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void addIngredient(String ingredient) {
        ingredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIntructions(String instruction) {
        instructions = instruction;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
