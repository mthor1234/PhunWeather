package async.crash.com.phunweather.Networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import async.crash.com.phunweather.Models.Model_Forecast;

/**
 * Created by mitchthornton on 8/3/18.
 */

public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();

    private static final String OPENWEATHERMAP_BASE_API_URL = "https://api.openweathermap.org/data/2.5/forecast?zip=06084,us&appid=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial";
    private static final String OPENWEATERMAP_API = "ae1d2194a7816e11b58f5e4fcc19f195";
    private static final String BASE_OPENWEATHERMAP_URL2 = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial";


    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");



    // Holds all of the weeks forecasts.
    // Each Model_Forecast = a daily forecast
    private ArrayList<Model_Forecast> models;


    /*
    * Parses through the JSON data for the Five Day Forecast API
    * Data is given in 3 hour increments for each day.
    *
    *  -  Example JSON RESPONSE -

                 {
                 "cod":"200",
                 "message":0.0042,
                 "cnt":38,
                 "list":[
                 {
                 "dt":1533276000,
                 "main":{
                 "temp":298.81,
                 "temp_min":298.685,
                 "temp_max":298.81,
                 "pressure":1010.75,
                 "sea_level":1029.94,
                 "grnd_level":1010.75,
                 "humidity":58,
                 "temp_kf":0.13
                 },
                 "weather":[
                 {
                 "id":800,
                 "main":"Clear",
                 "description":"clear sky",
                 "icon":"01d"
                 }
                 ],
                 "clouds":{
                 "all":0
                 },
                 "wind":{
                 "speed":2.41,
                 "deg":309.506
                 },
                 "sys":{
                 "pod":"d"
                 },
                 "dt_txt":"2018-08-03 06:00:00"
                 },
                 {
                 "dt":1533286800,
                 "main":{
                 "temp":301.06,
                 "temp_min":300.976,
                 "temp_max":301.06,
                 "pressure":1009.32,
                 "sea_level":1028.56,
                 "grnd_level":1009.32,
                 "humidity":53,
                 "temp_kf":0.09
                 },
                 "weather":[
                 {
                 "id":800,
                 "main":"Clear",
                 "description":"clear sky",
                 "icon":"01d"
                 }
                 ],
                 "clouds":{
                 "all":0
                 },
                 "wind":{
                 "speed":3.47,
                 "deg":319.003
                 },
                 "sys":{
                 "pod":"d"
                 },
                 "dt_txt":"2018-08-03 09:00:00"
                 },
    */
    public ArrayList<Model_Forecast> fiveDayForecast(final Context context){


        final ArrayList<Model_Forecast> forecast = new ArrayList<Model_Forecast>();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                BASE_OPENWEATHERMAP_URL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", "JSONObj response=" + response);

                try {
                    JSONArray list = response.getJSONArray("list");
                    Log.d("TAG", "JSONObj list=" + list);
                    Log.d("TAG", "JSONObj length=" + list.length());

                    for(int i = 0; i < list.length(); i++) {

                        JSONObject mainObj = list.getJSONObject(i).getJSONObject("main");
                        JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                        JSONArray weatherArray = list.getJSONObject(i).getJSONArray("weather");

                        String date = list.getJSONObject(i).getString("dt_txt");

                        // Format date
                        //date = formatDate(date);


                        String main_weather_description = weatherArray.getJSONObject(0).getString("main");


                        int currentTemp =  (int) Math.round(mainObj.getDouble("temp"));



                        int  minTemp = (int) Math.round(mainObj.getDouble("temp_min"));
                        int  maxTemp = (int) Math.round(mainObj.getDouble("temp_max"));



                        int humidity = (int) Math.round(mainObj.getDouble("humidity"));
                        int windspeed = (int) Math.round(windObj.getDouble("speed"));

                        int weatherID = weatherArray.getJSONObject(0).getInt("id");

                        int weatherIcon_DrawableID = getWeatherIconDrawablePath(context, weatherID);

                        Log.d("TAG", "currentTemp list=" + currentTemp);
                        Log.d("TAG", "minTemp list=" + minTemp);
                        Log.d("TAG", "maxTemp list=" + maxTemp);
                        Log.d("TAG", "humidity list=" + humidity);


                        // 1. Create a Model_Forecast object (This is a weather forecast)
                        // 2. Add it to the ArrayList<Model> (This holds the all of the days forecasts)
                        forecast.add(new Model_Forecast(currentTemp, minTemp, maxTemp, humidity, windspeed, main_weather_description, date, weatherIcon_DrawableID));

                    }

                }catch (JSONException e) {
                    e.printStackTrace();
               }
               //catch (ParseException e) {
//                    e.printStackTrace();
//                }

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "JSONObj Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        requestQueue.add(jsonObjReq);

        return forecast;
    }



    /********************** Description: *****************************
       OpenWeatherMAP API uses an ID to categorize it's weather.
       getWeatherIconDrawablePath assigns the drawableID to the weatherID returned by OpenWeatherMap
       EX: If OpenWeatherMAP Api returns a Weather ID of 2xx, then it will be some form of Thunderstorms
       DOCUMENTATION: https://openweathermap.org/weather-conditions
     */
    private int getWeatherIconDrawablePath(Context context, int weatherID) {

        int drawableId;

        // Group 2xx: Thunderstorm
        if (weatherID >= 200 && weatherID <= 299){
            drawableId = context.getResources().getIdentifier("storm_64", "drawable", context.getPackageName());
            return drawableId;
        }
        // Group 3xx: Drizzle
        else if(weatherID >= 300 && weatherID <= 399) {
            drawableId = context.getResources().getIdentifier("rain_64", "drawable", context.getPackageName());
            return drawableId;
        }

//             case 3: if(weatherID >= 400 && weatherID <= 499);
//                return weatherIcon_DrawablePath = "";

        // Group 5xx: Rain
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = context.getResources().getIdentifier("rain_64", "drawable", context.getPackageName());
            return drawableId;
        }

        // Group 6xx: Snow
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = context.getResources().getIdentifier("snow_64", "drawable", context.getPackageName());
            return drawableId;
        }

        // Group 7xx: Atmosphere
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = context.getResources().getIdentifier("windy_weather_64", "drawable", context.getPackageName());
            return drawableId;
        }
        // Group 800: Clear
        else if(weatherID == 800) {
            drawableId = context.getResources().getIdentifier("sun_64", "drawable", context.getPackageName());
            return drawableId;
        }

        // Group 80x: Clouds
        else if(weatherID >= 801 && weatherID <= 809) {
            drawableId = context.getResources().getIdentifier("clouds_64", "drawable", context.getPackageName());
            return drawableId;
        }

        else{
            drawableId = context.getResources().getIdentifier("sun_64", "drawable", context.getPackageName());
            return drawableId;
        }

    }


    /*
     * Parses the JSON Date of yyyy-MM-dd hh:mm:ss to EEEE MMM dd, yyyy
     * TODO: 08-04 10:41:06.659 30370-30370/async.crash.com.phunweather W/System.err: java.text.ParseException: Unparseable date: "2018-08-05 15:00:00"
     */
    private String formatDate(String jsonDate) throws ParseException {
        Date newDate = dateFormat.parse(jsonDate);


        dateFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");

        String date = dateFormat.format(newDate);
        return date;

    }



}
