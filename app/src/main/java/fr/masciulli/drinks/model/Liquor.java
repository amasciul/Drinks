package fr.masciulli.drinks.model;

public class Liquor {
    private String name;
    private String imageUrl;
    private float ratio;

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return ratio;
    }
}
