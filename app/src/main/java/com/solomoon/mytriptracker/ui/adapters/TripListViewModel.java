package com.solomoon.mytriptracker.ui.adapters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.solomoon.mytriptracker.App;
import com.solomoon.mytriptracker.models.Trip;

import java.util.List;

public class TripListViewModel extends ViewModel {

    private LiveData<List<Trip>> tripLiveData = App.getInstance().getDatabase().tripDao().getAllLiveData();

    public LiveData<List<Trip>> getNoteLiveData() {
        return tripLiveData;
    }

}