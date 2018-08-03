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
    private static final String BASE_OPENWEATHERMAP_URL2 = "http://api.openweathermap.org/data/2.5/forecast?id=524901&APPID=ae1d2194a7816e11b58f5e4fcc19f195";

//            "api.openweathermap.org/data/2.5/forecast/daily?zip={";

    // Volley
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;

    private ArrayList<Model_Forecast> models;

    private ProgressDialog pDialog;




    //{zip code},{country code}\n";

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

//        jsonRequest();
//        sendAndRequestResponse();

        Bundle bundle = new Bundle();

        FragmentManager fm = getSupportFragmentManager();

        generateDummyData();

//        Fragment detailFragment = DetailFragment.newInstance(item.get, item.content, models);
        Fragment detailFragment = DetailFragment.newInstance(models);
//        Fragment detailFragment = Fragment_Detail.newInstance(item.id, item.content);

        fm.beginTransaction()
                .replace(R.id.fragment_container, detailFragment)
                .addToBackStack(null)
                .commit();


    }


    private void jsonRequest(){

        System.out.println("Starting Response");
//
//        System.out.println("API Call " + BASE_OPENWEATHERMAP_URL2);
//
//
////        request = new JsonArrayRequest(BASE_OPENWEATHERMAP_URL2, new Response.Listener<JSONArray>() {
//        request = new JsonArrayRequest(BASE_OPENWEATHERMAP_URL2, new Response.Listener<JSONArray>() {
//
//
//            @Override
//            public void onResponse(JSONArray response) {
//
//                JSONObject jsonObject = null;
//
//                System.out.println("API Call" + BASE_OPENWEATHERMAP_URL2);
//
//                for(int i = 0; i < response.length(); i++){
//
//                    try{
//                        System.out.println("We in here!");
//
//                        jsonObject = response.getJSONObject(i);
//                        Model_Forecast forecast = new Model_Forecast();
//                        Model_Address address = new Model_Address();
//
////                        JSONObject JSONObject_City = jsonObject.getJSONObject("city");
//
//                        JSONObject JSONArray_list = jsonObject.getJSONObject("list");
//
//
////                        address.setCity(JSONObject_City.getString("name"));
//
//                        forecast.setMinTemp(JSONArray_list.getDouble("min"));
//
//
////                        address.setCity(jsonObject.getString("name");
//
//
//
//                        System.out.println("Min Temp: " +  JSONArray_list.getDouble("min"));
//

//
//                    }
//                    catch (JSONException e){
//                        e.printStackTrace();
//                    }
//                }
//
//            }
//        } , new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error == null || error.networkResponse == null) {
//                    return;
//                }
//
//                String body;
//                //get status code here
//                final String statusCode = String.valueOf(error.networkResponse.statusCode);
//                //get response body and parse with appropriate encoding
//                System.out.println("Status Code: " + statusCode);
//                try {
//                    body = new String(error.networkResponse.data, "UTF-8");
//                    System.out.println("Body Print: " + body);
//
//                } catch (UnsupportedEncodingException e) {
//                    // exception
//
//                }
//
//                System.out.println("Status Code: " + statusCode);
//
//                //do stuff with the body...            }
//            }
//        });
//
//        requestQueue = Volley.newRequestQueue(Activity_Main.this);
//        requestQueue.add(request);


        // Creating volley request obj
//        JsonArrayRequest request = new JsonArrayRequest(BASE_OPENWEATHERMAP_URL2,

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, BASE_OPENWEATHERMAP_URL2, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();
                        System.out.println("On Response: " + response.toString());


                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject("city");
                                System.out.println("City is: " + obj.get("city"));

//                                Model model = new Model();
//                                model.setTitle(obj.getString("title"));
//                                model.setCategory(obj.getString("category"));
//                                model.setThumbnailUrl(obj.getString("image"));

//                                modelList.add(model);

                            } catch (JSONException e) {
                                System.out.println("ERROR!: " + e.getMessage());

                                e.printStackTrace();
                            }

                        }
                        // notifying list adapter about data changes so that it renders the list view with updated data
//                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                System.out.println("In Here Error!: " + error.getMessage());

                hidePDialog();

            }
        });

    }

    public void generateDummyData() {
        for (int i = 0; i < 10; i++) {
            Model_Forecast forecast = new Model_Forecast(i, i, i, i, String.valueOf(i));
            models.add(forecast);
        }
    }

    public void objectRequest() {

        RequestQueue requestQueue = Volley.newRequestQueue(Activity_Main.this);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                BASE_OPENWEATHERMAP_URL2, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                //                        /*************** Example JSON Response **********************
//                                {"cod":"200","message":0.0032,
//                                    "city":{"id":1851632,"name":"Shuzenji",
//                                    "coord":{"lon":138.933334,"lat":34.966671},
//                                    "country":"JP"},
//                                    "cnt":10,
//                                    "list":[{
//                                        "dt":1406080800,
//                                        "temp":{
//                                            "day":297.77,
//                                            "min":293.52,
//                                            "max":297.77,
//                                            "night":293.52,
//                                            "eve":297.77,
//                                            "morn":297.77},
//                                        "pressure":925.04,
//                                        "humidity":76,
//                                        "weather":[{"id":803,"main":"Clouds","description":"broken clouds","icon":"04d"}],}
//                                            ]}
//
//                         *******************************************************************/
//

                Log.d("TAG", "JSONObj response=" + response);

                    try {
                        JSONArray list = response.getJSONArray("list");
                        Log.d("TAG", "JSONObj list=" + list);

                        JSONObject mainObj = list.getJSONObject(0).getJSONObject("main");
                        Log.d("TAG", "JSONObj temp=" + mainObj);


                        Double currentTemp = mainObj.getDouble("temp");
                        Double minTemp = mainObj.getDouble("temp_min");
                        Double maxTemp = mainObj.getDouble("temp_max");

                        Log.d("TAG", "currentTemp=" + Double.toString(currentTemp));
                        Log.d("TAG", "Min temp=" + Double.toString(minTemp));
                        Log.d("TAG", "Max temp=" + Double.toString(maxTemp));



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

}