package com.example.campusinfoapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.db.AppDatabase;
import com.example.campusinfoapp.data.db.User;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnRegister;
    private RecyclerView recyclerView;

    private UserAdapter adapter;
    private List<User> userList = new ArrayList<>();

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        recyclerView = findViewById(R.id.recyclerViewUsers);

        db = AppDatabase.getInstance(this);

        // 初始化列表
        userList = db.userDao().getAllUsers();
        adapter = new UserAdapter(userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 注册
        btnRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            if (db.userDao().checkUsernameExists(username) > 0) {
                Toast.makeText(this, "用户名已存在", Toast.LENGTH_SHORT).show();
                return;
            }

            User user = new User(username, password);
            db.userDao().insertUser(user);
            Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();

            // 注册成功后跳回登录界面
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // 长按修改或删除用户
        adapter.setOnItemLongClickListener((position, user) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择操作")
                    .setItems(new String[]{"修改", "删除"}, (dialog, which) -> {
                        if (which == 0) {
                            showEditDialog(user);
                        } else {
                            db.userDao().deleteUser(user);
                            refreshUserList();
                            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }

    /**
     * 显示修改用户对话框
     */
    private void showEditDialog(User user) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("修改用户");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(60, 30, 60, 10);

        EditText inputName = new EditText(this);
        EditText inputPwd = new EditText(this);
        inputName.setHint("用户名");
        inputPwd.setHint("密码");
        inputName.setText(user.getUsername());
        inputPwd.setText(user.getPassword());

        layout.addView(inputName);
        layout.addView(inputPwd);
        dialog.setView(layout);

        dialog.setPositiveButton("保存", (d, w) -> {
            String newName = inputName.getText().toString().trim();
            String newPwd = inputPwd.getText().toString().trim();

            if (newName.isEmpty() || newPwd.isEmpty()) {
                Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
                return;
            }

            user.setUsername(newName);
            user.setPassword(newPwd);

            db.userDao().updateUser(user);
            refreshUserList();

            Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
        });

        dialog.setNegativeButton("取消", null);
        dialog.show();
    }

    /**
     * 刷新列表
     */
    private void refreshUserList() {
        userList.clear();
        userList.addAll(db.userDao().getAllUsers());
        adapter.notifyDataSetChanged();
    }
}
