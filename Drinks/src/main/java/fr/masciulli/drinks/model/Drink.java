package fr.masciulli.drinks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class Drink implements Parcelable {
    public int id;
    public String name;
    public String imageUrl;
    public String history;
    public String instructions;
    public List<String> ingredients = new LinkedList<String>();
    public String wikipedia;

    public Drink() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(history);
        parcel.writeString(instructions);
        parcel.writeStringList(ingredients);
        parcel.writeString(wikipedia);
    }
}
