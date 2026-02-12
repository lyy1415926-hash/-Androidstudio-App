package com.example.campusinfoapp.ui.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campusinfoapp.R;
import com.example.campusinfoapp.data.model.WeatherResponse;
import com.example.campusinfoapp.data.network.WeatherApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends AppCompatActivity {

    private EditText editCity;
    private Button btnSearch;
    private TextView textResult;

    private final String API_KEY = "c28b4f63a20074ab0da434434754b21a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        editCity = findViewById(R.id.editCity);
        btnSearch = findViewById(R.id.btnSearch);
        textResult = findViewById(R.id.textResult);

        btnSearch.setOnClickListener(v -> fetchWeather());
    }

    private void fetchWeather() {
        String city = editCity.getText().toString().trim();

        if (city.isEmpty()) {
            Toast.makeText(this, "请输入城市名", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://apis.juhe.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherApiService api = retrofit.create(WeatherApiService.class);

        api.getWeather(city, API_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse body = response.body();

                    if (body.getError_code() == 0 && body.getResult() != null) {
                        WeatherResponse.Result result = body.getResult();
                        textResult.setText("城市：" + result.getCity()
                                + "\n天气：" + result.getRealtime().getInfo()
                                + "\n温度：" + result.getRealtime().getTemperature() + "°C");
                    } else {
                        textResult.setText("查询失败: " + body.getReason());
                    }

                } else {
                    textResult.setText("请求失败");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                textResult.setText("请求错误：" + t.getMessage());
            }
        });
    }
}
