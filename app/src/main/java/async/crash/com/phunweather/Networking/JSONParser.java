package async.crash.com.phunweather.Networking;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.Snackbar;
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
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;
import async.crash.com.phunweather.activities.Activity_Main;

/**
 * Created by mitchthornton on 8/3/18.
 */

public class JSONParser {

    private static final String TAG = JSONParser.class.getSimpleName();

    private static String OPENWEATHERMAP_CURRENT_API_URL; // = "https://api.openweathermap.org/data/2.5/weather?zip=06084,us&appid=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial";
    private static String OPENWEATHERMAP_FORCEAST_API_URL;  //"https://api.openweathermap.org/data/2.5/forecast?zip="+zipcode+",us&appid="+api+"&units="+units;


    private static final String OPENWEATERMAP_API_KEY = "ae1d2194a7816e11b58f5e4fcc19f195";

    private String zipcode, unit_type;

    private String openWeatherMap_FiveDayForecast_URL;
    private String openWeatherMap_CurrentForecast_URL;

    private Activity_Main activity_main;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat updateddateFormat = new SimpleDateFormat("EEEE MMM dd, yyyy");

    public JSONParser(final Activity_Main activity_main, String zipcode, String unit_type) {
//        this.context = context;
        this.zipcode = zipcode;
        this.activity_main = activity_main;
        this.unit_type = unit_type;

        // Generate URI / URL safe encodings to call the API's
        openWeatherMap_FiveDayForecast_URL = uriBuilder_fiveDayForecast();
        openWeatherMap_CurrentForecast_URL = uriBuilder_currentForecast();
    }


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

