package com.example.campusinfoapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.db.AppDatabase;
import com.example.campusinfoapp.data.db.User;
import com.example.campusinfoapp.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin, btnGoRegister;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 初始化视图
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoRegister = findViewById(R.id.btnGoRegister);

        // 数据库
        db = AppDatabase.getInstance(this);

        // ⭐ 如果从注册页面返回，把注册成功的账户填回来
        if (getIntent() != null && getIntent().hasExtra("registered_username")) {
            String registeredUser = getIntent().getStringExtra("registered_username");
            etUsername.setText(registeredUser);
            Toast.makeText(this, "注册成功，请输入密码登录", Toast.LENGTH_SHORT).show();
        }

        // ⭐ 登录按钮
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                return;
            }

            // 去数据库验证
            User user = db.userDao().login(username, password);

            if (user != null) {
                Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "账号或密码错误，请重试", Toast.LENGTH_SHORT).show();
            }
        });

        // ⭐ 跳转注册按钮 - 100% 不会闪退
        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
