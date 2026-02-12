package com.example.campusinfoapp.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.model.News;
import com.example.campusinfoapp.data.network.NewsApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private final List<News> newsList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private SearchView searchView;

    private static final String API_KEY = "07119e79e6e740d9b528d9a3f5ce6f82";
    private static final String BASE_URL = "https://v.juhe.cn/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        recyclerView = findViewById(R.id.recyclerViewNews);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.progressBarLoading);
        searchView = findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(newsList, this); // ✅ 加上 Context
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(news -> {
            Intent intent = new Intent(this, NewsDetailActivity.class);
            intent.putExtra("title", news.getTitle());
            intent.putExtra("content", news.getContent());
            intent.putExtra("imageUrl", news.getImageUrl());
            intent.putExtra("isFavorite", news.isFavorite());
            startActivity(intent);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // 中文搜索已支持
                return true;
            }
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchNews);
        fetchNews();
    }

    private void fetchNews() {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);
        apiService.getTopHeadlines("top", API_KEY).enqueue(new Callback<NewsApiService.NewsResponse>() {
            @Override
            public void onResponse(Call<NewsApiService.NewsResponse> call, Response<NewsApiService.NewsResponse> response) {
                progressBar.setVisibility(ProgressBar.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null && response.body().errorCode == 0) {
                    newsList.clear();
                    newsList.addAll(response.body().toNewsList());
                    adapter.updateData(newsList); // 收藏状态同步
                } else {
                    Toast.makeText(NewsActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NewsApiService.NewsResponse> call, Throwable t) {
                progressBar.setVisibility(ProgressBar.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(NewsActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
