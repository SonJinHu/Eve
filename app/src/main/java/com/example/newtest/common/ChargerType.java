package com.example.newtest.common;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.newtest.R;

import java.util.ArrayList;

public class ChargerType {

    private final int colorGray = android.R.color.darker_gray;
    private final int colorBlue = R.color.colorBlue;
    private final int backgroundGray = R.drawable.shape_square_edge_gray;
    private final int backgroundBlue = R.drawable.shape_square_edge_blue;

    private Context context;
    private String[] ctpList;
    private ArrayList<ImageView> imgList = null;
    private ArrayList<TextView> tvList = null;

    public ChargerType(Context context) {
        this.context = context;
    }

    public ChargerType setCtp(String ctp) {
        ctpList = ctp.split(",");
        return this;
    }

    public ChargerType setIvList(ArrayList<ImageView> imgList) {
        this.imgList = imgList;
        return this;
    }

    public ChargerType setTvList(ArrayList<TextView> tvList) {
        this.tvList = tvList;
        return this;
    }

    public void initView() {
        if (tvList == null) {
            initImage();
            return;
        }
        initImage();
        initText();
    }

    private void initImage() {
        for (ImageView img : imgList) {
            img.setColorFilter(ContextCompat.getColor(context, colorGray));
            img.setBackgroundResource(backgroundGray);
        }

        for (String ctp : ctpList) {
            switch (ctp) {
                case "1":
                    //cha
                    imgList.get(1).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(1).setBackgroundResource(backgroundBlue);
                    break;
                case "2":
                    //single
                    imgList.get(3).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(3).setBackgroundResource(backgroundBlue);
                    break;
                case "3":
                    //ac3
                    imgList.get(0).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(0).setBackgroundResource(backgroundBlue);
                    //cha
                    imgList.get(1).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(1).setBackgroundResource(backgroundBlue);
                    break;
                case "4":
                    //com
                    imgList.get(2).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(2).setBackgroundResource(backgroundBlue);
                    break;
                case "5":
                    //cha
                    imgList.get(1).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(1).setBackgroundResource(backgroundBlue);
                    //com
                    imgList.get(2).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(2).setBackgroundResource(backgroundBlue);
                    break;
                case "6":
                    //ac3
                    imgList.get(0).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(0).setBackgroundResource(backgroundBlue);
                    //cha
                    imgList.get(1).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(1).setBackgroundResource(backgroundBlue);
                    //com
                    imgList.get(2).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(2).setBackgroundResource(backgroundBlue);
                    break;
                case "7":
                    //ac3
                    imgList.get(0).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(0).setBackgroundResource(backgroundBlue);
                    break;
                case "89":
                    //h2
                    imgList.get(5).setColorFilter(ContextCompat.getColor(context, colorBlue));
                    imgList.get(5).setBackgroundResource(backgroundBlue);
                    break;
            }
        }
    }

    private void initText() {
        for (TextView tv : tvList) {
            tv.setTextColor(ContextCompat.getColor(context, colorGray));
        }

        for (String ctp : ctpList) {
            switch (ctp) {
                case "1":
                    //cha
                    tvList.get(1).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "2":
                    //single
                    tvList.get(3).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "3":
                    //ac3
                    tvList.get(0).setTextColor(ContextCompat.getColor(context, colorBlue));
                    //cha
                    tvList.get(1).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "4":
                    //com
                    tvList.get(2).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "5":
                    //cha
                    tvList.get(1).setTextColor(ContextCompat.getColor(context, colorBlue));
                    //com
                    tvList.get(2).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "6":
                    //ac3
                    tvList.get(0).setTextColor(ContextCompat.getColor(context, colorBlue));
                    //cha
                    tvList.get(1).setTextColor(ContextCompat.getColor(context, colorBlue));
                    //com
                    tvList.get(2).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "7":
                    //ac3
                    tvList.get(0).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
                case "89":
                    //h2
                    tvList.get(5).setTextColor(ContextCompat.getColor(context, colorBlue));
                    break;
            }
        }
    }
}
