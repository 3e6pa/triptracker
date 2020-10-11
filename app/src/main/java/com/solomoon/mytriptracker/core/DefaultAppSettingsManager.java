package com.solomoon.mytriptracker.core;

import com.solomoon.mytriptracker.data.AppDatabase;
import com.solomoon.mytriptracker.data.AppSettingsDao;
import com.solomoon.mytriptracker.model.AppSettings;

public class DefaultAppSettingsManager {

    private AppSettingsDao appSettingsDao;

    public DefaultAppSettingsManager(AppDatabase appDatabase) {
        appSettingsDao = appDatabase.appSettingsDao();
    }

    public AppSettings getCurrentAppSettings() {
        AppSettings appSettings = appSettingsDao.getAppSettings();
        if (appSettings == null) {
            appSettings = new AppSettings();
            appSettingsDao.insert(appSettings);
            appSettings = appSettingsDao.getAppSettings();
        }
        return appSettings;
    }

    public void updateCurrentUserId(String currentUserId) {
        AppSettings appSettings = getCurrentAppSettings();
        appSettings.setCurrentUserId(currentUserId);
        appSettingsDao.update(appSettings);
    }
}
