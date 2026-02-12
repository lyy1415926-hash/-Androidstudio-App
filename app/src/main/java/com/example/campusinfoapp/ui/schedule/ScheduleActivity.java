package com.example.campusinfoapp.ui.schedule;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.db.AppDatabase;
import com.example.campusinfoapp.data.db.Course;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CourseAdapter adapter;
    private List<Course> courseList;
    private AppDatabase db;

    private Button btnAdd;
    private EditText etName, etTeacher, etDate, etTime;
    private androidx.appcompat.widget.SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = findViewById(R.id.recyclerViewCourses);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        etName = findViewById(R.id.etCourseName);
        etTeacher = findViewById(R.id.etCourseTeacher);
        etDate = findViewById(R.id.etCourseDate);
        etTime = findViewById(R.id.etCourseTime);
        btnAdd = findViewById(R.id.btnAddCourse);
        searchView = findViewById(R.id.searchViewCourse);

        db = AppDatabase.getInstance(this);
        courseList = db.courseDao().getAllCourses();
        if (courseList == null) courseList = new ArrayList<>();

        adapter = new CourseAdapter(courseList);
        recyclerView.setAdapter(adapter);

        // 搜索功能
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText);
                return true;
            }
        });

        // 日期选择
        etDate.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) ->
                            etDate.setText(String.format("%04d-%02d-%02d", year, month+1, dayOfMonth)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        // 时间选择
        etTime.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this,
                    (view, hourOfDay, minute) ->
                            etTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
                    .show();
        });

        // 添加课程
        btnAdd.setOnClickListener(v -> addCourse());

        // 点击条目修改
        adapter.setOnItemClickListener(this::showEditDialog);

        // 长按删除
        adapter.setOnItemLongClickListener(course -> {
            db.courseDao().deleteCourse(course);
            refreshList();
            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show();
        });
    }

    private void addCourse() {
        String name = etName.getText().toString().trim();
        String teacher = etTeacher.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        if (name.isEmpty() || teacher.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "请填写完整课程信息", Toast.LENGTH_SHORT).show();
            return;
        }

        Course course = new Course(name, teacher, date, time);
        db.courseDao().insertCourse(course);
        refreshList();
    }

    private void showEditDialog(Course course) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑课程");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        EditText etNameEdit = new EditText(this);
        etNameEdit.setText(course.getName());
        layout.addView(etNameEdit);

        EditText etTeacherEdit = new EditText(this);
        etTeacherEdit.setText(course.getTeacher());
        layout.addView(etTeacherEdit);

        EditText etDateEdit = new EditText(this);
        etDateEdit.setText(course.getDate());
        layout.addView(etDateEdit);
        etDateEdit.setFocusable(false);
        etDateEdit.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this,
                    (view, year, month, dayOfMonth) ->
                            etDateEdit.setText(String.format("%04d-%02d-%02d", year, month+1, dayOfMonth)),
                    c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))
                    .show();
        });

        EditText etTimeEdit = new EditText(this);
        etTimeEdit.setText(course.getTime());
        layout.addView(etTimeEdit);
        etTimeEdit.setFocusable(false);
        etTimeEdit.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new TimePickerDialog(this,
                    (view, hourOfDay, minute) ->
                            etTimeEdit.setText(String.format("%02d:%02d", hourOfDay, minute)),
                    c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true)
                    .show();
        });

        builder.setView(layout);

        builder.setPositiveButton("保存", (dialog, which) -> {
            course.setName(etNameEdit.getText().toString().trim());
            course.setTeacher(etTeacherEdit.getText().toString().trim());
            course.setDate(etDateEdit.getText().toString().trim());
            course.setTime(etTimeEdit.getText().toString().trim());

            db.courseDao().updateCourse(course);
            refreshList();
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void refreshList() {
        courseList.clear();
        List<Course> updatedList = db.courseDao().getAllCourses();
        if (updatedList != null) courseList.addAll(updatedList);
        adapter.notifyDataSetChanged();

        etName.setText("");
        etTeacher.setText("");
        etDate.setText("");
        etTime.setText("");
    }
}
