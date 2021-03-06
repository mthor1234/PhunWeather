package async.crash.com.phunweather.Networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;
import async.crash.com.phunweather.activities.Activity_Main;

/**
 * Created by mitchthornton on 8/3/18.
 *
 * SUMMARY: Used to make API calls to OpenWeatherMap.
 * Uses Volley library to make the calls asynchronously.
 *      - Volley is lifecycle aware
 *      - Utilizes caching and network calls
 *      - Cache responses are set for 24 hours to expire
 *
 *      - NOTE: Believe I found a bug in the Volley library
 *              which causing it to make multiple network calls even though it is not desired
 *
 *
 *      https://developer.android.com/training/volley/
 *
 * Uses singleDayForecast() and FiveDayForecast() to retrieve information via cache or network call
 *
 * singleDayForecast()
 *      Summary:
 *               Creates the Model_Forecast responsible for holding today's weather forecast
 *               Gives a little more information that is not found within the 5 Day Forecast API call,
 *               Such as sunrise and sunset information
 *
 *      How it Works:
 *                  Either uses a cached response or calls parseSingleDayForecast()
 *                  Parses through the network response, creating a Model_Forecast object to store the information
 *                  This is added into ArrayList<Model_Forecast> to hold a six day forecast
 *
 *
*  fiveDayForecast()
 *      Summary:
 *               Creates the ArrayList<Model_Forecast> responsible for holding the five day weather forecast
 *               Each day in the forecast is a Model_Forecast

 *
 *      How it Works:
 *                  Either uses a cached response or calls parseFiveDayForecast()
 *                  Parses through the network response, creating a Model_Forecast object to store the information
 *                  This is added into ArrayList<Model_Forecast> to hold a six day forecast
 *
*  TODO: Depending on the forecast location / api call, the five day forecast may give today's date as well
 *  This causes a potential duplicate day for today's date
 */

public class JSONParser {

// ---------- Static ---------- //
    private static final String TAG = JSONParser.class.getSimpleName();
    private static final String OPENWEATERMAP_API_KEY = "ae1d2194a7816e11b58f5e4fcc19f195";


// ---------- String ---------- //
    private String zipcode, unit_type;
    private String openWeatherMap_FiveDayForecast_URL;
    private String openWeatherMap_CurrentForecast_URL;

// ---------- Activity ---------- //
    private Activity_Main activity_main;

// ---------- Formats ---------- //
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat updateddateFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");

// ---------- ArrayList ---------- //
    private ArrayList<Model_Forecast> five_day_forecast = new ArrayList<Model_Forecast>();
    private ArrayList<Model_Forecast> forecast = new ArrayList<Model_Forecast>();


    private Model_Forecast single_day_forecast;


    public JSONParser(final Activity_Main activity_main, String zipcode, boolean unit_type) {
        this.zipcode = zipcode;
        this.activity_main = activity_main;


    // API can return metric, but as of right now, data is fetched as imperical and converted locally
    // Left this in case a metric network responses may be of use in the future
        if(unit_type == false){
            this.unit_type = "metric";
        }else{
            this.unit_type = "imperial";
        }

        // Generate URI / URL safe encodings to call the API's
        openWeatherMap_FiveDayForecast_URL = uriBuilder_fiveDayForecast();
        openWeatherMap_CurrentForecast_URL = uriBuilder_currentForecast();


// --------- CACHING CODE ------------ //

        Cache cache = AppController.getInstance().getRequestQueue().getCache();

        Cache.Entry entry_fiveday_forecast = cache.get(openWeatherMap_FiveDayForecast_URL);
        Cache.Entry entry_single_forecast = cache.get(openWeatherMap_CurrentForecast_URL);

        // There is cached data
        if (entry_single_forecast != null) {

            // Check the internet connection to alert the user that data may be old if not connected
            // Will still set data if there is cached data
            checkInternetConnection();

            try {
                String data = new String(entry_single_forecast.data, "UTF-8");
                Gson gson = new Gson();
                parseSingleDayForecast(new JSONObject(data));


            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            // Internet connection is required if there is no cached data
            if (checkInternetConnection()) {
                singleDayForecast();
            }
        }

        // There is cached data
        if (entry_fiveday_forecast != null) {

            try {
                // Check the internet connection to alert the user that data may be old if not connected
                // Will still set data if there is cached data
                checkInternetConnection();

                String data = new String(entry_fiveday_forecast.data, "UTF-8");
                parseFiveDayForecast(new JSONObject(data));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            // Internet connection is required if there is no cached data
            if (checkInternetConnection()) {
                fiveDayForecast();
            }

        }
    }



    /*
    Used to build the API URLs for the five day five_day_forecast and single day five_day_forecast
     */
    private String uriBuilder_fiveDayForecast() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast")
                .appendQueryParameter("zip", zipcode)
                .appendQueryParameter("appid", OPENWEATERMAP_API_KEY)
                .appendQueryParameter("units", unit_type);

        String myUrl = builder.build().toString();
        return myUrl;
    }

    private String uriBuilder_currentForecast() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("weather")
                .appendQueryParameter("zip", zipcode)
                .appendQueryParameter("appid", OPENWEATERMAP_API_KEY)
                .appendQueryParameter("units", unit_type);

        String myUrl = builder.build().toString();
        return myUrl;
    }

