package com.kstu.myplanner.database;

import androidx.room.TypeConverter;

import com.kstu.myplanner.model.Priority;

public class Converters {
    @TypeConverter
    public static Priority fromString(String value) {
        return value == null ? null : Priority.valueOf(value);
    }

    @TypeConverter
    public static String toString(Priority priority) {
        return priority == null ? null : priority.name();
    }
}
