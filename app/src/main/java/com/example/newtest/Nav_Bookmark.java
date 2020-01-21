package com.example.newtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtest.api.ApiService;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.api.RetroClient;
import com.example.newtest.common.ChargerType;
import com.example.newtest.common.Server;
import com.example.newtest.common.Shared;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nav_Bookmark extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_bookmark);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                boolean orderAdd;
                boolean orderName;
                if (i == 0) { // 추가 순
                    orderAdd = true;
                    orderName = false;
                } else if (i == 1) { // 이름 순
                    orderAdd = false;
                    orderName = true;
                } else { // 기본(추가 순)
                    orderAdd = true;
                    orderName = false;
                }
                loadMyBookmark(orderAdd, orderName);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void loadMyBookmark(boolean orderAdd, boolean orderName) {
        final View progress = findViewById(R.id.progressBar);
        final RecyclerView recycler = findViewById(R.id.recycler);
        final View nothingView = findViewById(R.id.view_nothing);

        progress.setVisibility(View.VISIBLE);
        nothingView.setVisibility(View.GONE);
        recycler.setVisibility(View.GONE);

        HashMap<String, String> map = Shared.getAccount(getApplicationContext());
        String id = map.get("id");

        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getMyBookmark(id, orderAdd, orderName);
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.Info> items = response.body().getInfo();
                        if (items.size() == 0) {
                            progress.setVisibility(View.GONE);
                            nothingView.setVisibility(View.VISIBLE);
                            recycler.setVisibility(View.GONE);
                            return;
                        }

                        progress.setVisibility(View.GONE);
                        nothingView.setVisibility(View.GONE);
                        recycler.setVisibility(View.VISIBLE);
                        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycler.setAdapter(new Nav_BookmarkAdapter(getApplicationContext(), items));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    public class Nav_BookmarkAdapter extends RecyclerView.Adapter<Nav_BookmarkAdapter.Holder> {

        private Context context;
        private ArrayList<Charger.Info> items;

        Nav_BookmarkAdapter(Context context, ArrayList<Charger.Info> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            View view = inflater.inflate(R.layout.item_bookmark_my, parent, false);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            holder.tv_snm.setText(items.get(position).getSnm());
            holder.tv_parking.setText(items.get(position).getPark());
            holder.tv_utime.setText(items.get(position).getUtime());

            ArrayList<ImageView> imgList = new ArrayList<>();
            imgList.add(holder.iv1);
            imgList.add(holder.iv2);
            imgList.add(holder.iv3);
            imgList.add(holder.iv4);
            imgList.add(holder.iv5);
            imgList.add(holder.iv6);
            ArrayList<TextView> tvList = new ArrayList<>();
            tvList.add(holder.tv1);
            tvList.add(holder.tv2);
            tvList.add(holder.tv3);
            tvList.add(holder.tv4);
            tvList.add(holder.tv5);
            tvList.add(holder.tv6);
            new ChargerType(Nav_Bookmark.this)
                    .setCtp(items.get(position).getCtp())
                    .setIvList(imgList)
                    .setTvList(tvList)
                    .initView();

            holder.btn_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), B_Main.class);
                    intent.putExtra("sid", items.get(position).getSid());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            holder.tb_bookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        new Server(Nav_Bookmark.this).saveBookmark(items.get(position).getSid());
                    } else {
                        new Server(Nav_Bookmark.this).deleteBookmark(items.get(position).getSid());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class Holder extends RecyclerView.ViewHolder {

            TextView tv_snm;
            TextView tv_parking;
            TextView tv_utime;
            ImageView iv1;
            ImageView iv2;
            ImageView iv3;
            ImageView iv4;
            ImageView iv5;
            ImageView iv6;
            TextView tv1;
            TextView tv2;
            TextView tv3;
            TextView tv4;
            TextView tv5;
            TextView tv6;
            AppCompatButton btn_map;
            AppCompatToggleButton tb_bookmark;

            Holder(@NonNull View itemView) {
                super(itemView);
                tv_snm = itemView.findViewById(R.id.item_bookmark_my_tv_snm);
                tv_parking = itemView.findViewById(R.id.item_bookmark_my_tv_parking);
                tv_utime = itemView.findViewById(R.id.item_bookmark_my_tv_utime);
                iv1 = itemView.findViewById(R.id.ctp_square_iv_ac3);
                iv2 = itemView.findViewById(R.id.ctp_square_iv_cha);
                iv3 = itemView.findViewById(R.id.ctp_square_iv_combo);
                iv4 = itemView.findViewById(R.id.ctp_square_iv_single);
                iv5 = itemView.findViewById(R.id.ctp_square_iv_super);
                iv6 = itemView.findViewById(R.id.ctp_square_iv_h2);
                tv1 = itemView.findViewById(R.id.ctp_square_tv_ac3);
                tv2 = itemView.findViewById(R.id.ctp_square_tv_cha);
                tv3 = itemView.findViewById(R.id.ctp_square_tv_combo);
                tv4 = itemView.findViewById(R.id.ctp_square_tv_single);
                tv5 = itemView.findViewById(R.id.ctp_square_tv_super);
                tv6 = itemView.findViewById(R.id.ctp_square_tv_h2);
                btn_map = itemView.findViewById(R.id.item_bookmark_my_acb_map);
                tb_bookmark = itemView.findViewById(R.id.item_bookmark_my_actb_bookmark);
            }
        }
    }
}
