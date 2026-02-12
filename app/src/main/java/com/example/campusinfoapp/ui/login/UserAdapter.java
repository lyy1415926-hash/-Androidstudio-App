package com.example.campusinfoapp.ui.login;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.db.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface OnItemLongClickListener {
        void onItemLongClick(int position, User user);
    }

    private List<User> userList;
    private OnItemLongClickListener longClickListener;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);

        // 显示账号和密码
        holder.tvUserInfo.setText(
                "账号：" + user.getUsername() +
                        "   密码：" + user.getPassword()
        );

        // 长按事件
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClick(position, user);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserInfo;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserInfo = itemView.findViewById(R.id.tvUsername);
        }
    }
}
