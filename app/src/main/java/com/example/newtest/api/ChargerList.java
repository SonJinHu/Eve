package com.example.newtest.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ChargerList {
    @SerializedName("updatedDate")
    private ArrayList<Charger.Server> date;

    public ArrayList<Charger.Server> getDate() {
        return date;
    }

    @SerializedName("company")
    private ArrayList<Charger.Company> company;

    public ArrayList<Charger.Company> getCompany() {
        return company;
    }

    @SerializedName("info")
    @Expose
    private ArrayList<Charger.Info> info;

    public ArrayList<Charger.Info> getInfo() {
        return info;
    }

    @SerializedName("chargerList")
    @Expose
    private ArrayList<Charger.Status> status;

    public ArrayList<Charger.Status> getStatus() {
        return status;
    }

    @SerializedName("bookmark")
    @Expose
    private ArrayList<Charger.Bookmark> bookmark;

    public ArrayList<Charger.Bookmark> getBookmark() {
        return bookmark;
    }

    @SerializedName("comment")
    @Expose
    private ArrayList<Charger.Comment> comment;

    public ArrayList<Charger.Comment> getComment() {
        return comment;
    }

    @SerializedName("comment_my")
    @Expose
    private ArrayList<Charger.MyComment> myComment;

    public ArrayList<Charger.MyComment> getMyComment() {
        return myComment;
    }
}