package com.pcrssblog.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Channel Info
 */
public class Channel implements Parcelable {
    /**
     * Channel Title
     */
    private String mTitle;

    /**
     * List of Articles belong to the channel
     */
    private List<Article> mArticles;

    /**
     * Getters
     */
    public String getTitle() {
        return this.mTitle;
    }

    public List<Article> getArticles() {
        return this.mArticles;
    }

    public Channel(final String pTitle, final List<Article> pArticles) {
        this.mTitle = pTitle;
        this.mArticles = pArticles;
    }

    @Override
    public boolean equals(final Object pO) {
        if (this == pO) {
            return true;
        }

        if (pO == null || getClass() != pO.getClass()) {
            return false;
        }

        final Channel channel = (Channel) pO;

        if (this.mTitle != null ? !this.mTitle.equals(channel.mTitle) : channel.mTitle != null) {
            return false;
        }
        return this.mArticles != null ? this.mArticles.equals(channel.mArticles) : channel.mArticles == null;
    }

    @Override
    public int hashCode() {
        int result = this.mTitle != null ? this.mTitle.hashCode() : 0;
        result = 31 * result + (this.mArticles != null ? this.mArticles.hashCode() : 0);
        return result;
    }

    /**
     * For Parcelable
     */

    public static Parcelable.Creator<Channel> getCreator() {
        return CREATOR;
    }

    private Channel(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mTitle);
        out.writeList(this.mArticles);
    }

    public void readFromParcel(Parcel in) {
        this.mTitle = in.readString();
        this.mArticles = new ArrayList<Article>();
        in.readList(this.mArticles, null);
    }

    public static final Parcelable.Creator<Channel> CREATOR = new Parcelable.Creator<Channel>() {
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };
}