package com.example.newtest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newtest.common.Server;

public class A_Splash extends AppCompatActivity {

    private ProgressBar progressBar;
    private boolean isLoadCompany = false;
    private boolean isLoadStation = false;
    private boolean isLoadStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_splash);
        progressBar = findViewById(R.id.a_pb);
        progressBar.setVisibility(View.GONE);
        checkServer();
    }

    private void checkServer() {
        new Server(A_Splash.this).checkServer(new Server.OnCheckServerTaskListener() {
            @Override
            public void onPreTask() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPostTask(boolean beLoadData) {
                if (beLoadData) {
                    new Server(A_Splash.this).loadCompany(new Server.OnPostTaskListener() {
                        @Override
                        public void onPostTask() {
                            isLoadCompany = true;
                            startActivity();
                        }
                    });
                    new Server(A_Splash.this).loadAllStation(new Server.OnPostTaskListener() {
                        @Override
                        public void onPostTask() {
                            isLoadStation = true;
                            startActivity();
                        }
                    });
                } else {
                    isLoadCompany = true;
                    isLoadStation = true;
                }

                new Server(A_Splash.this).loadStatus(new Server.OnTaskListener() {
                    @Override
                    public void onPreTask() {

                    }

                    @Override
                    public void onPostTask() {
                        isLoadStatus = true;
                        startActivity();
                    }
                });
            }
        });
    }

    private void startActivity() {
        if (isLoadCompany && isLoadStation && isLoadStatus) {
            progressBar.setVisibility(View.GONE);
            startActivity(new Intent(getApplicationContext(), B_Main.class));
            finish();
        }
    }
}