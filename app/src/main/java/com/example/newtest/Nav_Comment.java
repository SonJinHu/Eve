package com.example.newtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.newtest.api.ApiService;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.api.RetroClient;
import com.example.newtest.common.Shared;
import com.example.newtest.common.TimeCalculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Nav_Comment extends AppCompatActivity {

    private final int REQUEST_ACTIVITY_COMMENT = 100;

    View scroll;
    View nothing;
    View progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_comment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        scroll = findViewById(R.id.nestedScrollView);
        nothing = findViewById(R.id.view_nothing);
        progress = findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        scroll.setVisibility(View.GONE);
        nothing.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        HashMap<String, String> map = Shared.getAccount(getApplicationContext());
        String id = map.get("id");

        ApiService api = RetroClient.getApiService();
        Call<ChargerList> call = api.getCommentMy(id);
        call.enqueue(new Callback<ChargerList>() {
            @Override
            public void onResponse(@NonNull Call<ChargerList> call, @NonNull Response<ChargerList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Charger.MyComment> comments = response.body().getMyComment();
                        if (comments.size() == 0) {
                            scroll.setVisibility(View.GONE);
                            nothing.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            return;
                        }

                        TreeMap<String, List<Charger.MyComment>> map = new TreeMap<>();
                        for (Charger.MyComment comment : comments) {
                            String key = comment.getSnm();
                            if (map.containsKey(key)) {
                                List<Charger.MyComment> items = map.get(key);
                                assert items != null;
                                items.add(comment);
                                map.put(key, items);
                            } else {
                                List<Charger.MyComment> items = new ArrayList<>();
                                items.add(comment);
                                map.put(key, items);
                            }
                        }

                        List<ListItem> items = new ArrayList<>();
                        for (String snm : map.keySet()) {
                            HeaderItem header = new HeaderItem();
                            header.setSnm(snm);

                            List<Charger.MyComment> aaa = map.get(snm);
                            assert aaa != null;
                            header.setSid(aaa.get(0).getSid());
                            items.add(header);
                            for (Charger.MyComment event : aaa) {
                                CommentItem item = new CommentItem();
                                item.setComment(event);
                                items.add(item);
                            }
                        }

                        scroll.setVisibility(View.VISIBLE);
                        nothing.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);

                        RecyclerView recycler = findViewById(R.id.recycler);
                        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycler.setAdapter(new Nav_CommentAdapter(getApplicationContext(), items));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ChargerList> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACTIVITY_COMMENT) {
            if (resultCode == RESULT_OK) {
                onResume();
                Toast.makeText(Nav_Comment.this, "댓글 수정 완료", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Nav_CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private List<ListItem> items;

        Nav_CommentAdapter(Context context, List<ListItem> items) {
            this.context = context;
            this.items = items;
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getType();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            if (viewType == ListItem.TYPE_HEADER) {
                View itemView = inflater.inflate(R.layout.item_comment_my_header, parent, false);
                return new HeaderHolder(itemView);
            } else {
                View itemView = inflater.inflate(R.layout.item_comment_my, parent, false);
                return new CommentHolder(itemView);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int position) {
            int type = getItemViewType(position);
            if (type == ListItem.TYPE_HEADER) {
                final HeaderItem header = (HeaderItem) items.get(position);
                HeaderHolder holder = (HeaderHolder) viewHolder;
                holder.tv_snm.setText(header.getSnm());
                holder.iv_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), B_Main.class);
                        intent.putExtra("sid", header.getSid());
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            } else {
                CommentHolder holder = (CommentHolder) viewHolder;
                CommentItem comment = (CommentItem) items.get(position);
                final Charger.MyComment item = comment.getComment();

                if (item.getProfileImg().equals("")) {
                    Glide.with(context)
                            .load(R.drawable.default_profile_image)
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.iv_profile);
                } else {
                    Glide.with(context)
                            .load("http://115.68.223.21" + item.getProfileImg())
                            .apply(RequestOptions.circleCropTransform())
                            .into(holder.iv_profile);
                }

                if (item.getContentImg().equals("")) {
                    holder.iv_content.setVisibility(View.GONE);
                } else {
                    holder.iv_content.setVisibility(View.VISIBLE);
                    Glide.with(context)
                            .load("http://115.68.223.21" + item.getContentImg())
                            .into(holder.iv_content);
                }

                holder.tv_nick.setText(item.getNick());
                try {
                    String time = TimeCalculator.calculate(item.getTime());
                    holder.tv_time.setText(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.tv_content.setText(item.getContent());
                holder.iv_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(Nav_Comment.this, view);
                        popupMenu.getMenuInflater().inflate(R.menu.comment, popupMenu.getMenu());
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.comment_modify:
                                        Intent intent = new Intent(getApplicationContext(), Ba_Comment.class);
                                        intent.putExtra("snm", item.getSnm());
                                        intent.putExtra("sid", item.getSid());
                                        intent.putExtra("time", item.getTime());
                                        intent.putExtra("apply", false);
                                        startActivityForResult(intent, REQUEST_ACTIVITY_COMMENT);
                                        break;
                                    case R.id.comment_delete:
                                        deleteComment(item.getId(), item.getTime());
                                        break;
                                }
                                return false;
                            }
                        });
                        popupMenu.show();
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class HeaderHolder extends RecyclerView.ViewHolder {

            TextView tv_snm;
            ImageView iv_map;

            HeaderHolder(@NonNull View itemView) {
                super(itemView);
                tv_snm = itemView.findViewById(R.id.item_bookmark_my_header_tv_snm);
                iv_map = itemView.findViewById(R.id.item_bookmark_my_header_iv_map);
            }
        }

        class CommentHolder extends RecyclerView.ViewHolder {
            ImageView iv_profile;
            ImageView iv_content;
            AppCompatImageView iv_more;
            TextView tv_nick;
            TextView tv_time;
            TextView tv_content;

            CommentHolder(@NonNull View itemView) {
                super(itemView);
                iv_profile = itemView.findViewById(R.id.item_comment_my_iv_profileImg);
                iv_content = itemView.findViewById(R.id.item_comment_my_iv_contentImg);
                iv_more = itemView.findViewById(R.id.item_comment_my_iv_more);
                tv_nick = itemView.findViewById(R.id.item_comment_my_tv_nick);
                tv_time = itemView.findViewById(R.id.item_comment_my_tv_time);
                tv_content = itemView.findViewById(R.id.item_comment_my_tv_content);
            }
        }

        private void deleteComment(String id, String time) {
            ApiService api = RetroClient.getApiService();
            Call<Charger.ServerResponse> call = api.deleteComment(id, time);
            call.enqueue(new Callback<Charger.ServerResponse>() {
                @Override
                public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getInsertResult()) {
                                onResume();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Charger.ServerResponse> call, @NonNull Throwable t) {
                    Log.e("onFailure", Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }

    public abstract class ListItem {

        public static final int TYPE_HEADER = 0;
        public static final int TYPE_EVENT = 1;

        abstract public int getType();
    }

    public class HeaderItem extends ListItem {

        private String snm;
        private String sid;

        public String getSnm() {
            return snm;
        }

        public void setSnm(String snm) {
            this.snm = snm;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    public class CommentItem extends ListItem {

        private Charger.MyComment comment;

        public Charger.MyComment getComment() {
            return comment;
        }

        public void setComment(Charger.MyComment comment) {
            this.comment = comment;
        }

        @Override
        public int getType() {
            return TYPE_EVENT;
        }
    }
}
