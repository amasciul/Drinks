package fr.masciulli.drinks.model;

public class Drink {
    private String mName;
    private String mImageURL;

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
}
