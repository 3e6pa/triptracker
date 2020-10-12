package com.solomoon.mytriptracker.data.migrations;

import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class RoomMigration {

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            String query = "CREATE TABLE IF NOT EXISTS AppSettings (id INTEGER NOT NULL, currentUserId TEXT DEFAULT NULL)";
            database.execSQL(query);
        }
    };
}
