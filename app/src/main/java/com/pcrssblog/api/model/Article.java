package com.pcrssblog.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Article Info
 */
public class Article implements Parcelable {
    /**
     * Article Title
     */
    private String mTitle;
    /**
     * Article Content
     */
    private String mContent;
    /**
     * Article Image Link
     */
    private String mImageLink;
    /**
     * Article Web Link
     */
    private String mWebLink;

    /**
     * Getters
     */
    public String getTitle() {
        return this.mTitle;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getImageLink() {
        return this.mImageLink;
    }

    public String getWebLink() {
        return this.mWebLink;
    }

    public Article(final String pTitle, final String pContent, final String pImageLink, final String pWebLink) {
        this.mTitle = pTitle;
        this.mContent = pContent;
        this.mImageLink = pImageLink;
        this.mWebLink = pWebLink;
    }

    @Override
    public boolean equals(final Object pO) {
        if (this == pO) {
            return true;
        }

        if (pO == null || getClass() != pO.getClass()) {
            return false;
        }

        final Article channel = (Article) pO;

        if (this.mTitle != null ? !this.mTitle.equals(channel.mTitle) : channel.mTitle != null) {
            return false;
        }
        if (this.mContent != null ? !this.mContent.equals(channel.mContent) : channel.mContent != null) {
            return false;
        }
        if (this.mImageLink != null ? !this.mImageLink.equals(channel.mImageLink) : channel.mImageLink != null) {
            return false;
        }
        return this.mWebLink != null ? this.mWebLink.equals(channel.mWebLink) : channel.mWebLink == null;
    }

    @Override
    public int hashCode() {
        int result = this.mTitle != null ? this.mTitle.hashCode() : 0;
        result = 31 * result + (this.mContent != null ? this.mContent.hashCode() : 0);
        result = 31 * result + (this.mImageLink != null ? this.mImageLink.hashCode() : 0);
        result = 31 * result + (this.mWebLink != null ? this.mWebLink.hashCode() : 0);
        return result;
    }

    /**
     * For Parcelable
     */

    public static Parcelable.Creator<Article> getCreator() {
        return CREATOR;
    }

    private Article(Parcel in) {
        readFromParcel(in);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mTitle);
        out.writeString(this.mContent);
        out.writeString(this.mImageLink);
        out.writeString(this.mWebLink);
    }

    public void readFromParcel(Parcel in) {
        this.mTitle = in.readString();
        this.mContent = in.readString();
        this.mImageLink = in.readString();
        this.mWebLink = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}