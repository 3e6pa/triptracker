package com.solomoon.mytriptracker.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Entity
public class AppSettings implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "currentUerId")
    private String currentUserId;

    public AppSettings(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppSettings that = (AppSettings) o;

        if (id != that.id) return false;
        return currentUserId != null ? currentUserId.equals(that.currentUserId) : that.currentUserId == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (currentUserId != null ? currentUserId.hashCode() : 0);
        return result;
    }


    protected AppSettings(Parcel in){
        id = in.readInt();
        currentUserId = in.readString();
    }

    public static final Creator<AppSettings> CREATOR = new Creator<AppSettings>() {
        @Override
        public AppSettings createFromParcel(Parcel in) {
            return new AppSettings(in);
        }

        @Override
        public AppSettings[] newArray(int size) {
            return new AppSettings[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(currentUserId);
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public void setId(int id) {
        this.id = id;
    }

}
