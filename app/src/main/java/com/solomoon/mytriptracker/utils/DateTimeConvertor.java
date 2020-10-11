package com.solomoon.mytriptracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeConvertor {
    public static String millisecToDateTime(long milliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        return formatter.format(new Date(milliseconds));
    }
}
