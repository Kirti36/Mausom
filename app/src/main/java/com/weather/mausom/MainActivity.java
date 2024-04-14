package com.weather.mausom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SearchView;

import com.weather.mausom.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.widget.TextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ActivityMainBinding getBinding() {
        if (binding == null) {
            binding = ActivityMainBinding.inflate(getLayoutInflater());
        }
        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getBinding().getRoot());
        fetchWeatherData("jaipur");
        searchCity();
    }

    private void searchCity() {
        SearchView searchView =binding.searchView;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                fetchWeatherData(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });
    }

    //a0323d01541c65d24d2f42deeb94a173
    private void fetchWeatherData(String city) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterFace retrofitAPI = retrofit.create(ApiInterFace.class);
        Call<WeatherApp> call= retrofitAPI.getWeatherData(city ,"a0323d01541c65d24d2f42deeb94a173","metric");
        call.enqueue(new Callback<WeatherApp>() {
            @Override
            public void onResponse(Call<WeatherApp> call, Response<WeatherApp> response) {

                if(response.isSuccessful() && response.body()!=null){
                    WeatherApp weatherResponse = response.body();
                    double tempera = weatherResponse.getMain().getTemp();
                    String temperature=String.valueOf(tempera);
                    int humidity = weatherResponse.getMain().getHumidity();
                    double windspeed = weatherResponse.getWind().getSpeed();
                    int sunset = weatherResponse.getSys().getSunset();
                    int sunrise = weatherResponse.getSys().getSunrise();
                    double sealevel = weatherResponse.getMain().getPressure();
                    String condition = weatherResponse.getWeather().get(0).getMain();
                    double minTemp = weatherResponse.getMain().getTemp_min();
                    double maxTemp = weatherResponse.getMain().getTemp_max();

                    binding.temperat.setText(temperature + "°C");
                    binding.conditions.setText(condition);
                    binding.Humidity.setText(humidity + "%");
                    binding.sunrise.setText((time((long) sunrise)));
                    binding.sunset.setText((time((long) sunset)));
                    binding.wind.setText(windspeed + " m/s");
                    binding.sea.setText(sealevel + " hPa");
                    binding.maxTemp.setText("Max Temp: " + maxTemp + "°C");
                    binding.minTemp.setText("Min Temp: " + minTemp + "°C");
                    binding.day.setText(date());
                    binding.date.setText(dayName(System.currentTimeMillis()));
                    binding.cityName.setText(city);
                    changeImg(condition);
                }
            }

            @Override
            public void onFailure(Call<WeatherApp> call, Throwable t) {

            }
        });

    }


    private void changeImg(String condition) {
        int drawableId;
        int animationId;
        TextView weatherTextView = findViewById(R.id.weather);

        if (condition.equalsIgnoreCase("rain")) {
            drawableId = R.drawable.rain_background;
            animationId = R.raw.rainy;
            weatherTextView.setText("Rainy");
        } else if (condition.equalsIgnoreCase("clouds")) {
            drawableId = R.drawable.colud_background;
            animationId = R.raw.cloudy;
            weatherTextView.setText("Cloudy");
        } else if (condition.equalsIgnoreCase("sunny")) {
            drawableId = R.drawable.bbsun;
            animationId = R.raw.sunny;
        } else if (condition.equalsIgnoreCase("snow")) {
            drawableId = R.drawable.snow_background;
            animationId = R.raw.snow;
            weatherTextView.setText("Snowy");
        } else {
            drawableId = R.drawable.bbsun;
            animationId = R.raw.sunny;
            weatherTextView.setText("Sunny");
        }


        binding.getRoot().setBackgroundResource(drawableId);
        binding.lottieAnimationView.setAnimation(animationId);
        binding.lottieAnimationView.playAnimation();
    }

//    private void changeImg(String condition) {
//        TextView weatherTextView = findViewById(R.id.weather);
//        switch (condition.toLowerCase()) {
//            case "light rain":
//            case "heavy rain":
//            case "moderate rain":
//            case "showers":
//            case "drizzle":
//                weatherTextView.setText("Rainy");
//                break;
//            case "clouds":
//            case "partly cloudy":
//            case "mist":
//            case "foggy":
//            case "overcast":
//                weatherTextView.setText("Cloudy");
//                break;
//            case "sunny":
//            case "clear sky":
//            case "clear":
//                weatherTextView.setText("Sunny");
//                break;
//            case "light snow":
//            case "moderate snow":
//            case "heavy snow":
//            case "blizzard":
//                weatherTextView.setText("Snowy");
//                break;
//            default:
//                weatherTextView.setText("Weather Unavailable");
//                break;
//        }
//    }






    private String date() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String time(Long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp*1000));
    }

    public String dayName(Long timestamp){
        if (timestamp == null || timestamp == 0) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        //Date date = new Date(timestamp);
        return sdf.format(new Date());
    }
}