package com.example.newtest.api;

import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

public class KakaoSDKAdapter extends KakaoAdapter {
    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[]{AuthType.KAKAO_TALK_ONLY};
                // KAKAO_TALK: kakaotalk으로 login을 하고 싶을때 지정.
                // KAKAO_STORY: kakaostory으로 login을 하고 싶을때 지정.
                // KAKAO_ACCOUNT: 웹뷰 Dialog를 통해 카카오 계정연결을 제공하고 싶을경우 지정.
                // KAKAO_TALK_ONLY:	카카오톡으로만 로그인을 유도하고 싶으면서 계정이 없을때 계정생성을 위한 버튼도 같이 제공을 하고 싶다면 지정. KAKAO_TALK과 중복 지정불가.
                // KAKAO_LOGIN_ALL: 모든 로그인방식을 사용하고 싶을때 지정.
            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            @Override
            public boolean isSecureMode() {
                return false;
            }

            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {
            @Override
            public Context getApplicationContext() {
                return GlobalApplication.getGlobalApplicationContext();
            }
        };
    }
}