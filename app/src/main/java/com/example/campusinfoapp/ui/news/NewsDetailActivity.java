package com.example.campusinfoapp.ui.news;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.campusinfoapp.R;

import java.util.HashSet;
import java.util.Set;

public class NewsDetailActivity extends AppCompatActivity {

    private TextView title, content;
    private ImageView image, fav;
    private String contentUrl;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        // 安全设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        title = findViewById(R.id.tvDetailTitle);
        content = findViewById(R.id.tvDetailContent);
        image = findViewById(R.id.ivDetailImage);
        fav = findViewById(R.id.detail_favorite);

        title.setText(getIntent().getStringExtra("title"));
        contentUrl = getIntent().getStringExtra("content");
        content.setText(contentUrl);

        Glide.with(this)
                .load(getIntent().getStringExtra("imageUrl"))
                .placeholder(R.drawable.ic_placeholder)
                .into(image);

        // 初始读取收藏状态
        SharedPreferences prefs = getSharedPreferences("news_prefs", MODE_PRIVATE);
        Set<String> favSet = prefs.getStringSet("favorites", new HashSet<>());
        isFavorite = favSet.contains(contentUrl);
        updateIcon();

        // 点击收藏按钮
        fav.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            saveFavorite();
            updateIcon();

            // ✅ 显示提示
            if (isFavorite) {
                Toast.makeText(NewsDetailActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(NewsDetailActivity.this, "已取消收藏", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateIcon() {
        fav.setImageResource(isFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
    }

    private void saveFavorite() {
        SharedPreferences prefs = getSharedPreferences("news_prefs", MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("favorites", new HashSet<>());
        Set<String> newSet = new HashSet<>(set);

        if (isFavorite) newSet.add(contentUrl);
        else newSet.remove(contentUrl);

        prefs.edit().putStringSet("favorites", newSet).apply();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
