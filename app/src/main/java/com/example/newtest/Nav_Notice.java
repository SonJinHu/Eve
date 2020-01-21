package com.example.newtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtest.api.Charger;
import com.google.gson.Gson;

import java.util.ArrayList;

public class Nav_Notice extends AppCompatActivity {

    private final String TAG = getClass().getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_notice);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                        int pos = getAdapterPosition();
                        Bundle bundle = new Bundle();
                        bundle.putString("item", new Gson().toJson(items.get(pos)));

                        Intent intent = new Intent(getApplicationContext(), B_Main.class);
                        intent.putExtra("bundle", bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
    }
}
