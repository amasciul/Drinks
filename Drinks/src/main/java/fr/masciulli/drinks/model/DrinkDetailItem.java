package fr.masciulli.drinks.model;

import java.util.LinkedList;
import java.util.List;

public class DrinkDetailItem {
    private String mName;
    private String mImageURL;
    private String mHistory;
    private String mInstructions;
    private List<String> mIngredients = new LinkedList<String>();

    public DrinkDetailItem() {
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

    public void setIntructions(String instruction) {
        mInstructions = instruction;
    }

    public String getInstructions() {
        return mInstructions;
    }
}
