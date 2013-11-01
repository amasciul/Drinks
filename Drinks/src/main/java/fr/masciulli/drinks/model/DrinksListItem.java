package fr.masciulli.drinks.model;

public class DrinksListItem {
    public String id;
    public String name;
    public String imageURL;

    public DrinksListItem() {
    }

    // TODO remove getters/setters (fields are now public because of Retrofit)
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
