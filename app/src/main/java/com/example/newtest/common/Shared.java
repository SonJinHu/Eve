package com.example.newtest.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.newtest.api.Charger;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Shared {
    public static void setUpdatedDate(Context context, String date) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("updated_date", date);
        editor.apply();
    }

    public static String getUpdatedDate(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("updated_date", null);
    }

    public static void setCompanyList(Context context, ArrayList<Charger.Company> items) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("company_list", new Gson().toJson(items));
        editor.apply();
    }

    public static ArrayList<Charger.Company> getCompanyList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("company_list", null);
        Type type = new TypeToken<ArrayList<Charger.Company>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setInfoList(Context context, ArrayList<Charger.Info> items) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("info_list", new Gson().toJson(items));
        editor.apply();
    }

    public static ArrayList<Charger.Info> getInfoList(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("info_list", null);
        Type type = new TypeToken<ArrayList<Charger.Info>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setStatus(Context context, ArrayList<Charger.Status> items) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("status", new Gson().toJson(items));
        editor.apply();
    }

    public static ArrayList<Charger.Status> getStatus(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("status", null);
        Type type = new TypeToken<ArrayList<Charger.Status>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setAccount(Context context, HashMap<String, String> map) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("account", new Gson().toJson(map));
        editor.apply();
    }

    public static HashMap<String, String> getAccount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("account", null);
        Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void setBookmark(Context context, ArrayList<Charger.Bookmark> items) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("bookmark", new Gson().toJson(items));
        editor.apply();
    }

    public static ArrayList<Charger.Bookmark> getBookmark(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("bookmark", null);
        Type type = new TypeToken<ArrayList<Charger.Bookmark>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static void initTypeCheck(Context context) {
        ArrayList<Boolean> array = new ArrayList<>();
        array.add(true);
        array.add(true);
        array.add(true);
        array.add(true);
        array.add(true);
        array.add(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("type_check", new Gson().toJson(array));
        editor.apply();
    }

    public static void initParkingCheck(Context context) {
        ArrayList<Boolean> array = new ArrayList<>();
        array.add(true);
        array.add(false);
        array.add(false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("parking_check", new Gson().toJson(array));
        editor.apply();
    }

    public static void initCompanyCheck(Context context, int count) {
        ArrayList<Boolean> array = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            array.add(true);
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("company_check", new Gson().toJson(array));
        editor.apply();
    }

    public static void setTypeCheck(Context context, ArrayList<Boolean> arrayType) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("type_check", new Gson().toJson(arrayType));
        editor.apply();
    }

    public static void setParkingCheck(Context context, ArrayList<Boolean> arrayType) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("parking_check", new Gson().toJson(arrayType));
        editor.apply();
    }

    public static void setCompanyCheck(Context context, ArrayList<Boolean> array) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("company_check", new Gson().toJson(array));
        editor.apply();
    }

    public static ArrayList<Boolean> getTypeCheck(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("type_check", null);
        Type type = new TypeToken<ArrayList<Boolean>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static ArrayList<Boolean> getParkingCheck(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("parking_check", null);
        Type type = new TypeToken<ArrayList<Boolean>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }

    public static ArrayList<Boolean> getCompanyCheck(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString("company_check", null);
        Type type = new TypeToken<ArrayList<Boolean>>() {
        }.getType();
        return new Gson().fromJson(json, type);
    }
}