    // Do a single day five_day_forecast API call and parse JSON. Save results into ArrayList
    private void singleDayForecast() {

        // Start the process dialog to alert user of loading time
        activity_main.startProcessDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(activity_main);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                openWeatherMap_CurrentForecast_URL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                parseSingleDayForecast(response);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "JSONObj Error: " + error.getMessage());

                // If errored out, hide the progress dialog
                activity_main.hidePDialog();

                Snackbar.make(activity_main.findViewById(R.id.fragment_container), "Unable to fetch information. Try again", Snackbar.LENGTH_SHORT).show();
            }
        })

                // Caching the response
        {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    Log.d(TAG, "Caching!");

                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);

            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(jsonObjReq);
    }


    private void parseSingleDayForecast(JSONObject response){
        try {

            single_day_forecast = new Model_Forecast();

            JSONArray weather_array = response.getJSONArray("weather");
            JSONObject mainObj = response.getJSONObject("main");
            JSONObject windObj = response.getJSONObject("wind");
            JSONObject sysObj = response.getJSONObject("sys");

            Double minTemp = mainObj.getDouble("temp_min");
            Double maxTemp = mainObj.getDouble("temp_max");


            int weatherID = weather_array.getJSONObject(0).getInt("id");
            String main_weather_description = weather_array.getJSONObject(0).getString("main");

//            String previousDate = "";
//
//            CharSequence timeStampDate = formatTimeStamp(response.getLong("dt") * 1000);


            String date = updateddateFormat.format(new Date());
                if(!forecast.contains(date)){

                single_day_forecast.setCurrentTemp((int) Math.round(mainObj.getDouble("temp")));
                single_day_forecast.setCurrentTemp_Metric((int) Math.round(mainObj.getDouble("temp")));
                single_day_forecast.addMinTemp((int) Math.round(minTemp));
                single_day_forecast.addMaxTemp((int) Math.round(maxTemp));
                single_day_forecast.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                single_day_forecast.addWindSpeed((int) windObj.getDouble("speed"));
                single_day_forecast.addWeatherIconPath(getWeatherIconDrawablePath(activity_main, weatherID));
                single_day_forecast.addWeatherDescription(main_weather_description);
                single_day_forecast.setDate(date);

                single_day_forecast.addSunrise(formatTimeStamp(sysObj.getLong("sunrise")*1000));
                single_day_forecast.addSunset(formatTimeStamp(sysObj.getLong("sunset")*1000));


                single_day_forecast.calculateWeatherIcon();
                single_day_forecast.calculateAverageHumidity();
                single_day_forecast.calculateAverageWind();

            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
        forecast.add(getSingle_day_forecast());

        // Hides the process dialog, letting the user know that the background operation is over
        activity_main.hidePDialog();
    }


    // Holds all of the weeks forecasts.
    // Each Model_Forecast = a daily five_day_forecast


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

    /*
    Note: There is no sunrise / sunset information for the fiveday five_day_forecast
     */
    private void fiveDayForecast(){

        // Start the process dialog to alert user of loading time
        activity_main.startProcessDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(activity_main);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                openWeatherMap_FiveDayForecast_URL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("TAG", "JSONObj response=" + response);

                parseFiveDayForecast(response);

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "JSONObj Error: " + error.getMessage());

                // If errored out, hide the progress dialog and alert the user
                activity_main.hidePDialog();

                Snackbar.make(activity_main.findViewById(R.id.fragment_container), "Unable to fetch Fiveday information. Try again", Snackbar.LENGTH_SHORT).show();
            }
        })

        {
            // Used for caching
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
                    if (cacheEntry == null) {
                        cacheEntry = new Cache.Entry();
                    }

                    Log.d(TAG, "Caching!");

                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
                    long now = System.currentTimeMillis();
                    final long softExpire = now + cacheHitButRefreshed;
                    final long ttl = now + cacheExpired;
                    cacheEntry.data = response.data;
                    cacheEntry.softTtl = softExpire;
                    cacheEntry.ttl = ttl;
                    String headerValue;
                    headerValue = response.headers.get("Date");
                    if (headerValue != null) {
                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    headerValue = response.headers.get("Last-Modified");
                    if (headerValue != null) {
                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
                    }
                    cacheEntry.responseHeaders = response.headers;
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    return Response.success(new JSONObject(jsonString), cacheEntry);
                } catch (UnsupportedEncodingException | JSONException e) {
                    return Response.error(new ParseError(e));
                }
            }

            @Override
            protected void deliverResponse(JSONObject response) {
                super.deliverResponse(response);
            }

            @Override
            public void deliverError(VolleyError error) {
                super.deliverError(error);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjReq);

    }



        private void parseFiveDayForecast(JSONObject response){
        five_day_forecast = new ArrayList<Model_Forecast>();
        Model_Forecast tempForecast = new Model_Forecast();

        try {

            JSONArray list = response.getJSONArray("list");
            Log.d("TAG", "JSONObj list=" + list);

            String previousDate = "";


            for(int i = 0; i < list.length(); i++) {

                JSONObject mainObj = list.getJSONObject(i).getJSONObject("main");
                JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                JSONArray weatherArray = list.getJSONObject(i).getJSONArray("weather");
                String date = list.getJSONObject(i).getString("dt_txt");

                // Format date
                date = formatDate(date);


                System.out.println("i: " + i + " list length: " + list.length());

                // TODO: Sometimes get duplicate entries. Added this if statement which I believe is on the right track but getting an error for i = 7 calculateweathericon() sometimes
                // Getting duplicate volley reponses due to slow internet / error with Volley library
                if(!forecast.contains(date)){

                // New Day
                    if (!previousDate.matches(date) || i == list.length() - 1) {

                        if (i != 0) {
                            // 1. Create a Model_Forecast object (This is a weather five_day_forecast)
                            // 2. Add it to the ArrayList<Model> (This holds the all of the days forecasts)

                            tempForecast.calculateAverageTemp();
                            tempForecast.calculateWeatherIcon();
                            tempForecast.calculateAverageHumidity();
                            tempForecast.calculateAverageWind();

                            five_day_forecast.add(tempForecast);

                        }
                        tempForecast = new Model_Forecast();

                        String main_weather_description = weatherArray.getJSONObject(0).getString("main");

                        tempForecast.addMinTemp((int) Math.round(mainObj.getDouble("temp_min")));
                        tempForecast.addMaxTemp((int) Math.round(mainObj.getDouble("temp_max")));
                        tempForecast.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                        tempForecast.addWindSpeed((int) Math.round(windObj.getDouble("speed")));
                        tempForecast.addWeatherIconPath(getWeatherIconDrawablePath(activity_main, weatherArray.getJSONObject(0).getInt("id")));
                        tempForecast.setDate(date);
                        tempForecast.addWeatherDescription(main_weather_description);

                        previousDate = date;

                        int weatherID = weatherArray.getJSONObject(0).getInt("id");

                        int weatherIcon_DrawableID = getWeatherIconDrawablePath(activity_main, weatherID);

                    }

                    // Same date, add values to ArrayList
                    else {

                        String main_weather_description = weatherArray.getJSONObject(0).getString("main");

                        tempForecast.addMinTemp((int) Math.round(mainObj.getDouble("temp_min")));
                        tempForecast.addMaxTemp((int) Math.round(mainObj.getDouble("temp_max")));
                        tempForecast.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                        tempForecast.addWindSpeed((int) Math.round(windObj.getDouble("speed")));
                        tempForecast.addWeatherIconPath(getWeatherIconDrawablePath(activity_main, weatherArray.getJSONObject(0).getInt("id")));
                        tempForecast.addWeatherDescription(main_weather_description);

                    }

                }
            }

            // Add the last five_day_forecast at the end of the loop
            tempForecast.calculateAverageTemp();
            tempForecast.calculateWeatherIcon();
            tempForecast.calculateAverageHumidity();
            tempForecast.calculateAverageWind();


            // Updates adapter via Activity -> Fragment communication
            // Utilizes an interface
//            activity_main.updateFragment_DetailAdapater();
            forecast.addAll(getFive_day_forecast());
            activity_main.onDataLoaded(activity_main, getForecast());



            // Hides the process dialog, letting the user know that the background operation is over
            activity_main.hidePDialog();




        }catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

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
        else if(weatherID >= 600 && weatherID <= 699) {
            drawableId = context.getResources().getIdentifier("snow_64", "drawable", context.getPackageName());
            return drawableId;
        }

        // Group 7xx: Atmosphere
        else if(weatherID >= 700 && weatherID <= 799) {
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

    private String formatDate(String jsonDate) throws ParseException {
        Date newDate = dateFormat.parse(jsonDate);

        String date = updateddateFormat.format(newDate);
        return date;

    }


    // Formats the timestamp from UNIX UTC Timestamp to ---> "EEE MM-dd HH:mm:ss Z yyyy"
    private CharSequence formatTimeStamp(long timeStamp){
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        String relTime = new Date(timeStamp).toString();

        return relTime;
    }



    // Checks if currently connected to the internet
    //  - False: Show a snackbar alerting the user that they should connect to the internet
    public boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)activity_main.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        // If no connection
        if(!isConnected){
            Snackbar.make(activity_main.findViewById(R.id.fragment_container), "Not Connected to the Internet! Data May Not Be Up-To-Date", Snackbar.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }


// ------------ Getters -------------- //

    public ArrayList<Model_Forecast> getFive_day_forecast() {
        return five_day_forecast;
    }

    public Model_Forecast getSingle_day_forecast() {
        return single_day_forecast;
    }

    public ArrayList<Model_Forecast> getForecast() {
        return forecast;
    }


}
