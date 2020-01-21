package com.example.newtest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newtest.R;
import com.example.newtest.api.Charger;
import com.example.newtest.common.TimeCalculator;

import java.text.ParseException;
import java.util.ArrayList;

public class B_CommentAdapter extends RecyclerView.Adapter<B_CommentAdapter.BottomChargerViewHolder> {

    private Context context;
    private ArrayList<Charger.Comment> items;

    public B_CommentAdapter(Context context, ArrayList<Charger.Comment> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public BottomChargerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.item_comment, parent, false);
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        return new BottomChargerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BottomChargerViewHolder holder, final int position) {
        if (items.get(position).getProfileImg().equals("")) {
            Glide.with(context)
                    .load(R.drawable.default_profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_profile);
        } else {
            Glide.with(context)
                    .load("http://115.68.223.21" + items.get(position).getProfileImg())
                    .thumbnail(R.drawable.default_profile_image)
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.iv_profile);
        }

        holder.tv_nick.setText(items.get(position).getNick());

        try {
            String time = TimeCalculator.calculate(items.get(position).getTime());
            holder.tv_time.setText(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (items.get(position).getContentImg().equals("")) {
            holder.iv_content.setVisibility(View.GONE);
        } else {
            holder.iv_content.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load("http://115.68.223.21" + items.get(position).getContentImg())
                    .into(holder.iv_content);
        }

        holder.tv_comment.setText(items.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BottomChargerViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        ImageView iv_content;
        TextView tv_nick;
        TextView tv_time;
        TextView tv_comment;

        BottomChargerViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.item_comment_iv);
            iv_content = itemView.findViewById(R.id.item_comment_iv_content);
            tv_nick = itemView.findViewById(R.id.item_comment_tv_nick);
            tv_time = itemView.findViewById(R.id.item_comment_tv_time);
            tv_comment = itemView.findViewById(R.id.item_comment_tv);
        }
    }
}
