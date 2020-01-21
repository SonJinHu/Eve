package com.example.newtest;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.newtest.api.ApiService;
import com.example.newtest.api.Charger;
import com.example.newtest.api.ChargerList;
import com.example.newtest.api.RetroClient;
import com.example.newtest.common.PickImage;
import com.example.newtest.common.Server;
import com.example.newtest.common.Shared;
import com.example.newtest.common.TimeCalculator;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Ba_Comment extends PickImage implements PickImage.CroppedListener {

    private String userId;
    private String extraSnm;
    private String extraSid;
    private String extraTime;
    private boolean extraApply;

    private Button bt_apply;
    private EditText et_content;
    private ImageView iv_selectImg;
    private View cv_imgFrame;
    private View bt_addPhoto;

    private String loadedComment;
    private boolean loadedIsImg;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ba_comment);
        userId = Shared.getAccount(getApplicationContext()).get("id");
        extraSnm = getIntent().getStringExtra("snm");
        extraSid = getIntent().getStringExtra("sid");
        extraTime = getIntent().getStringExtra("time");
        extraApply = getIntent().getBooleanExtra("apply", true);

        initToolbar();
        initView();

        if (extraApply) {
            bt_apply.setText("게시");
            bt_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentApply();
                }
            });
            cv_imgFrame.setVisibility(View.GONE);
        } else {
            bt_apply.setText("수정");
            bt_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    commentUpdate();
                }
            });
            commentLoad();
        }
    }

    private void initToolbar() {
        // 왼쪽: 닫기
        Toolbar toolbar = findViewById(R.id.ba_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishEvent();
            }
        });
        // 중간: 제목
        TextView tv_title = findViewById(R.id.ba_tv_title);
        tv_title.setText(extraSnm);
        // 오른쪽 끝 버튼
        bt_apply = findViewById(R.id.ba_bt_apply);
        bt_apply.setEnabled(false);
    }

    private void initView() {
        et_content = findViewById(R.id.ba_et_content);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkButtonEnable();
            }
        });

        cv_imgFrame = findViewById(R.id.ba_cv);
        iv_selectImg = findViewById(R.id.ba_aiv_selectImg);

        View bt_clearImg = findViewById(R.id.ba_aiv_clearSelectImg);
        bt_clearImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideEffectSelectImage();
            }
        });

        bt_addPhoto = findViewById(R.id.ba_view_addPhoto);
        bt_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogHowToPickImage(Ba_Comment.this);
            }
        });
    }

    @Override
    public void onAfterCrop(File file, Bitmap bitmap) {
        this.file = file;
        Glide.with(getApplicationContext())
                .load(bitmap)
                .into(iv_selectImg);
        showEffectSelectImage();
    }

    private void showEffectSelectImage() {
        bt_addPhoto.animate().alpha(0).setDuration(100).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bt_addPhoto.setVisibility(View.GONE);
                cv_imgFrame.animate().alpha(1).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        cv_imgFrame.setVisibility(View.VISIBLE);
                        checkButtonEnable();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void hideEffectSelectImage() {
        cv_imgFrame.animate().alpha(0).setDuration(100).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cv_imgFrame.setVisibility(View.GONE);
                checkButtonEnable();
                bt_addPhoto.animate().alpha(1).setDuration(100).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        bt_addPhoto.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    private void commentLoad() {
        new Server(this).loadComment(userId, extraSid, extraTime, new Server.OnCommentTaskListener() {
            @Override
            public void onPreTask() {

            }

            @Override
            public void onPostTask(Response<ChargerList> response) {
                assert response.body() != null;
                final ArrayList<Charger.Comment> comments = response.body().getComment();
                if (comments.get(0).getContentImg().equals("")) {
                    loadedIsImg = false;
                    cv_imgFrame.setVisibility(View.GONE);
                    bt_addPhoto.setVisibility(View.VISIBLE);
                } else {
                    Glide.with(Ba_Comment.this)
                            .load("http://115.68.223.21" + comments.get(0).getContentImg())
                            .into(iv_selectImg);
                    loadedIsImg = true;
                    cv_imgFrame.setVisibility(View.VISIBLE);
                    bt_addPhoto.setVisibility(View.GONE);
                }

                loadedComment = comments.get(0).getContent();
                et_content.setText(loadedComment);
            }
        });
    }

    private void commentApply() {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody sid = RequestBody.create(MediaType.parse("text/plain"), extraSid);
        RequestBody content = RequestBody.create(MediaType.parse("text/plain"), et_content.getText().toString());
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), TimeCalculator.getCurrentTime());

        ApiService api = RetroClient.getApiService();
        Call<Charger.ServerResponse> call;

        if (file == null || cv_imgFrame.getVisibility() != View.VISIBLE) {
            call = api.postComment(id, sid, content, time);
        } else {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), body);
            call = api.postComment(id, sid, content, time, part);
        }

        call.enqueue(new Callback<Charger.ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getInsertResult() && response.body().getUploadResult()) {
                            Intent intent = new Intent(getApplicationContext(), B_Main.class);
                            setResult(RESULT_OK, intent);
                            finish();
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

    private void commentUpdate() {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), userId);
        RequestBody sid = RequestBody.create(MediaType.parse("text/plain"), extraSid);
        RequestBody content = RequestBody.create(MediaType.parse("text/plain"), et_content.getText().toString());
        RequestBody time = RequestBody.create(MediaType.parse("text/plain"), extraTime);

        ApiService api = RetroClient.getApiService();
        Call<Charger.ServerResponse> call;

        boolean isImage = cv_imgFrame.getVisibility() == View.VISIBLE;
        if (isImage && file != null) {
            RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), body);
            call = api.updateComment(id, sid, content, time, part);
        } else {
            if(loadedIsImg && !isImage) {
                call = api.updateComment(id, sid, content, time, true);
            } else {
                call = api.updateComment(id, sid, content, time, false);
            }
        }

        call.enqueue(new Callback<Charger.ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<Charger.ServerResponse> call, @NonNull Response<Charger.ServerResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getUploadResult() && response.body().getInsertResult()) {
                            Intent intent = new Intent(getApplicationContext(), B_Main.class);
                            setResult(RESULT_OK, intent);
                            finish();
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

    private void checkButtonEnable() {
        String editText = et_content.getText().toString();
        boolean isImage = cv_imgFrame.getVisibility() == View.VISIBLE;

        if (extraApply) {
            if (editText.length() != 0)
                bt_apply.setEnabled(true);
            else
                bt_apply.setEnabled(false);
        } else {
            if (loadedIsImg) {
                // 이미지 있는 댓글
                if (editText.length() != 0 && (!editText.equals(loadedComment) || !isImage || file != null))
                    bt_apply.setEnabled(true);
                else
                    bt_apply.setEnabled(false);
            } else {
                // 이미지 없는 댓글
                if (editText.length() != 0 && (!editText.equals(loadedComment) || isImage))
                    bt_apply.setEnabled(true);
                else
                    bt_apply.setEnabled(false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finishEvent();
    }

    private void finishEvent() {
        if (bt_apply.isEnabled()) {
            String message;
            String buttonText;
            if (extraApply) {
                message = "댓글 작성을 취소하시겠습니까?";
                buttonText = "계속 작성";
            } else {
                message = "댓글 수정을 취소하시겠습니까?";
                buttonText = "계속 수정";
            }

            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setNegativeButton(buttonText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
            return;
        }
        finish();
    }
}