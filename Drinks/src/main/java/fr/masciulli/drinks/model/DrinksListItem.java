package fr.masciulli.drinks.model;

public class DrinksListItem {
    private String mName;
    private String mImageURL;

    public DrinksListItem() {
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
}
