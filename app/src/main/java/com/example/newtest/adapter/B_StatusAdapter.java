package com.example.newtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtest.R;
import com.example.newtest.api.Charger;

import java.util.ArrayList;

public class B_StatusAdapter extends RecyclerView.Adapter<B_StatusAdapter.BottomChargerViewHolder> {

    private final int PROGRESS = 0;
    private final int STATUS = 1;

    private Context context;
    private ArrayList<Charger.Status> items;
    private boolean finish;
    private int machineCount;

    public B_StatusAdapter(Context context, int machineCount, boolean finish) {
        this.context = context;
        this.machineCount = machineCount;
        this.finish = finish;
    }

    public B_StatusAdapter(Context context, ArrayList<Charger.Status> items, boolean finish) {
        this.context = context;
        this.items = items;
        this.finish = finish;
    }

    @Override
    public int getItemViewType(int position) {
        if (finish)
            return STATUS;
        else
            return PROGRESS;
    }

    @NonNull
    @Override
    public BottomChargerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == PROGRESS)
            layout = R.layout.item_status_progress;
        else
            layout = R.layout.item_status;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, parent, false);
        return new BottomChargerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomChargerViewHolder holder, final int position) {
        if (holder.getItemViewType() == PROGRESS) {
            return;
        }

        int a = R.drawable.shape_round_edge_gray;
        int b = R.drawable.shape_round_edge_blue;
        int c = android.R.color.darker_gray;
        int d = R.color.colorBlue;

        holder.iv_ac3.setBackgroundResource(a);
        holder.iv_ac3.setColorFilter(ContextCompat.getColor(context, c));
        holder.iv_cha.setBackgroundResource(a);
        holder.iv_cha.setColorFilter(ContextCompat.getColor(context, c));
        holder.iv_com.setBackgroundResource(a);
        holder.iv_com.setColorFilter(ContextCompat.getColor(context, c));
        holder.iv_single.setBackgroundResource(a);
        holder.iv_single.setColorFilter(ContextCompat.getColor(context, c));

        holder.view_super.setVisibility(View.GONE);
        holder.view_h2.setVisibility(View.GONE);

        //todo: 테슬라 추가
        switch (items.get(position).getCtp()) {
            case "01":
                holder.iv_cha.setBackgroundResource(b);
                holder.iv_cha.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "02":
                holder.iv_single.setBackgroundResource(b);
                holder.iv_single.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "03":
                holder.iv_cha.setBackgroundResource(b);
                holder.iv_cha.setColorFilter(ContextCompat.getColor(context, d));
                holder.iv_ac3.setBackgroundResource(b);
                holder.iv_ac3.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "04":
                holder.iv_com.setBackgroundResource(b);
                holder.iv_com.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "05":
                holder.iv_cha.setBackgroundResource(b);
                holder.iv_cha.setColorFilter(ContextCompat.getColor(context, d));
                holder.iv_com.setBackgroundResource(b);
                holder.iv_com.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "06":
                holder.iv_cha.setBackgroundResource(b);
                holder.iv_cha.setColorFilter(ContextCompat.getColor(context, d));
                holder.iv_ac3.setBackgroundResource(b);
                holder.iv_ac3.setColorFilter(ContextCompat.getColor(context, d));
                holder.iv_com.setBackgroundResource(b);
                holder.iv_com.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "07":
                holder.iv_ac3.setBackgroundResource(b);
                holder.iv_ac3.setColorFilter(ContextCompat.getColor(context, d));
                break;
            case "89":
                holder.view_h2.setVisibility(View.VISIBLE);
                break;
        }

        holder.tv_cst.setText(cst(items.get(position).getCst()));
        switch (items.get(position).getCst()) {
            case "1": //통신미연결
            case "6": //예약중
            case "8": //설치예정
                holder.view_color.setBackgroundResource(android.R.color.darker_gray);
                break;
            case "3": //충전중
            case "9": //타기관
                holder.view_color.setBackgroundResource(android.R.color.holo_green_dark);
                break;
            case "2": //대기중
                holder.view_color.setBackgroundResource(android.R.color.holo_blue_dark);
                break;
            case "4": //운영중지
            case "5": //점검중
                holder.view_color.setBackgroundResource(android.R.color.holo_red_dark);
                break;
            case "7": //시범운영
                holder.view_color.setBackgroundResource(android.R.color.holo_orange_dark);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (finish) {
            return items.size();
        }
        return machineCount;
    }

    class BottomChargerViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_ac3;
        ImageView iv_cha;
        ImageView iv_com;
        ImageView iv_single;

        View view_super;
        View view_h2;

        View view_color;
        TextView tv_cst;

        BottomChargerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_ac3 = itemView.findViewById(R.id.b_sheet_front_iv_ac3);
            iv_cha = itemView.findViewById(R.id.b_sheet_front_iv_cha);
            iv_com = itemView.findViewById(R.id.b_sheet_front_iv_combo);
            iv_single = itemView.findViewById(R.id.b_sheet_front_iv_single);
            view_super = itemView.findViewById(R.id.item_constraint_super);
            view_h2 = itemView.findViewById(R.id.item_constraint_h2);
            view_color = itemView.findViewById(R.id.item_status_constraint_cst);
            tv_cst = itemView.findViewById(R.id.item_status_tv_cst);
        }
    }

    String cst(String cst) {
        switch (cst) {
            case "1":
                return "통신미연결";
            case "2": //대기중
                return "충전가능";
            case "3": //충전중
            case "9": //타기관
                return "충전중";
            case "4":
                return "운영중지";
            case "5":
                return "점검중";
            case "6":
                return "예약중";
            case "7":
                return "시범운영";
            case "8":
                return "설치예정";
            default:
                return "알 수 없음";
        }
    }
}
