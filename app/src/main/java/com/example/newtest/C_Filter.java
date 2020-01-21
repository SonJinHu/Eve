package com.example.newtest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newtest.adapter.C_CompanyAdapter;
import com.example.newtest.api.Charger;
import com.example.newtest.common.CheckableImageView;
import com.example.newtest.common.Shared;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class C_Filter extends AppCompatActivity implements CheckableImageView.setOnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    CheckableImageView iv_ac3;
    CheckableImageView iv_chaDemo;
    CheckableImageView iv_combo;
    CheckableImageView iv_single;
    CheckableImageView iv_super;
    CheckableImageView iv_h2;

    RadioGroup radioGroup;
    RadioButton rb_all;
    RadioButton rb_charged;
    RadioButton rb_free;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.c_filter);
        initToolbar();
        initView();
        initCheckedData();
        new LoadCompanyImageAndInitChipTask().execute();
    }

    @Override
    public void onBackPressed() {
        finishWithResult();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.c_toolbar);
        toolbar.inflateMenu(R.menu.c_filter_menu);
        // 왼쪽: 닫기
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResult();
            }
        });
        // 오른쪽: 초기화
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_init) {
                    Shared.initTypeCheck(getApplicationContext());
                    Shared.initParkingCheck(getApplicationContext());
                    Shared.initCompanyCheck(getApplicationContext(), Shared.getCompanyList(getApplicationContext()).size());
                    initCheckedData();
                    initChip();
                    return true;
                }
                return false;
            }
        });
    }

    private void initView() {
        iv_ac3 = findViewById(R.id.ba_iv_ac3);
        iv_chaDemo = findViewById(R.id.ba_iv_cha);
        iv_combo = findViewById(R.id.ba_iv_combo);
        iv_single = findViewById(R.id.c_iv_single);
        iv_super = findViewById(R.id.c_iv_super);
        iv_h2 = findViewById(R.id.c_iv_h2);

        iv_ac3.setOnCheckedChangeListener(this);
        iv_chaDemo.setOnCheckedChangeListener(this);
        iv_combo.setOnCheckedChangeListener(this);
        iv_single.setOnCheckedChangeListener(this);
        iv_super.setOnCheckedChangeListener(this);
        iv_h2.setOnCheckedChangeListener(this);

        radioGroup = findViewById(R.id.c_rg_parking);
        rb_all = findViewById(R.id.c_rb_all);
        rb_charged = findViewById(R.id.c_rb_charged);
        rb_free = findViewById(R.id.c_rb_free);

        radioGroup.setOnCheckedChangeListener(this);

        TextView tv_select = findViewById(R.id.c_tv_select);
        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Charger.Company> companyList = Shared.getCompanyList(getApplicationContext());
                ArrayList<Boolean> checkList = Shared.getCompanyCheck(getApplicationContext());

                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                @SuppressLint("InflateParams") View popupView = layoutInflater.inflate(R.layout.c_filter_popup, null);

                AppCompatCheckedTextView ctv = popupView.findViewById(R.id.popup_appCompatCheckedTextView);

                RecyclerView recyclerView = popupView.findViewById(R.id.popup_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                C_CompanyAdapter adapter = new C_CompanyAdapter(getApplicationContext(), ctv, companyList, checkList);
                recyclerView.setAdapter(adapter);

                PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setElevation(10);
                popupWindow.showAtLocation(popupView, Gravity.END, 0, 0);
                popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        initChip();
                    }
                });
            }
        });
    }

    private void initCheckedData() {
        ArrayList<CheckableImageView> imageViewArrayList = new ArrayList<>();
        imageViewArrayList.add(iv_ac3);
        imageViewArrayList.add(iv_chaDemo);
        imageViewArrayList.add(iv_combo);
        imageViewArrayList.add(iv_single);
        imageViewArrayList.add(iv_super);
        imageViewArrayList.add(iv_h2);

        ArrayList<Boolean> typeCheckList = Shared.getTypeCheck(getApplicationContext());
        for (int i = 0; i < typeCheckList.size(); i++) {
            if (typeCheckList.get(i)) {
                imageViewArrayList.get(i).setChecked(true);
            } else {
                imageViewArrayList.get(i).setChecked(false);
            }
        }

        ArrayList<RadioButton> radioButtonArrayList = new ArrayList<>();
        radioButtonArrayList.add(rb_all);
        radioButtonArrayList.add(rb_charged);
        radioButtonArrayList.add(rb_free);

        ArrayList<Boolean> parkingCheckList = Shared.getParkingCheck(getApplicationContext());
        for (int i = 0; i < parkingCheckList.size(); i++) {
            if (parkingCheckList.get(i)) {
                radioButtonArrayList.get(i).setChecked(true);
            } else {
                radioButtonArrayList.get(i).setChecked(false);
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class LoadCompanyImageAndInitChipTask extends AsyncTask<String, Chip, Long> {

        ArrayList<Drawable> imageList;
        ArrayList<Charger.Company> companyList;
        ArrayList<Boolean> checkList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            companyList = Shared.getCompanyList(getApplicationContext());
            checkList = Shared.getCompanyCheck(getApplicationContext());
        }

        @Override
        protected Long doInBackground(String... strings) {
            imageList = new ArrayList<>();
            for (int i = 0; i < companyList.size(); i++) {
                try {
                    Bitmap bitmap = Glide.with(C_Filter.this)
                            .asBitmap()
                            .load("http://115.68.223.21" + companyList.get(i).getImg())
                            .into(100, 100)
                            .get();
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    imageList.add(drawable);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            initCompanyCount();
            ChipGroup chipGroup = findViewById(R.id.c_chipGroup);
            for (int i = 0; i < companyList.size(); i++) {
                Chip chip = new Chip(C_Filter.this);
                chip.setText(companyList.get(i).getName());
                chip.setChipIcon(imageList.get(i));
                chip.setChipBackgroundColorResource(R.color.colorBlueBackground);
                chip.setChipStrokeColorResource(R.color.colorBlueBorder);
                chip.setChipStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()));
                if (!checkList.get(i)) {
                    chip.setVisibility(View.GONE);
                }
                chipGroup.addView(chip);
            }
        }
    }

    private void initChip() {
        initCompanyCount();
        ArrayList<Charger.Company> companyList = Shared.getCompanyList(getApplicationContext());
        ArrayList<Boolean> checkList = Shared.getCompanyCheck(getApplicationContext());
        ChipGroup chipGroup = findViewById(R.id.c_chipGroup);
        for (int i = 0; i < companyList.size(); i++) {
            View view = chipGroup.getChildAt(i);
            if (!checkList.get(i)) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initCompanyCount() {
        ArrayList<Charger.Company> companyList = Shared.getCompanyList(getApplicationContext());
        TextView tv_countAll = findViewById(R.id.c_tv_countAll);
        String string = "/" + companyList.size();
        tv_countAll.setText(string);

        ArrayList<Boolean> checkList = Shared.getCompanyCheck(getApplicationContext());
        int count = 0;
        for (int i = 0; i < checkList.size(); i++) {
            if (checkList.get(i))
                count++;
        }

        TextView tv = findViewById(R.id.c_tv_count);
        tv.setText(String.valueOf(count));
    }

    /**
     * 기존의 필터 설정 값을 받아옴 from B_Main
     * C_Filter 종료 전, 기존의 설정 값과 현재의 설정 값을 비교
     * 다른 것이 하나라도 있다면, 'B_Main'에서 Data 새로고침
     * 다른 것이 없다면, 새로고침하지 않음
     */
    private void finishWithResult() {
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        String type = bundle.getString("typeCheck");
        String park = bundle.getString("parkCheck");
        String company = bundle.getString("companyCheck");

        boolean a = !Objects.equals(type, new Gson().toJson(Shared.getTypeCheck(getApplicationContext())));
        boolean b = !Objects.equals(park, new Gson().toJson(Shared.getParkingCheck(getApplicationContext())));
        boolean c = !Objects.equals(company, new Gson().toJson(Shared.getCompanyCheck(getApplicationContext())));

        Intent intent = new Intent(getApplicationContext(), B_Main.class);
        if (a || b || c) {
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    @Override
    public void onCheckedChanged(boolean isChecked) {
        ArrayList<Boolean> array = new ArrayList<>();
        array.add(iv_ac3.isChecked());
        array.add(iv_chaDemo.isChecked());
        array.add(iv_combo.isChecked());
        array.add(iv_single.isChecked());
        array.add(iv_super.isChecked());
        array.add(iv_h2.isChecked());
        Shared.setTypeCheck(getApplicationContext(), array);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        ArrayList<Boolean> array = new ArrayList<>();
        array.add(rb_all.isChecked());
        array.add(rb_charged.isChecked());
        array.add(rb_free.isChecked());
        Shared.setParkingCheck(getApplicationContext(), array);
    }

//    private void initTypeCheck() {
//        CheckableImageView iv_ac3 = findViewById(R.id.ba_iv_ac3);
//        CheckableImageView iv_chaDemo = findViewById(R.id.ba_iv_cha);
//        CheckableImageView iv_combo = findViewById(R.id.ba_iv_combo);
//        CheckableImageView iv_single = findViewById(R.id.ba_iv_single);
//        CheckableImageView iv_super = findViewById(R.id.ba_iv_super);
//        CheckableImageView iv_h2 = findViewById(R.id.ba_iv_h2);
//
//        TextView tv_ac3 = findViewById(R.id.ba_tv_ac3);
//        TextView tv_chaDemo = findViewById(R.id.ba_tv_cha);
//        TextView tv_combo = findViewById(R.id.ba_tv_combo);
//        TextView tv_single = findViewById(R.id.ba_tv_single);
//        TextView tv_super = findViewById(R.id.ba_tv_super);
//        TextView tv_h2 = findViewById(R.id.ba_tv_h2);
//
//        iv_ac3.setOnCheckedChangeListener(getListener(tv_ac3));
//        iv_chaDemo.setOnCheckedChangeListener(getListener(tv_chaDemo));
//        iv_combo.setOnCheckedChangeListener(getListener(tv_combo));
//        iv_single.setOnCheckedChangeListener(getListener(tv_single));
//        iv_super.setOnCheckedChangeListener(getListener(tv_super));
//        iv_h2.setOnCheckedChangeListener(getListener(tv_h2));
//    }
//
//
//
//    private CheckableImageView.setOnCheckedChangeListener getListener(final TextView tv) {
//        return new CheckableImageView.setOnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(boolean isChecked) {
//                if (isChecked) {
//                    tv.setTextColor(getResources().getColor(R.color.colorBlack));
//                    return;
//                }
//                tv.setTextColor(getResources().getColor(R.color.colorDefault));
//            }
//        };
//    }
}