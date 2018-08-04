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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import async.crash.com.phunweather.Fragments.Fragment_Detail;
import async.crash.com.phunweather.Fragments.Fragment_Zipcode;
import async.crash.com.phunweather.Models.DummyContent;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.Networking.JSONParser;
import async.crash.com.phunweather.R;


/*
1) Added JSONParser class to clean up code and create a class dedicated towards API calls / parsing results
2) Casted temperature doubles to ints, so they are more readable
3) Removed tv_dayofthe week
4) Formatted the date so it is now EEEE MMM dd, yyyy
 */

public class Activity_Main extends AppCompatActivity
        implements Fragment_Zipcode.OnListFragmentInteractionListener,
                    Fragment_Detail.OnFragmentInteractionListener{

    private static final String TAG = Activity_Main.class.getSimpleName();
    private static final String BASE_OPENWEATHERMAP_URL = "https://samples.openweathermap.org/data/2.5/forecast/daily?zip=94040&appid=b6907d289e10d714a6e88b30761fae22";
    private static final String OPENWEATHERMAP_API_KEY = "ae1d2194a7816e11b58f5e4fcc19f195";

 // By Zip
 //   https://api.openweathermap.org/data/2.5/forecast?zip=06084,us&appid=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial


//    private static final String BASE_OPENWEATHERMAP_URL2 = "https://api.openweathermap.org/data/2.5/forecast?id=524901&APPID="+OPENWEATHERMAP_API_KEY+"";


 //  http://api.openweathermap.org/data/2.5/forecast/daily?id=524901&APPID=ae1d2194a7816e11b58f5e4fcc19f195&units=imperial


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

        JSONParser jsonParser = new JSONParser();

        models = jsonParser.fiveDayForecast(this);



        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
//        pDialog.show();


        // Get JSON
//        objectRequest();



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


        Fragment fragment_detail = Fragment_Detail.newInstance(models);

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment_detail)
                .addToBackStack(null)
                .commit();


    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onListFragmentInteraction(ArrayList<Model_Forecast> item) {

    }
}