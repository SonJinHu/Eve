package com.example.newtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtest.api.Charger;
import com.example.newtest.common.Shared;
import com.google.android.material.appbar.AppBarLayout;

import java.util.ArrayList;

public class Bb_Search extends AppCompatActivity {

    private final String TAG = getClass().getName();
    private RecyclerView recycler;
    private ArrayList<Charger.Info> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bb_search);

        Toolbar toolbar = findViewById(R.id.bb_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        items = Shared.getInfoList(getApplicationContext());

        recycler = findViewById(R.id.bb_recycler);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int scrollDy = 0;
            AppBarLayout appBarLayout = findViewById(R.id.bb_appBarLayout);

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                scrollDy += dy;
                if (scrollDy != 0) {
                    appBarLayout.setElevation((float) 8.0);
                } else {
                    appBarLayout.setElevation((float) 0.0);
                }
            }
        }); // 경계선 between AppBarLayout and RecyclerView
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        EditText et = findViewById(R.id.bb_et_search);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {
                ArrayList<Charger.Info> filterItems = new ArrayList<>();
                for (Charger.Info item : items) {
                    if (item.getSnm().contains(s.toString()))
                        filterItems.add(item);
                }

                recycler.setAdapter(new Bb_SearchAdapter(getApplicationContext(), filterItems));
            }

            @Override
            public void afterTextChanged(final Editable s) {
                ImageView iv_clear = findViewById(R.id.bb_iv_clear);
                if (s.toString().length() == 0) {
                    iv_clear.setVisibility(View.GONE);
                    return;
                }
                iv_clear.setVisibility(View.VISIBLE);
            }
        });
        et.setText("");

        ImageView iv_clear = findViewById(R.id.bb_iv_clear);
        iv_clear.setVisibility(View.GONE);
        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = findViewById(R.id.bb_et_search);
                et.setText("");
            }
        });
    }

    public class Bb_SearchAdapter extends RecyclerView.Adapter<Bb_SearchAdapter.SearchHolder> {

        private Context context;
        private ArrayList<Charger.Info> items;

        Bb_SearchAdapter(Context context, ArrayList<Charger.Info> items ) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_search, parent, false);
            return new SearchHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final SearchHolder holder, final int position) {
            holder.tv.setText(items.get(position).getSnm());

            String address = items.get(position).getAdr();
            if (address.contains(","))
                address = address.split(",")[0];
            if (address.contains("("))
                address = address.split("\\(")[0];
            if (address.contains("  ")) {
                address = address.replace("  ", " ");
            }
            holder.tv_address.setText(address);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class SearchHolder extends RecyclerView.ViewHolder {

            TextView tv;
            TextView tv_address;

            SearchHolder(@NonNull View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.item_search_tv_snm);
                tv_address = itemView.findViewById(R.id.item_search_tv_address);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), B_Main.class);
                        intent.putExtra("sid", items.get(getAdapterPosition()).getSid());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
    }
}
