package com.example.campusinfoapp.ui.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.example.campusinfoapp.R;
import com.example.campusinfoapp.ui.login.LoginActivity;
import com.example.campusinfoapp.ui.news.NewsActivity;
import com.example.campusinfoapp.ui.schedule.ScheduleActivity;
import com.example.campusinfoapp.ui.weather.WeatherActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin, btnNews, btnSchedule, btnWeather;
    private Switch switchNightMode;
    private View topBar;
    private TextView topBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLoginMain);
        btnNews = findViewById(R.id.btnNewsMain);
        btnSchedule = findViewById(R.id.btnScheduleMain);
        btnWeather = findViewById(R.id.btnWeatherMain);
        switchNightMode = findViewById(R.id.switchNightMode);

        topBar = findViewById(R.id.topBar);

        // 在 topBar 上添加标题文字
        topBarTitle = new TextView(this);
        topBarTitle.setText("校园信息助手");
        topBarTitle.setTextSize(18);
        topBarTitle.setTextColor(Color.WHITE);
        topBarTitle.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        topBarTitle.setPadding(16,0,0,0);

        if (topBar instanceof ConstraintLayout) {
            ((ConstraintLayout) topBar).addView(topBarTitle,
                    new ConstraintLayout.LayoutParams(
                            ConstraintLayout.LayoutParams.MATCH_PARENT,
                            ConstraintLayout.LayoutParams.MATCH_PARENT));
        }

        // 初始化开关状态
        int currentMode = AppCompatDelegate.getDefaultNightMode();
        switchNightMode.setChecked(currentMode == AppCompatDelegate.MODE_NIGHT_YES);
        updateTopBarColor(currentMode == AppCompatDelegate.MODE_NIGHT_YES);

        // 切换夜间/白天模式
        switchNightMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            updateTopBarColor(isChecked);
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        btnLogin.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        btnNews.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NewsActivity.class)));
        btnSchedule.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ScheduleActivity.class)));
        btnWeather.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, WeatherActivity.class)));
    }

    private void updateTopBarColor(boolean night) {
        topBar.setBackgroundColor(night ? Color.BLACK : getResources().getColor(R.color.purple_200));
    }
}
