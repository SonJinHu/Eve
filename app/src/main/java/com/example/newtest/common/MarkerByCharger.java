package com.example.newtest.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.newtest.R;
import com.example.newtest.api.Charger;

public class MarkerByCharger {
    public static View getMarkerViewByManage(Context context, Charger.Info item) {
        @SuppressLint("InflateParams")
        View marker_layout = LayoutInflater.from(context).inflate(R.layout.marker_charger, null);
        ImageView iv = marker_layout.findViewById(R.id.marker_iv);
        switch (item.getChgeMange()) {
            default:
            case "0":
                iv.setImageResource(R.drawable.logo_00);
                break;
            case "10":
                iv.setImageResource(R.drawable.logo_10);
                break;
            case "11":
                iv.setImageResource(R.drawable.logo_11);
                break;
            case "12":
                iv.setImageResource(R.drawable.logo_12);
                break;
            case "13":
                iv.setImageResource(R.drawable.logo_13);
                break;
            case "14":
                iv.setImageResource(R.drawable.logo_14);
                break;
            case "15":
                iv.setImageResource(R.drawable.logo_15);
                break;
            case "16":
                iv.setImageResource(R.drawable.logo_16);
                break;
            case "17":
                iv.setImageResource(R.drawable.logo_17);
                break;
            case "18":
                iv.setImageResource(R.drawable.logo_18);
                break;
            case "19":
                iv.setImageResource(R.drawable.logo_19);
                break;
            case "20":
                iv.setImageResource(R.drawable.logo_20);
                break;
            case "21":
                iv.setImageResource(R.drawable.logo_21);
                break;
            case "22":
                iv.setImageResource(R.drawable.logo_22);
                break;
            case "23":
                iv.setImageResource(R.drawable.logo_23);
                break;
            case "24":
                iv.setImageResource(R.drawable.logo_24);
                break;
            case "25":
                iv.setImageResource(R.drawable.logo_25);
                break;
            case "26":
                iv.setImageResource(R.drawable.logo_26);
                break;
            case "27":
                iv.setImageResource(R.drawable.logo_27);
                break;
            case "28":
                iv.setImageResource(R.drawable.logo_28);
                break;
            case "29":
                iv.setImageResource(R.drawable.logo_29);
                break;
            case "30":
                iv.setImageResource(R.drawable.logo_30);
                break;
            case "31":
                iv.setImageResource(R.drawable.logo_31);
                break;
            case "32":
                iv.setImageResource(R.drawable.logo_32);
                break;
            case "33":
                iv.setImageResource(R.drawable.logo_33);
                break;
            case "34":
                iv.setImageResource(R.drawable.logo_34);
                break;
            case "35":
                iv.setImageResource(R.drawable.logo_35);
                break;
            case "36":
                iv.setImageResource(R.drawable.logo_36);
                break;
            case "37":
                iv.setImageResource(R.drawable.logo_37);
                break;
            case "38":
                iv.setImageResource(R.drawable.logo_38);
                break;
            case "39":
                iv.setImageResource(R.drawable.logo_39);
                break;
            case "40":
                iv.setImageResource(R.drawable.logo_40);
                break;
            case "41":
                iv.setImageResource(R.drawable.logo_41);
                break;
            case "42":
                iv.setImageResource(R.drawable.logo_42);
                break;
            case "43":
                iv.setImageResource(R.drawable.logo_43);
                break;
            case "44":
                iv.setImageResource(R.drawable.logo_44);
                break;
            case "45":
                iv.setImageResource(R.drawable.logo_45);
                break;
            case "46":
                iv.setImageResource(R.drawable.logo_46);
                break;
            case "89":
                iv.setImageResource(R.drawable.logo_89);
                break;
        }
        return marker_layout;
    }

    public static View getMarkerBigViewByManage(Context context, Charger.Info item) {
        @SuppressLint("InflateParams")
        View marker_layout = LayoutInflater.from(context).inflate(R.layout.marker_charger_big, null);
        ImageView iv = marker_layout.findViewById(R.id.marker_iv);
        switch (item.getChgeMange()) {
            default:
            case "0":
                iv.setImageResource(R.drawable.logo_00);
                break;
            case "10":
                iv.setImageResource(R.drawable.logo_10);
                break;
            case "11":
                iv.setImageResource(R.drawable.logo_11);
                break;
            case "12":
                iv.setImageResource(R.drawable.logo_12);
                break;
            case "13":
                iv.setImageResource(R.drawable.logo_13);
                break;
            case "14":
                iv.setImageResource(R.drawable.logo_14);
                break;
            case "15":
                iv.setImageResource(R.drawable.logo_15);
                break;
            case "16":
                iv.setImageResource(R.drawable.logo_16);
                break;
            case "17":
                iv.setImageResource(R.drawable.logo_17);
                break;
            case "18":
                iv.setImageResource(R.drawable.logo_18);
                break;
            case "19":
                iv.setImageResource(R.drawable.logo_19);
                break;
            case "20":
                iv.setImageResource(R.drawable.logo_20);
                break;
            case "21":
                iv.setImageResource(R.drawable.logo_21);
                break;
            case "22":
                iv.setImageResource(R.drawable.logo_22);
                break;
            case "23":
                iv.setImageResource(R.drawable.logo_23);
                break;
            case "24":
                iv.setImageResource(R.drawable.logo_24);
                break;
            case "25":
                iv.setImageResource(R.drawable.logo_25);
                break;
            case "26":
                iv.setImageResource(R.drawable.logo_26);
                break;
            case "27":
                iv.setImageResource(R.drawable.logo_27);
                break;
            case "28":
                iv.setImageResource(R.drawable.logo_28);
                break;
            case "29":
                iv.setImageResource(R.drawable.logo_29);
                break;
            case "30":
                iv.setImageResource(R.drawable.logo_30);
                break;
            case "31":
                iv.setImageResource(R.drawable.logo_31);
                break;
            case "32":
                iv.setImageResource(R.drawable.logo_32);
                break;
            case "33":
                iv.setImageResource(R.drawable.logo_33);
                break;
            case "34":
                iv.setImageResource(R.drawable.logo_34);
                break;
            case "35":
                iv.setImageResource(R.drawable.logo_35);
                break;
            case "36":
                iv.setImageResource(R.drawable.logo_36);
                break;
            case "37":
                iv.setImageResource(R.drawable.logo_37);
                break;
            case "38":
                iv.setImageResource(R.drawable.logo_38);
                break;
            case "39":
                iv.setImageResource(R.drawable.logo_39);
                break;
            case "40":
                iv.setImageResource(R.drawable.logo_40);
                break;
            case "41":
                iv.setImageResource(R.drawable.logo_41);
                break;
            case "42":
                iv.setImageResource(R.drawable.logo_42);
                break;
            case "43":
                iv.setImageResource(R.drawable.logo_43);
                break;
            case "44":
                iv.setImageResource(R.drawable.logo_44);
                break;
            case "45":
                iv.setImageResource(R.drawable.logo_45);
                break;
            case "46":
                iv.setImageResource(R.drawable.logo_46);
                break;
            case "89":
                iv.setImageResource(R.drawable.logo_89);
                break;
        }
        return marker_layout;
    }

    public static Bitmap createBitmapFromMarkerView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();

        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }
}
