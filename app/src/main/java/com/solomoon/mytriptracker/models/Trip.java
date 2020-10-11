package com.solomoon.mytriptracker.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

@Entity
public class Trip implements Parcelable {

    @PrimaryKey
    @NonNull
    private String id;
    
    @ColumnInfo(name = "userId")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns ="userId", onDelete = ForeignKey.CASCADE)
    private String userId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "startTimestamp")
    private long sartTimestamp;


    @ColumnInfo(name = "endTimestamp")
    private long endTimestamp;

    public Trip() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (sartTimestamp != trip.sartTimestamp) return false;
        if (endTimestamp != trip.endTimestamp) return false;
        if (!id.equals(trip.id)) return false;
        if (userId != null ? !userId.equals(trip.userId) : trip.userId != null) return false;
        return name != null ? name.equals(trip.name) : trip.name == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (sartTimestamp ^ (sartTimestamp >>> 32));
        result = 31 * result + (int) (endTimestamp ^ (endTimestamp >>> 32));
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeLong(sartTimestamp);
        dest.writeLong(endTimestamp);
    }

    protected Trip(Parcel in) {
        id = in.readString();
        name = in.readString();
        sartTimestamp = in.readLong();
        endTimestamp = in.readLong();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSartTimestamp() {
        return sartTimestamp;
    }

    public void setSartTimestamp(long sartTimestamp) {
        this.sartTimestamp = sartTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

}
