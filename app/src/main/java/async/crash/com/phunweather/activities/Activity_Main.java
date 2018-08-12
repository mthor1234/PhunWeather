package async.crash.com.phunweather.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Detail_Item;
import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Zipcode;
import async.crash.com.phunweather.Fragments.Fragment_Detail;
import async.crash.com.phunweather.Fragments.Fragment_Zipcode;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_With_Adapter;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.Models.Model_Zipcode;
import async.crash.com.phunweather.Networking.JSONParser;
import async.crash.com.phunweather.R;


/*
Summary: Added a switch within the toolbar to handle if the user would like units displayed in Celsius or Fahrenheit

    1) Added switch to toolbar.xml

    2) Activity_Main
        - Set view
        - Created boolean to hold weather checked or not
        - OnCheckedChangeListener
        - JSONParser now takes a boolean which equates to:
            * true: imperial
            * false: metric
    3)
 */

public class Activity_Main extends AppCompatActivity
        implements Fragment_Zipcode.OnListFragmentInteractionListener,
                    Fragment_Detail.OnFragmentInteractionListener{

// ------ Static ------ //

    private static final String TAG = Activity_Main.class.getSimpleName();
    private static final String BASE_OPENWEATHERMAP_URL = "https://samples.openweathermap.org/data/2.5/forecast/daily?zip=94040&appid=b6907d289e10d714a6e88b30761fae22";
    private static final String OPENWEATHERMAP_API_KEY = "ae1d2194a7816e11b58f5e4fcc19f195";

    public static final int FRAGMENT_ZIPCODE = 0;
    public static final int FRAGMENT_DETAIL = 1;


// ------ Volley ------ //
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


// ------ Fragments ------ //
    private Fragment fragment_detail;
    private Fragment fragment_zip;
    private Fragment mContent; // Keeps Instance State


// ------ Interfaces ------ //
    // Used to update adapters within those fragments
    private Interface_Communicate_With_Adapter listener_Detail_Fragment;
    private Interface_Communicate_With_Adapter listener_Zipcode_Fragment;


// ------ Views ------ //
    private Switch switch_units;
    private EditText et_enterZip;


// ------ Adapters ------ //
    // Creating fragment_detail_adapter for Detail
    private Adapter_RecyclerView_Detail_Item fragment_detail_adapter;
    private Adapter_RecyclerView_Zipcode fragment_zipcode_adapter;

    private ProgressDialog pDialog;


// ------ ArrayLists ------ //
    private ArrayList<Model_Forecast> models = new ArrayList<Model_Forecast>();
    private ArrayList<Model_Zipcode> al_zipCodes;


// ------ int ------ //
    private int currentFragment;


// ------ Strings ------ //
    private String unit_type = "imperial";  // Used if user wants imperial or metric readings
    private String selected_zipCode;

// ------ boolean ------ //

    /* Holds what unit type the user wants:
         false: "metric"
         true: "imperial"
     imperial by default   */

    private boolean unitType = true;


    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager fm = getSupportFragmentManager();

        //--------- Setting Views ---------- //

        // ToolBar & removing toolbar title
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        et_enterZip = (EditText) findViewById(R.id.et_zipcode);
        et_enterZip.setHint("Enter Zip Code");
        et_enterZip.setEnabled(true);

        //--------- Click Listeners ---------- //

        // When submit button of the IME is clicked
        // 1) If the EditText is five digits in length
        //      True: Add it to the arraylist -> recyclerview / adapter
        //      False: Show a Snackbar that the entry needs to be 5 digits in length

        et_enterZip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    System.out.println("DONE BUTTON CLICKED");

                    addZipCode();

                    handled = true;
                }
                return handled;
            }
        });


        //other stuff
        if (savedInstanceState == null) {
            System.out.println("Saved Instance State = null");
            currentFragment = FRAGMENT_ZIPCODE;

//            al_zipCodes = new ArrayList<Model_Zipcode>();

            loadData();


            // Create the Zipcode Fragment & Fragment Manager
            fragment_zip = Fragment_Zipcode.newInstance(al_zipCodes);

            //--------- End of Fragment ---------- //


            // Replace the Fragment
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment_zip)
                    .commit();



        } else {
            System.out.println("Saved Instance State != null");
            currentFragment = savedInstanceState.getInt("currentFragment");
            System.out.println(currentFragment);

            fragment_zip = fm.getFragment(savedInstanceState, "Fragment_Zipcode");
            loadData();


            //--------- Fragment ---------- //

            System.out.println("CASE: Fragment Detail!!");


            if(currentFragment == FRAGMENT_DETAIL){

//                        fragment_detail = fm.getFragment(savedInstanceState, "Fragment_Detail");


                // Using parsing cache to update the fragment_detail information
//                JSONParser parser = new JSONParser(this, selected_zipCode, "imperial");
                JSONParser parser = new JSONParser(this, selected_zipCode, unitType);
                models.add(parser.getSingle_day_forecast());
                models.addAll(parser.getFive_day_forecast());

                fragment_detail = new Fragment_Detail().newInstance(models);

                fm.popBackStack();

                        fm.beginTransaction()
                                .replace(R.id.fragment_container,  fragment_detail)
                                .addToBackStack("Detail Fragment")
                                .commit();

                        // Used setting the correct Fragment on screen rotation
//                        loadData();


                        // Called to properly handle the Edit_Text
                        et_enterZip.setHint(selected_zipCode + " Forecast");
                        et_enterZip.setEnabled(false);

                }
                else{

                fm.popBackStack();

                fm.beginTransaction()
                        .replace(R.id.fragment_container,  fragment_zip)
                        .commit();

                // Called to properly handle the Edit_Text
                et_enterZip.setHint("Enter Zip Code");
                et_enterZip.setEnabled(true);


            }
        }
        models = new ArrayList<Model_Forecast>();


        //--------- Switch ---------- //
        switch_units = (Switch) findViewById(R.id.switch_units);
        switch_units.setTextOff("C");
        switch_units.setTextOn("F");
        switch_units.setChecked(true);


    //--------- End Setting Views ---------- //

        // Setting interface to allow Activity_Main to call fragment_zip.updateAdapter()
        set_Fragment_Zipcode_Listener((Interface_Communicate_With_Adapter) fragment_zip);

        switch_units.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                System.out.println("On Options Item Selected");
