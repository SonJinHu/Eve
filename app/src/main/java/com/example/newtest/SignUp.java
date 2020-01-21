package com.example.newtest;

import android.os.Bundle;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout layout_mail;
    TextInputLayout layout_name;
    TextInputLayout layout_pw;
    TextInputLayout layout_checkPw;
    TextInputEditText et_mail;
    TextInputEditText et_name;
    TextInputEditText et_pw;
    TextInputEditText et_checkPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        initToolbar();
        initView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ba_bt_createAccount:
                checkBlank(layout_mail, et_mail, "메일 주소를 입력해주세요.");
                checkBlank(layout_name, et_name, "이름을 입력해주세요.");
                checkBlank(layout_pw, et_pw, "비밀번호를 입력해주세요.");
                checkBlank(layout_checkPw, et_checkPw, "비밀번호를 확인해주세요.");
                break;
            case R.id.ba_tv_signIn:
                finish();
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.ba_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    private void initView() {
        layout_mail = findViewById(R.id.ba_textInputLayout_mail);
        layout_name = findViewById(R.id.ba_textInputLayout_name);
        layout_pw = findViewById(R.id.ba_textInputLayout_pw);
        layout_checkPw = findViewById(R.id.ba_textInputLayout_checkPw);
        et_mail = findViewById(R.id.ba_textInputEditText_mail);
        et_name = findViewById(R.id.ba_textInputEditText_name);
        et_pw = findViewById(R.id.ba_textInputEditText_pw);
        et_checkPw = findViewById(R.id.ba_textInputEditText_checkPw);
    }

    private void checkBlank(TextInputLayout layout, EditText edit, String msg) {
        String checkValue = Objects.requireNonNull(edit.getText(), "").toString().trim();
        if (checkValue.isEmpty()) {
            layout.setError(msg);
            return;
        }
        layout.setErrorEnabled(false);
    }
}
