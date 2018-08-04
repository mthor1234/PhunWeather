package async.crash.com.phunweather.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import async.crash.com.phunweather.Fragments.DetailFragment;
import async.crash.com.phunweather.Fragments.Fragment_Zipcode;
import async.crash.com.phunweather.Models.DummyContent;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;

public class Activity_Main extends AppCompatActivity
        implements Fragment_Zipcode.OnListFragmentInteractionListener,
                    DetailFragment.OnFragmentInteractionListener{

    private static final String TAG = Activity_Main.class.getSimpleName();
    private static final String BASE_OPENWEATHERMAP_URL = "https://samples.openweathermap.org/data/2.5/forecast/daily?zip=94040&appid=b6907d289e10d714a6e88b30761fae22";
    private static final String OPENWEATHERMAP_API_KEY = "ae1d2194a7816e11b58f5e4fcc19f195";

//    private static final String BASE_OPENWEATHERMAP_URL2 = "https://api.openweathermap.org/data/2.5/forecast?id=524901&APPID="+OPENWEATHERMAP_API_KEY+"";
    private static final String BASE_OPENWEATHERMAP_URL2 = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial";


    // Volley
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private ArrayList<Model_Forecast> models;

    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        models = new ArrayList<Model_Forecast>();

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
//        pDialog.show();


        // Get JSON
        objectRequest();



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        // Create Fragment_Zipcode and add it
        Fragment fragment = Fragment_Zipcode.newInstance(1);

        FragmentManager fm = getSupportFragmentManager();

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // Implements from Fragment_Zipcode
    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {
//    public void onListFragmentInteraction(ArrayList<Model_Forecast> item) {
        Log.v(TAG, "onListFragmentInteraction");

        Bundle bundle = new Bundle();

        FragmentManager fm = getSupportFragmentManager();

//        generateDummyData();

//        Fragment detailFragment = DetailFragment.newInstance(item.get, item.content, models);
        Fragment detailFragment = DetailFragment.newInstance(models);
//        Fragment detailFragment = Fragment_Detail.newInstance(item.id, item.content);

        fm.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();


    }

    public void generateDummyData() {
//        for (int i = 0; i < 10; i++) {
//            Model_Forecast forecast = new Model_Forecast(i, i, i, i, String.valueOf(i));
//            models.add(forecast);
//        }
    }

    public void objectRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Main.this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                BASE_OPENWEATHERMAP_URL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

               /*******************************************
                        -  Example JSON RESPONSE -

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
                    {
                        "dt":1533297600,
                            "main":{
                        "temp":302.07,
                                "temp_min":302.027,
                                "temp_max":302.07,
                                "pressure":1007.72,
                                "sea_level":1026.85,
                                "grnd_level":1007.72,
                                "humidity":46,
                                "temp_kf":0.04
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
                        "speed":3.76,
                                "deg":314.501
                    },
                        "sys":{
                        "pod":"d"
                    },
                        "dt_txt":"2018-08-03 12:00:00"
                    },
                    {
                        "dt":1533308400,
                            "main":{
                        "temp":301.394,
                                "temp_min":301.394,
                                "temp_max":301.394,
                                "pressure":1006.45,
                                "sea_level":1025.5,
                                "grnd_level":1006.45,
                                "humidity":43,
                                "temp_kf":0
                    },
                        "weather":[
                        {
                            "id":801,
                                "main":"Clouds",
                                "description":"few clouds",
                                "icon":"02d"
                        }
         ],
                        "clouds":{
                        "all":12
                    },
                        "wind":{
                        "speed":4.36,
                                "deg":306.505
                    },
                        "sys":{
                        "pod":"d"
                    },
                        "dt_txt":"2018-08-03 15:00:00"
                    },

                    *************************************************************/

                Log.d("TAG", "JSONObj response=" + response);

                    try {
                        JSONArray list = response.getJSONArray("list");
                        Log.d("TAG", "JSONObj list=" + list);

                        for(int i = 0; i < list.length(); i++) {

                            JSONObject mainObj = list.getJSONObject(i).getJSONObject("main");
                            JSONObject windObj = list.getJSONObject(i).getJSONObject("wind");
                            JSONArray weatherArray = list.getJSONObject(i).getJSONArray("weather");

                            String dateTime = list.getJSONObject(i).getString("dt_txt");
                            String main_weather_description = weatherArray.getJSONObject(0).getString("main");


                            Double currentTemp = mainObj.getDouble("temp");
                            Double minTemp = mainObj.getDouble("temp_min");
                            Double maxTemp = mainObj.getDouble("temp_max");
                            Double humidity = mainObj.getDouble("humidity");
                            Double windspeed = windObj.getDouble("speed");

                            int weatherID = weatherArray.getJSONObject(0).getInt("id");

                            Log.v(TAG, "Returned Weather: " + weatherID);


                            int weatherIcon_DrawableID = getWeatherIconDrawablePath(weatherID);


                            models.add(new Model_Forecast(currentTemp, minTemp, maxTemp, humidity, windspeed, main_weather_description, dateTime, weatherIcon_DrawableID));

                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

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
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    /* OpenWeatherMAP API uses an ID to categorize it's weather.
       getWeatherIconDrawablePath assigns the drawableID to the weatherID returned by OpenWeatherMap
       EX: If OpenWeatherMAP Api returns a Weather ID of 2xx, then it will be some form of Thunderstorms
       DOCUMENTATION: https://openweathermap.org/weather-conditions
     */

    private int getWeatherIconDrawablePath(int weatherID) {

        int drawableId;

        // Group 2xx: Thunderstorm
        if (weatherID >= 200 && weatherID <= 299){
        drawableId = getResources().getIdentifier("storm_64", "drawable", getPackageName());
        return drawableId;
        }
            // Group 3xx: Drizzle
        else if(weatherID >= 300 && weatherID <= 399) {
            drawableId = getResources().getIdentifier("rain_64", "drawable", getPackageName());
            return drawableId;
        }

//             case 3: if(weatherID >= 400 && weatherID <= 499);
//                return weatherIcon_DrawablePath = "";

                // Group 5xx: Rain
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = getResources().getIdentifier("rain_64", "drawable", getPackageName());
            return drawableId;
        }

                // Group 6xx: Snow
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = getResources().getIdentifier("snow_64", "drawable", getPackageName());
            return drawableId;
        }

            // Group 7xx: Atmosphere
        else if(weatherID >= 500 && weatherID <= 599) {
            drawableId = getResources().getIdentifier("windy_weather_64", "drawable", getPackageName());
            return drawableId;
        }
            // Group 800: Clear
        else if(weatherID == 800) {
            drawableId = getResources().getIdentifier("sun_64", "drawable", getPackageName());
            return drawableId;
        }

            // Group 80x: Clouds
        else if(weatherID >= 801 && weatherID <= 809) {
            drawableId = getResources().getIdentifier("clouds_64", "drawable", getPackageName());
            return drawableId;
        }

        else{
            drawableId = getResources().getIdentifier("sun_64", "drawable", getPackageName());
            return drawableId;
        }

    }

}