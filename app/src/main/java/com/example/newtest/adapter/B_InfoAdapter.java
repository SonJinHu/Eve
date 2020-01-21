package com.example.newtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newtest.R;
import com.example.newtest.api.Charger;

import java.util.ArrayList;

public class B_InfoAdapter extends RecyclerView.Adapter<B_InfoAdapter.BottomInfoViewHolder> {

    private Context context;
    private ArrayList<Integer> arrayImg;
    private ArrayList<String> arrayText;

    public B_InfoAdapter(Context context, Charger.Info item) {
        this.context = context;

        arrayImg = new ArrayList<>();
        arrayImg.add(R.drawable.ic_business_black_24dp);
        arrayImg.add(R.drawable.ic_place_black_24dp);
        arrayImg.add(R.drawable.ic_access_alarm_black_24dp);
        arrayImg.add(R.drawable.ic_local_parking_black_24dp);

        arrayText = new ArrayList<>();
        arrayText.add(item.getChgeName());
        arrayText.add(item.getAdr());
        arrayText.add(item.getUtime());
        arrayText.add(item.getPark());
    }

    @NonNull
    @Override
    public BottomInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_info, parent, false);
        return new BottomInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomInfoViewHolder holder, final int position) {
        holder.iv.setImageResource(arrayImg.get(position));
        holder.tv.setText(arrayText.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayImg.size();
    }

    class BottomInfoViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;

        BottomInfoViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_info_iv);
            tv = itemView.findViewById(R.id.item_info_tv);
        }
    }
}
