package com.example.campusinfoapp.ui.schedule;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.db.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courseList;   // 当前显示列表
    private List<Course> fullList;     // 原始完整列表，用于搜索

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    // 点击接口
    public interface OnItemClickListener { void onItemClick(Course course); }
    public void setOnItemClickListener(OnItemClickListener listener) { this.clickListener = listener; }

    // 长按接口
    public interface OnItemLongClickListener { void onItemLongClick(Course course); }
    public void setOnItemLongClickListener(OnItemLongClickListener listener) { this.longClickListener = listener; }

    public CourseAdapter(List<Course> courseList) {
        this.courseList = courseList;
        this.fullList = new ArrayList<>(courseList); // 复制一份完整列表
    }

    // 更新整个数据集（刷新用，同时更新 fullList）
    public void updateList(List<Course> newList) {
        this.courseList.clear();
        this.courseList.addAll(newList);

        this.fullList.clear();
        this.fullList.addAll(newList);

        notifyDataSetChanged();
    }

    // 搜索过滤
    public void filter(String text) {
        courseList.clear();
        if (text == null || text.isEmpty()) {
            courseList.addAll(fullList);
        } else {
            text = text.toLowerCase();
            for (Course c : fullList) {
                if (c.getName().toLowerCase().contains(text) ||
                        c.getTeacher().toLowerCase().contains(text) ||
                        c.getDate().toLowerCase().contains(text) ||
                        c.getTime().toLowerCase().contains(text)) {
                    courseList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.tvName.setText(course.getName());
        holder.tvTeacher.setText(course.getTeacher());
        holder.tvDate.setText(course.getDate());
        holder.tvTime.setText(course.getTime());

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(course);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) longClickListener.onItemLongClick(course);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    static class CourseViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTeacher, tvDate, tvTime;

        public CourseViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCourseName);
            tvTeacher = itemView.findViewById(R.id.tvCourseTeacher);
            tvDate = itemView.findViewById(R.id.tvCourseDate);
            tvTime = itemView.findViewById(R.id.tvCourseTime);
        }
    }
}
