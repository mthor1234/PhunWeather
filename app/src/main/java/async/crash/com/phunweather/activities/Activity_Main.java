package async.crash.com.phunweather.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Detail_Item;
import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Zipcode;
import async.crash.com.phunweather.Fragments.Fragment_Detail;
import async.crash.com.phunweather.Fragments.Fragment_Zipcode;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_UnitType;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_With_Adapter;
import async.crash.com.phunweather.Interfaces.Interface_OnDataLoadListener;
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
        Fragment_Detail.OnFragmentInteractionListener,
        Interface_OnDataLoadListener{

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
    private Interface_Communicate_UnitType listener_unitType;
    private Interface_OnDataLoadListener listener_OnDataLoaded;


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

    private JSONParser jsonParser;



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
        //      - True: Add it to the arraylist -> recyclerview / adapter
        //      - False: Show a Snackbar that the entry needs to be 5 digits in length

        et_enterZip.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addZipCode();

                    handled = true;
                }
                return handled;
            }
        });


        if (savedInstanceState == null) {
            System.out.println("Saved Instance State = null");
            currentFragment = FRAGMENT_ZIPCODE;

            loadData();


            // Create the Zipcode Fragment & Fragment Manager
            fragment_zip = Fragment_Zipcode.newInstance();

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


                // Using parsing cache to update the fragment_detail information
                jsonParser = new JSONParser(this, selected_zipCode, unitType);

                hideKeyboard(this);

                        // Used setting the correct Fragment on screen rotation
                        loadData();


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

        //--------- Switch ---------- //
        switch_units = (Switch) findViewById(R.id.switch_units);
        switch_units.setTextOff("C");
        switch_units.setTextOn("F");
        switch_units.setChecked(true);


    //--------- End Setting Views ---------- //

        // Setting interface to allow Activity_Main to call fragment_zip.updateAdapter()
        set_Fragment_Zipcode_Listener((Interface_Communicate_With_Adapter) fragment_zip);



//        switch_units.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if(isChecked){
//                    System.out.println("Switch is checked!");
//                    unitType = true;
//                }else{
//                    System.out.println("Switch is not checked!");
//                    unitType = false;
//                }
//            }
//        });



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
    @Override
        public void onListFragmentInteraction(Model_Zipcode item) {
            Log.v(TAG, "onListFragmentInteraction");


        // Hide the softkeyboard
            hideKeyboard(this);


        // Clear out models so it is starting fresh when the fivedayforecast is generated.
        // Also prevents different zipcode forecasts from being added.
        // Should look into caching / saving data
        models.clear();

        jsonParser = new JSONParser(this, item.getZipcode(), unitType);


        // Used setting the correct Fragment on screen rotation
        currentFragment = FRAGMENT_DETAIL;


        selected_zipCode = item.getZipcode();
        et_enterZip.setHint(selected_zipCode + " Forecast");

        et_enterZip.setEnabled(false);

    }

    // Interface used to communicate with RecyclerView which acts upon the Fragment_Detail
    public void set_Fragment_Detail_Listener(Interface_Communicate_With_Adapter listener) {
        listener_Detail_Fragment = listener ;
    }

    // Interface used to communicate with the adapter data for Fragment_Zipcode
    public void set_Fragment_Zipcode_Listener(Interface_Communicate_With_Adapter listener) {
        listener_Zipcode_Fragment = listener ;
    }

    // Interface used to communicate with Listvew which acts upon the Fragment_Zipcode
    public void set_OnDataLoadListener(Interface_OnDataLoadListener listener) {
        listener_OnDataLoaded = listener ;
    }

    // Interface used to communicate with RecyclerView which acts upon the Fragment_Zipcode
    public void set_unitType_Listener(Interface_Communicate_UnitType listener) {
        listener_unitType = listener ;
    }


    public void updateFragment_ZipcodeAdapater(){
        // Updates the fragment_detail_adapter

        System.out.println("-------------> UpdatingFragment_ZipcodeAdapter: " + selected_zipCode);
        listener_Zipcode_Fragment.updateAdapter();
    }

    public void zipcode_adapter_AddItem(Model_Zipcode zipCode){
        System.out.println("-------------> Adding Item to ZipCode Frag: " + zipCode.getZipcode());
        et_enterZip.setText("");

        listener_Zipcode_Fragment.addArrayListItem(zipCode);
    }

    public void zipcode_adapter_setArrayList(ArrayList<Model_Zipcode> zipCodes){
        System.out.println("-------------> Setting ArrayList to ZipCode Frag: " + zipCodes.size());
        listener_Zipcode_Fragment.setArrayList(zipCodes);
    }

    // Used to pass data to DetailFragment once JSONParser has finished grabbing the data
    @Override
    public void onDataLoaded(Context context, ArrayList<Model_Forecast> forecast) {
        models = forecast;

        fragment_detail = new Fragment_Detail().newInstance();

//                fragment_detail = new Fragment_Detail().newInstance(models);
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        fm.beginTransaction()
                .replace(R.id.fragment_container,  fragment_detail)
                .addToBackStack("Detail Fragment")
                .commit();

        set_OnDataLoadListener((Interface_OnDataLoadListener) fragment_detail);

        listener_OnDataLoaded.onDataLoaded(context, models);


    }

    public void changeUnitType(boolean unitType){
        // Updates the fragment_detail_adapter
        listener_unitType.changeUnitType(unitType);
    }

//    public void addToFiveDayForecast(Model_Forecast forecast){
//        models.add(forecast);
//    }


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

    public void addZipCode() {
        String enteredZipcode = et_enterZip.getText().toString();
        zipcode_adapter_AddItem(new Model_Zipcode(enteredZipcode));
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
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        System.out.println("Activity Saving Instance State");

        outState.putInt("currentFragment", currentFragment);

        super.onSaveInstanceState(outState);

        saveData();

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "Fragment_Zipcode", fragment_zip);


    }

    // Save data to shared preferences
    //  1) GrabShared Preferences Obj
    //  2) Create Gson Object
    //  3) Save arraylist of zipcodes as JSON by using GSON
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(al_zipCodes);
//        editor.putString("zipcodes", json);
        editor.putString("selected zipcode", selected_zipCode);
        editor.apply();

    }

    // Recover data from SharedPreferences
    private void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        selected_zipCode = sharedPreferences.getString("selected zipcode", null);

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

    public ArrayList<Model_Forecast> getModels() {
        return models;
    }


    public void setModels(ArrayList<Model_Forecast> models) {
        this.models = models;
    }

    public ArrayList<Model_Zipcode> getAl_zipCodes() {
        return al_zipCodes;
    }

    public void setAl_zipCodes(ArrayList<Model_Zipcode> al_zipCodes) {
        this.al_zipCodes = al_zipCodes;
    }

    @Override
    public void onListFragmentInteraction(Model_Forecast item) {

    }
}