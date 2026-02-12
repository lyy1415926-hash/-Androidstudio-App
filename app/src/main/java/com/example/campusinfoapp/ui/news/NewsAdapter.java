package com.example.campusinfoapp.ui.news;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.model.News;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<News> newsList = new ArrayList<>();
    private final List<News> originalList = new ArrayList<>();
    private OnItemClickListener listener;

    private Context context;

    public NewsAdapter(List<News> newsList, Context context) {
        this.context = context;
        if (newsList != null) {
            this.newsList.addAll(newsList);
            this.originalList.addAll(newsList);
        }
        loadFavorites();
    }

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.title.setText(news.getTitle());
        holder.content.setText(news.getContent());

        Glide.with(holder.itemView.getContext())
                .load(news.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.image);

        int color = news.isRead() ? 0xFF888888 : 0xFF000000;
        holder.title.setTextColor(color);
        holder.content.setTextColor(color);

        holder.ivFavorite.setImageResource(news.isFavorite() ?
                R.drawable.ic_favorite : R.drawable.ic_favorite_border);

        // 点击收藏按钮
        holder.ivFavorite.setOnClickListener(v -> {
            news.toggleFavorite();
            saveFavoriteState(news, holder.itemView.getContext());
            notifyItemChanged(position);

            // ✅ 显示提示
            if (context != null) {
                if (news.isFavorite()) {
                    Toast.makeText(context, "已收藏", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            news.setRead(true);
            notifyItemChanged(position);
            if (listener != null) listener.onItemClick(news);
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void updateData(List<News> newList) {
        newsList.clear();
        originalList.clear();
        if (newList != null) {
            newsList.addAll(newList);
            originalList.addAll(newList);
        }
        loadFavorites();
        notifyDataSetChanged();
    }

    // 中文搜索支持
    public void filter(String query) {
        newsList.clear();
        if (query == null || query.trim().isEmpty()) {
            newsList.addAll(originalList);
        } else {
            query = query.trim();
            for (News news : originalList) {
                if (news.getTitle() != null && news.getTitle().contains(query)) {
                    newsList.add(news);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void saveFavoriteState(News news, Context ctx) {
        if (ctx == null) ctx = context;
        if (ctx == null) return;

        SharedPreferences prefs = ctx.getSharedPreferences("news_prefs", Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("favorites", new HashSet<>());
        Set<String> newSet = new HashSet<>(set);

        String key = news.getContent();

        if (news.isFavorite()) newSet.add(key);
        else newSet.remove(key);

        prefs.edit().putStringSet("favorites", newSet).apply();
    }

    private void loadFavorites() {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences("news_prefs", Context.MODE_PRIVATE);
        Set<String> favSet = prefs.getStringSet("favorites", new HashSet<>());
        for (News n : originalList) {
            n.setFavorite(favSet.contains(n.getContent()));
        }
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        ImageView image, ivFavorite;

        public NewsViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.news_title);
            content = itemView.findViewById(R.id.news_content);
            image = itemView.findViewById(R.id.news_image);
            ivFavorite = itemView.findViewById(R.id.iv_favorite);
        }
    }
}