    /*
    Note: There is no sunrise / sunset information for the fiveday forecast
     */
    public ArrayList<Model_Forecast> fiveDayForecast(){

        // Start the process dialog to alert user of loading time
        activity_main.startProcessDialog();

        final ArrayList<Model_Forecast> forecast = new ArrayList<Model_Forecast>();

        RequestQueue requestQueue = Volley.newRequestQueue(activity_main);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                openWeatherMap_FiveDayForecast_URL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", "JSONObj response=" + response);

                try {
                    JSONArray list = response.getJSONArray("list");
                    Log.d("TAG", "JSONObj list=" + list);
                    Log.d("TAG", "JSONObj length=" + list.length());


                    String previousDate = "";


                    Model_Forecast forecast_Model = new Model_Forecast();

                    for(int i = 0; i < list.length(); i++) {

                        JSONObject mainObj = list.getJSONObject(i).getJSONObject("main");
                        JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                        JSONArray weatherArray = list.getJSONObject(i).getJSONArray("weather");
                        String date = list.getJSONObject(i).getString("dt_txt");



//                        CharSequence time = formatTimeStamp(list.getJSONObject(i).getLong("dt"));

//                        Date timeStampDate = new java.util.Date(list.getJSONObject(i).getLong("dt") * 1000);
//                        CharSequence timeStampDate = formatTimeStamp(list.getJSONObject(i).getLong("dt") * 1000);


//                        CharSequence timeStampDate =  formatTimeStamp(list.getJSONObject(i).getLong("dt") * 1000);
//                        Log.d("TAG", "TimeStampDate=" + timeStampDate);
//

                        // Format date
                        date = formatDate(date);
//                        Log.d("TAG", "TimeStampDate=" + timeStampDate);




                        // New Day
                        if(!previousDate.matches(date)){

                            if(i != 0) {
                                // 1. Create a Model_Forecast object (This is a weather forecast)
                                // 2. Add it to the ArrayList<Model> (This holds the all of the days forecasts)

                                System.out.println("i: " + i + " Previous Date: " + previousDate + " date: " + date +  "Adding the previous forecast_model");

                                forecast_Model.calculateAverageTemp();
                                forecast_Model.calculateWeatherIcon();
                                forecast_Model.calculateAverageHumidity();
                                forecast_Model.calculateAverageWind();


                                activity_main.getModels().add(forecast_Model);

                            }

                            System.out.println("i: " + i + " Adding New Forecast");
                            forecast_Model = new Model_Forecast();

                            Log.d("TAG", "Wind Speed =" + windObj.getDouble("speed"));


                            String main_weather_description = weatherArray.getJSONObject(0).getString("main");

                            forecast_Model.addMinTemp((int) Math.round(mainObj.getDouble("temp_min")));
                            forecast_Model.addMaxTemp((int) Math.round(mainObj.getDouble("temp_max")));
                            forecast_Model.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                            forecast_Model.addWindSpeed((int) Math.round(windObj.getDouble("speed")));
                            forecast_Model.addWeatherIconPath(getWeatherIconDrawablePath(activity_main,weatherArray.getJSONObject(0).getInt("id")));
                            forecast_Model.setDate(date);
//                            forecast_Model.addTime(time);
                            forecast_Model.addWeatherDescription(main_weather_description);


                            previousDate = date;




                            int weatherID = weatherArray.getJSONObject(0).getInt("id");

                        int weatherIcon_DrawableID = getWeatherIconDrawablePath(activity_main, weatherID);

                    }

                    // Same date, add values to ArrayList
                        else{
                            System.out.println("i: " + i + " Adding to old Forecast " + forecast_Model.getDate());

                            String main_weather_description = weatherArray.getJSONObject(0).getString("main");


//                            Log.v(TAG, "Adding to the arraylists");
                            forecast_Model.addMinTemp((int) Math.round(mainObj.getDouble("temp_min")));
                            forecast_Model.addMaxTemp((int) Math.round(mainObj.getDouble("temp_max")));
                            forecast_Model.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                            forecast_Model.addWindSpeed((int) Math.round(windObj.getDouble("speed")));
                            forecast_Model.addWeatherIconPath(getWeatherIconDrawablePath(activity_main, weatherArray.getJSONObject(0).getInt("id")));
                            forecast_Model.addWeatherDescription(main_weather_description);

//                            forecast_Model.addTime(time);
                        }

                    }

                    // Add the last forecast at the end of the loop
                    forecast_Model.calculateAverageTemp();
                    forecast_Model.calculateWeatherIcon();
                    forecast_Model.calculateAverageHumidity();
                    forecast_Model.calculateAverageWind();


                    // Add the final forecast_Model to the activity_main models ArrayList<Forecast_Model>
//                    activity_main.getModels().add(forecast_Model);


                    // Updates adapter via Activity -> Fragment communication
                    // Utilizes an interface
                    activity_main.updateFragment_DetailAdapater();

                    // Hides the process dialog, letting the user know that the background operation is over
                    activity_main.hidePDialog();



                }catch (JSONException e) {
                    e.printStackTrace();
               } catch (ParseException e) {
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

                // If errored out, hide the progress dialog
                activity_main.hidePDialog();

                Snackbar.make(activity_main.findViewById(R.id.fragment_container), "Unable to fetch information. Try again", Snackbar.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjReq);

        return forecast;
    }

    // Do a single day forecast API call and parse JSON. Save results into ArrayList
    public Model_Forecast singleDayForecast() {

        // Start the process dialog to alert user of loading time
        activity_main.startProcessDialog();

        final Model_Forecast forecast_Model = new Model_Forecast();

        RequestQueue requestQueue = Volley.newRequestQueue(activity_main);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                openWeatherMap_CurrentForecast_URL
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.d("TAG", "JSONObj response=" + response);

                try {

                    JSONArray weather_array = response.getJSONArray("weather");
                    JSONObject mainObj = response.getJSONObject("main");
                    JSONObject windObj = response.getJSONObject("wind");
                    JSONObject sysObj = response.getJSONObject("sys");

                    Double minTemp = mainObj.getDouble("temp_min");
                    Double maxTemp = mainObj.getDouble("temp_max");

//                    Double wind = windObj.getDouble("speed");


                    int weatherID = weather_array.getJSONObject(0).getInt("id");
                    String main_weather_description = weather_array.getJSONObject(0).getString("main");


                    Log.d("TAG", "Wind Speed =" + windObj.getDouble("speed"));


                    Log.d("TAG", "weather id=" + weatherID);
//                    Log.d("TAG", "min temp=" + minTemp);
//                    Log.d("TAG", "max temp=" + maxTemp);
//                    Log.d("TAG", "Double Wind=" + wind);
//                    Log.d("TAG", "Double humidity=" + humidity);


                    String previousDate = "";
//                    int weatherDrawablePath = getWeatherIconDrawablePath(activity_main, weatherID);

//                    Log.d(TAG, "ID DRAWABLE PATH: " + weatherDrawablePath);

//                    CharSequence time = formatTimeStamp(response.getLong("dt"));


                    CharSequence timeStampDate = formatTimeStamp(response.getLong("dt") * 1000);
                    Log.d("TAG", "TimeStampDate=" + timeStampDate);



                    String date = updateddateFormat.format(new Date());

                    forecast_Model.setCurrentTemp((int) Math.round(mainObj.getDouble("temp")));
                    forecast_Model.addMinTemp((int) Math.round(minTemp));
                    forecast_Model.addMaxTemp((int) Math.round(maxTemp));
                    forecast_Model.addHumidity((int) Math.round(mainObj.getDouble("humidity")));
                    forecast_Model.addWindSpeed((int) windObj.getDouble("speed"));
                    forecast_Model.addWeatherIconPath(getWeatherIconDrawablePath(activity_main, weatherID));
                    forecast_Model.addWeatherDescription(main_weather_description);
                    forecast_Model.setDate(date);

//                    forecast_Model.addTime(time);

                    forecast_Model.addSunrise(formatTimeStamp(sysObj.getLong("sunrise")*1000));
                    forecast_Model.addSunset(formatTimeStamp(sysObj.getLong("sunset")*1000));


                    forecast_Model.calculateWeatherIcon();
                    forecast_Model.calculateAverageHumidity();
                    forecast_Model.calculateAverageWind();



                    // Add the final forecast_Model to the activity_main models ArrayList<Forecast_Model>

                    } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                    // Updates adapter via Activity -> Fragment communication
                    // Utilizes an interface
//                    activity_main.updateFragment_DetailAdapater();

                    // Hides the process dialog, letting the user know that the background operation is over
                    activity_main.hidePDialog();

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("TAG", "JSONObj Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                // If errored out, hide the progress dialog
                activity_main.hidePDialog();

                Snackbar.make(activity_main.findViewById(R.id.fragment_container), "Unable to fetch information. Try again", Snackbar.LENGTH_SHORT).show();
            }
        });



//        })
//
//            // Caching the response
//            {
//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                try {
//                    Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                    if (cacheEntry == null) {
//                        cacheEntry = new Cache.Entry();
//                    }
//
//                    Log.d(TAG, "Caching!");
//
//                    final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//                    final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//                    long now = System.currentTimeMillis();
//                    final long softExpire = now + cacheHitButRefreshed;
//                    final long ttl = now + cacheExpired;
//                    cacheEntry.data = response.data;
//                    cacheEntry.softTtl = softExpire;
//                    cacheEntry.ttl = ttl;
//                    String headerValue;
//                    headerValue = response.headers.get("Date");
//                    if (headerValue != null) {
//                        cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    headerValue = response.headers.get("Last-Modified");
//                    if (headerValue != null) {
//                        cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                    }
//                    cacheEntry.responseHeaders = response.headers;
//                    final String jsonString = new String(response.data,
//                            HttpHeaderParser.parseCharset(response.headers));
//                    return Response.success(new JSONObject(jsonString), cacheEntry);
//                } catch (UnsupportedEncodingException | JSONException e) {
//                    return Response.error(new ParseError(e));
//                }
//            }
//
//            @Override
//            protected void deliverResponse(JSONObject response) {
//                super.deliverResponse(response);
//            }
//
//            @Override
//            public void deliverError(VolleyError error) {
//                super.deliverError(error);
//            }
//
//            @Override
//            protected VolleyError parseNetworkError(VolleyError volleyError) {
//                return super.parseNetworkError(volleyError);
//            }
//        };

        requestQueue.add(jsonObjReq);

        return forecast_Model;
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

    private CharSequence formatTimeStamp(long timeStamp){
        Log.d("Server time: ", Long.toString(timeStamp));

/* log the device timezone */
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

//        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
//        timeFormat.setTimeZone(tz);

        Log.d("Time zone: ", tz.getDisplayName());

/* log the system time */
        Log.d("System time: ", String.valueOf(System.currentTimeMillis()));

//        CharSequence relTime = DateUtils.getRelativeTimeSpanString(
//                timeStamp * 1000,
//                System.currentTimeMillis(),
//                DateUtils.MINUTE_IN_MILLIS);




//        String relTime = timeFormat.format(timeStamp);

        String relTime = new Date(timeStamp).toString();

        return relTime;

//        ((TextView) view).setText(relTime);

    }
}
