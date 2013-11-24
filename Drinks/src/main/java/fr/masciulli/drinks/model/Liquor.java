package fr.masciulli.drinks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Liquor implements Parcelable {
    public int id;
    public String name;
    public String imageUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(imageUrl);
    }
}
