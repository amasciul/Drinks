package fr.masciulli.drinks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Liquor implements Parcelable {
    public int id;
    public String name;
    public String imageUrl;
    public String history;
    public String wikipedia;

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
        parcel.writeString(wikipedia);
    }
}
