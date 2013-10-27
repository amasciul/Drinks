package fr.masciulli.drinks.model;

import java.util.LinkedList;
import java.util.List;

public class Drink {
    private String mName;
    private String mImageURL;
    private String mHistory;
    private List<String> mIngredients = new LinkedList<String>();
    private List<String> mInstructions = new LinkedList<String>();

    public Drink(String name, String imageURL) {
        mName = name;
        mImageURL = imageURL;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setImageURL(String imageURL) {
        mImageURL = imageURL;
    }

    public String getHistory() {
        return mHistory;
    }

    public void setHistory(String history) {
        mHistory = history;
    }

    public void addIngredient(String ingredient) {
        mIngredients.add(ingredient);
    }

    public List<String> getIngredients() {
        return mIngredients;
    }

    public void addIntruction(String instruction) {
        mInstructions.add(instruction);
    }

    public List<String> getInstructions() {
        return mInstructions;
    }
}
