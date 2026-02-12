package com.example.campusinfoapp.data.network;

import com.example.campusinfoapp.data.model.News;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {

    @GET("toutiao/index")
    Call<NewsResponse> getTopHeadlines(
            @Query("type") String type,
            @Query("key") String apiKey
    );

    class NewsResponse {
        @SerializedName("error_code")
        public int errorCode;

        @SerializedName("reason")
        public String reason;

        @SerializedName("result")
        public Result result;

        public List<News> toNewsList() {
            List<News> list = new ArrayList<>();
            if (result != null && result.data != null) {
                for (Article a : result.data) {
                    list.add(new News(
                            a.title != null ? a.title : "无标题",
                            a.url != null ? a.url : "暂无内容",
                            a.thumbnail_pic_s != null ? a.thumbnail_pic_s : ""
                    ));
                }
            }
            return list;
        }

        public static class Result {
            @SerializedName("data")
            public List<Article> data;
        }

        public static class Article {
            @SerializedName("title")
            public String title;

            @SerializedName("date")
            public String date;

            @SerializedName("author_name")
            public String authorName;

            @SerializedName("url")
            public String url;

            @SerializedName("thumbnail_pic_s")
            public String thumbnail_pic_s;
        }
    }
}