//                System.out.println("switch units matching!");
//
//                switch_units.toggle();

                if(isChecked){
                    System.out.println("Switch is checked!");

                }else{
                    System.out.println("Switch is not checked!");
                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }


    // Implements from Fragment_Zipcode
    // TODO: Not loading on first click
    @Override
        public void onListFragmentInteraction(Model_Zipcode item) {
            Log.v(TAG, "onListFragmentInteraction");


            // Hide the softkeyboard
            hideKeyboard(this);


            // Clear out models so it is starting fresh when the fivedayforecast is generated.
            // Also prevents different zipcode forecasts from being added.
            // Should look into caching / saving data
            models.clear();

//            JSONParser jsonParser = new JSONParser(this, item.getZipcode(), "imperial");
            JSONParser jsonParser = new JSONParser(this, item.getZipcode(), unitType);


            // Return get the forecast and add it to models
            models.add(jsonParser.getSingle_day_forecast());
            models.addAll(jsonParser.getFive_day_forecast());

            FragmentManager fm = getSupportFragmentManager();
            fragment_detail = Fragment_Detail.newInstance(models);


        set_Fragment_Detail_Listener((Interface_Communicate_With_Adapter) fragment_detail);

        fm.popBackStack();

        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment_detail)
                .addToBackStack("Detail Fragment")
                .commit();


        updateFragment_DetailAdapater();


        // Used setting the correct Fragment on screen rotation
            currentFragment = FRAGMENT_DETAIL;


            selected_zipCode = item.getZipcode();
            et_enterZip.setHint(selected_zipCode + " Forecast");

            et_enterZip.setEnabled(false);
       // }

    }

    // Interface used to communicate with RecyclerView which acts upon the Fragment_Detail
    public void set_Fragment_Detail_Listener(Interface_Communicate_With_Adapter listener) {
        listener_Detail_Fragment = listener ;
    }

    // Interface used to communicate with RecyclerView which acts upon the Fragment_Zipcode
    public void set_Fragment_Zipcode_Listener(Interface_Communicate_With_Adapter listener) {
        listener_Zipcode_Fragment = listener ;
    }

    public void updateFragment_DetailAdapater(){
        // Updates the fragment_detail_adapter
        listener_Detail_Fragment.updateAdapter();
    }

    public void updateFragment_ZipcodeAdapater(){
        // Updates the fragment_detail_adapter
        listener_Zipcode_Fragment.updateAdapter();
    }

    public void addToFiveDayForecast(Model_Forecast forecast){
        models.add(forecast);
    }

    public ArrayList<Model_Forecast> getModels() {
        return models;
    }

    public void startProcessDialog(){
        if(pDialog == null){
            pDialog = new ProgressDialog(this);
        }

        if (!pDialog.isShowing()) {
            pDialog.setMessage("Loading...");
            pDialog.show();
        }
    }

    public void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onListFragmentInteraction(Model_Forecast item) {

    }

    /*
     Checks if the zipcode entered is 5 digits in length
              True:
                1) Display a successful Snackbar to the user
                2) Set et_enterZip text to ""
                3) Create new Model_Zipcode and add it to al_zipcodes
                4) Updates the adapter

              false: Display an unsuccessful Snackbar to the user
  */
    public void addZipCode() {


        String enteredZipcode = et_enterZip.getText().toString();

        if(enteredZipcode.length() == 5) {

            if(!checkDuplicateZipCodeEntry(enteredZipcode)) {
                Snackbar.make(this.findViewById(R.id.fragment_container), "Zipcode: " + enteredZipcode + " Has Been Added!", Snackbar.LENGTH_SHORT).show();
                et_enterZip.setText("");

                System.out.println("Adding to al_zipcodes: " + al_zipCodes.size());

                al_zipCodes.add(new Model_Zipcode(enteredZipcode.toString()));
                // Updates the adapter so the newly created Model_Zipcode is added and displayed on the Recyclerview
                updateFragment_ZipcodeAdapater();
            }
        }
        else{
            Snackbar.make(this.findViewById(R.id.fragment_container), "Zipcode Must be Five Digits! Try Again", Snackbar.LENGTH_SHORT).show();
        }
    }

    // Checks to see if the zipcode entered matches a zipcode already entered
    // Prevents duplicates
    public boolean checkDuplicateZipCodeEntry(String zipcode){
        for(Model_Zipcode item : al_zipCodes){
            if(item.getZipcode().contains(zipcode)){
                // Already contains zipcode
                Snackbar.make(this.findViewById(R.id.fragment_container), "This Zip Code Has Already Been Added", Snackbar.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
    // Hides the softkeyboard
    //  - Used when a another fragment is added
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


// -------------- DATA Persistence ----------------- //

    // TODO: Need to save the currently attached fragment or possibly both fragments?????
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("Activity Saving Instance State");

        outState.putInt("currentFragment", currentFragment);

        super.onSaveInstanceState(outState);

        saveData();

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "Fragment_Zipcode", fragment_zip);



// Causes Error when when first creating
//        Fragment_Detail tempFragment = (Fragment_Detail) getSupportFragmentManager().findFragmentByTag("Fragment Detail");
//
//        if(fragment_detail != null){
//
//            System.out.println("Putting fragment detail into outstate");
//            getSupportFragmentManager().putFragment(outState, "Fragment_Detail", fragment_detail);
//        }


    }

    // Save data to shared preferences
    //  1) GrabShared Preferences Obj
    //  2) Create Gson Object
    //  3) Save arraylist of zipcodes as JSON by using GSON
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(al_zipCodes);
        editor.putString("zipcodes", json);
        editor.putString("selected zipcode", selected_zipCode);
        editor.apply();

    }

    // Recover data from SharedPreferences
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("zipcodes", null);
        Type type = new TypeToken<ArrayList<Model_Zipcode>>(){}.getType();
        al_zipCodes = gson.fromJson(json, type);
        selected_zipCode = sharedPreferences.getString("selected zipcode", null);

        if (al_zipCodes == null) {
            al_zipCodes = new ArrayList<Model_Zipcode>();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        System.out.println("onBackPressed!");

        if (fragment_detail != null) {
            if (fragment_detail.isVisible()) {
                et_enterZip.setHint(selected_zipCode + " Forecast");
                et_enterZip.setEnabled(false);
                currentFragment = FRAGMENT_DETAIL;
            } else if (fragment_zip.isVisible()) {
                et_enterZip.setHint("Enter Zip Code");
                et_enterZip.setEnabled(true);
                currentFragment = FRAGMENT_ZIPCODE;
            }
        }
    }
}