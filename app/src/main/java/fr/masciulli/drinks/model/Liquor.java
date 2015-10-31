package fr.masciulli.drinks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Liquor implements Parcelable {
    private String name;
    private String imageUrl;
    private String wikipedia;
    private String history;
    private List<String> otherNames = new ArrayList<>();

    private float ratio;

    public Liquor(Parcel source) {
        name = source.readString();
        imageUrl = source.readString();
        wikipedia = source.readString();
        history = source.readString();
        source.readStringList(otherNames);
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getWikipedia() {
        return wikipedia;
    }

    public String getHistory() {
        return history;
    }

    public List<String> getOtherNames() {
        return otherNames;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getRatio() {
        return ratio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeString(wikipedia);
        dest.writeString(history);
        dest.writeStringList(otherNames);
    }

    public static final Creator<Liquor> CREATOR = new Creator<Liquor>() {
        @Override
        public Liquor createFromParcel(Parcel source) {
            return new Liquor(source);
        }

        @Override
        public Liquor[] newArray(int size) {
            return new Liquor[size];
        }
    };
}
