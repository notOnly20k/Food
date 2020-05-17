package com.example.food.utils;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class StringListConverters {

    @TypeConverter
    public static List<String> stringToObject(String value){
        Type listType = new TypeToken<List<String>>() {

        }.getType();
        return new Gson().fromJson(value,listType);
    }

    @TypeConverter
    public static String objectToString(List<String> value){
        return new Gson().toJson(value);
    }


}
