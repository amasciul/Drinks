package fr.masciulli.drinks;

/**
 * Created by Alexandre on 22/10/13.
 */
public class Drink {
    private String mName;

    public Drink(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
