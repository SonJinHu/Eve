package com.example.newtest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

public class Nav_Setting extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView bt_login = findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton lb = findViewById(R.id.lb_kakao);
                lb.performClick();
            }
        });
        TextView bt_logout = findViewById(R.id.bt_logout);
        bt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaoLogout();
            }
        });
        TextView bt_unlink = findViewById(R.id.bt_unlink);
        bt_unlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kakaoUnlink();
            }
        });
    }

    private void kakaoLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onSuccess(Long result) {
                super.onSuccess(result);
                // 회원 탈퇴 완료
                Toast.makeText(Nav_Setting.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleteLogout() {

            }
        });
    }

    private void kakaoUnlink() {
        new AlertDialog.Builder(Nav_Setting.this)
                .setMessage(getString(R.string.com_kakao_confirm_unlink))
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {

                                    }

                                    @Override
                                    public void onNotSignedUp() {

                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        // 회원 탈퇴 완료
                                        Toast.makeText(Nav_Setting.this, "이용해주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
