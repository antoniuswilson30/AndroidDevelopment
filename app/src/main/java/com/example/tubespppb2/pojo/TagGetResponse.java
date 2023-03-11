package com.example.tubespppb2.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TagGetResponse implements Parcelable {
    @SerializedName("id")
    public String id;
    @SerializedName("tag")
    public String tag;

    protected TagGetResponse(Parcel in) {
        id = in.readString();
        tag = in.readString();
    }

    public static final Creator<TagGetResponse> CREATOR = new Creator<TagGetResponse>() {
        @Override
        public TagGetResponse createFromParcel(Parcel in) {
            return new TagGetResponse(in);
        }

        @Override
        public TagGetResponse[] newArray(int size) {
            return new TagGetResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tag);
    }
}
