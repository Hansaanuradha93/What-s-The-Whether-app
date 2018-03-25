package com.example.hansaanuradhawickramanayake.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText cityEditText;
    TextView weatherInfoTextView;

    String encodedCityName;

    public void getWeather(View view){

        try {
        DownloadTask task = new DownloadTask();

        encodedCityName = URLEncoder.encode(cityEditText.getText().toString(), "UTF-8");

        task.execute("http://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityEditText.getWindowToken(), 0);

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(MainActivity.this, "Could not find weather : (", Toast.LENGTH_SHORT).show();

        }

    }

    public class DownloadTask extends AsyncTask<String, Void, String>{



        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                char current;

                while (data != -1){

                    current = (char) data;

                    result += current;

                    data = reader.read();
                }
                return result;

            } catch (Exception e) {


                e.printStackTrace();

                Toast.makeText(MainActivity.this, "Could not find weather : (", Toast.LENGTH_SHORT).show();

                return null;

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                //String mainInfo = jsonObject.getString("main");

                JSONArray jsonWeatherInfoArray = new JSONArray(weatherInfo);
                //JSONArray jsonMainInfoArray = new JSONArray(mainInfo);

                String message = "";
                String main = "";
                String description = "";



                for (int i = 0; i < jsonWeatherInfoArray.length(); i++){

                    JSONObject jsonPart = jsonWeatherInfoArray.getJSONObject(i);

                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals("")){

                        message += main + " : " + description + "\r\n";

                    }
                }

                if (!message.equals("")){

                    weatherInfoTextView.setText(message);

                } else {

                    Toast.makeText(MainActivity.this, "Could not find weather : (", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {

                e.printStackTrace();

                Toast.makeText(MainActivity.this, "Could not find weather : (", Toast.LENGTH_SHORT).show();

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        weatherInfoTextView = findViewById(R.id.weatherInfoTextView);



    }
}
