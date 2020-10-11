package com.solomoon.mytriptracker.model;

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
    public String id;
    
    @ColumnInfo(name = "userId")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns ="userId", onDelete = ForeignKey.CASCADE)
    public String userId;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "startTimestamp")
    public long sartTimestamp;

    @ColumnInfo(name = "endTimestamp")
    public long endTimestamp;

    public Trip() {
        this.id = UUID.randomUUID().toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return sartTimestamp == trip.sartTimestamp &&
                endTimestamp == trip.endTimestamp &&
                id.equals(trip.id) &&
                Objects.equals(name, trip.name);
    }

    @Override
    public int hashCode() {
        int result =  id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (sartTimestamp ^ (sartTimestamp >>> 32));
        result = 31 *  result + (int) (endTimestamp ^ (endTimestamp >>> 32));
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
}
