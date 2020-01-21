package com.example.newtest;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class SignIn extends AppCompatActivity implements View.OnClickListener {

    EditText et_mail;
    EditText et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b_bt_signIn:
                startActivity(new Intent(getApplicationContext(), B_Main.class));
                finish();
                break;
            case R.id.b_tv_signUp:
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                break;
        }
    }

    private void initView() {
        et_mail = findViewById(R.id.b_et_mail);
        et_pw = findViewById(R.id.b_et_pw);
    }

}
