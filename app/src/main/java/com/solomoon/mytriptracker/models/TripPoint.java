package com.solomoon.mytriptracker.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class TripPoint implements Parcelable {

    @PrimaryKey
    @NonNull
    public String id;

    @ColumnInfo(name = "tripId")
    @ForeignKey(entity = Trip.class, parentColumns = "id", childColumns = "tripId", onDelete = ForeignKey.CASCADE)
    private String tripId;

    @ColumnInfo(name = "latitude")
    private double latitude;

    @ColumnInfo(name = "longitude")
    private double longitude;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @Override
    public int describeContents() {
        return 0;
    }

    public TripPoint() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TripPoint tripPoint = (TripPoint) o;

        if (Double.compare(tripPoint.latitude, latitude) != 0) return false;
        if (Double.compare(tripPoint.longitude, longitude) != 0) return false;
        if (timestamp != tripPoint.timestamp) return false;
        if (!id.equals(tripPoint.id)) return false;
        return tripId != null ? tripId.equals(tripPoint.tripId) : tripPoint.tripId == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id.hashCode();
        result = 31 * result + (tripId != null ? tripId.hashCode() : 0);
        temp = Double.doubleToLongBits(latitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longitude);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    protected TripPoint(Parcel in) {
        id = in.readString();
        tripId = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        timestamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(tripId);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeLong(timestamp);
    }

    public static final Creator<TripPoint> CREATOR = new Creator<TripPoint>() {
        @Override
        public TripPoint createFromParcel(Parcel in) {
            return new TripPoint(in);
        }

        @Override
        public TripPoint[] newArray(int size) {
            return new TripPoint[size];
        }
    };

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}

