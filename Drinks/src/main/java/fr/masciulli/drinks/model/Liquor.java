package fr.masciulli.drinks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Liquor implements Parcelable {
    public String name;
    public String imageUrl;
    public String history;
    public String wikipedia;
    public List<String> otherNames = new ArrayList<String>();

    public Liquor(Parcel parcel) {
        name = parcel.readString();
        imageUrl = parcel.readString();
        history = parcel.readString();
        wikipedia = parcel.readString();
        parcel.readStringList(otherNames);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(imageUrl);
        parcel.writeString(history);
        parcel.writeString(wikipedia);
        parcel.writeStringList(otherNames);
    }

    public static final Parcelable.Creator<Liquor> CREATOR = new Parcelable.Creator<Liquor>() {
        public Liquor createFromParcel(Parcel in) {
            return new Liquor(in);
        }

        public Liquor[] newArray(int size) {
            return new Liquor[size];
        }
    };
}