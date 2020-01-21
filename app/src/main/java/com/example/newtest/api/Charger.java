package com.example.newtest.api;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

public class Charger {
    public class Server {
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("count")
        @Expose
        private String count;

        public String getDate() {
            return date;
        }

        public String getCount() {
            return count;
        }
    }

    public class Company {
        @SerializedName("chgeMange")
        @Expose
        private String chgeMange;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("img")
        @Expose
        private String img;

        public String getChgeMange() {
            return chgeMange;
        }

        public String getName() {
            return name;
        }

        public String getImg() {
            return img;
        }
    }

    public class Info implements ClusterItem {
        @SerializedName("sid")
        @Expose
        private String sid;
        @SerializedName("snm")
        @Expose
        private String snm;
        @SerializedName("ctp")
        @Expose
        private String ctp;
        @SerializedName("park")
        @Expose
        private String park;
        @SerializedName("utime")
        @Expose
        private String utime;
        @SerializedName("adr")
        @Expose
        private String adr;
        @SerializedName("x")
        @Expose
        private String x;
        @SerializedName("y")
        @Expose
        private String y;
        @SerializedName("chgeMange")
        @Expose
        private String chgeMange;
        @SerializedName("chge_name")
        @Expose
        private String chgeName;
        @SerializedName("chge_img")
        @Expose
        private String chgeImg;

        public String getSid() {
            return sid;
        }

        public String getSnm() {
            return snm;
        }

        public String getCtp() {
            return ctp;
        }

        public String getPark() {
            if (park.equals("0"))
                return "무료 주차";
            else if (park.equals("1"))
                return "유료 주차";
            else
                return park;
        }

        public String getUtime() {
            if (utime == null || utime.equals(""))
                return "운영시간 정보 없음";
            else
                return utime;
        }

        public String getAdr() {
            return adr;
        }

        private String getX() {
            return x;
        }

        private String getY() {
            return y;
        }

        public String getChgeMange() {
            return chgeMange;
        }

        public String getChgeName() {
            return chgeName;
        }

        public String getChgeImg() {
            return chgeImg;
        }

        @Override
        public LatLng getPosition() {
            return new LatLng(Double.parseDouble(getX()), Double.parseDouble(getY()));
        }
    }

    public class Status {
        @SerializedName("sid")
        @Expose
        private String sid;
        @SerializedName("cid")
        @Expose
        private String cid;
        @SerializedName("ctp")
        @Expose
        private String ctp;
        @SerializedName("cst")
        @Expose
        private String cst;

        public String getSid() {
            return sid;
        }

        public String getCid() {
            return cid;
        }

        public String getCtp() {
            return ctp;
        }

        public String getCst() {
            return cst;
        }
    }

    public static class Bookmark {
        @SerializedName("sid")
        @Expose
        String sid;

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }
    }

    public class Comment {
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("nick")
        @Expose
        String nick;
        @SerializedName("profile_img")
        @Expose
        String profileImg;
        @SerializedName("content")
        @Expose
        String content;
        @SerializedName("img")
        @Expose
        String img;
        @SerializedName("time")
        @Expose
        String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public class MyComment {
        @SerializedName("id")
        @Expose
        String id;
        @SerializedName("sid")
        @Expose
        String sid;
        @SerializedName("content")
        @Expose
        String content;
        @SerializedName("img")
        @Expose
        String img;
        @SerializedName("time")
        @Expose
        String time;
        @SerializedName("nick")
        @Expose
        String nick;
        @SerializedName("profile_img")
        @Expose
        String profileImg;
        @SerializedName("snm")
        @Expose
        String snm;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getSnm() {
            return snm;
        }

        public void setSnm(String snm) {
            this.snm = snm;
        }
    }

    public class ServerResponse {
        private boolean uploadResult;
        private boolean insertResult;
        private String msg;

        public boolean getInsertResult() {
            return insertResult;
        }

        public boolean getUploadResult() {
            return uploadResult;
        }

        public String getMsg() {
            return msg;
        }
    }
}