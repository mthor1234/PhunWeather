package async.crash.com.phunweather.activities;

import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

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
List of Changes Made Since Last Push

1) Adapter now updates once the Volley response is parsed and all the items have been added to the forecast.
   Done by having JSONParser call activity_main.updateFragment_DetailAdapater(); which is a linked up via interfaces
   to the Fragment_Detail. Interfaces are used because that is the preferred method for communicating with Fragments

2) Appended "F" to all the temperature fields under fragment_row_item.xml.
   Consider updating the View_Model to hold a string version of the temperature in order for it to support:
        - Celsius & Farhenheit
        - Cleaner Code
        - More flexible by having a String version and an int version
3) Made the input type for SearchView numbers only

4) Added a ProcessDialog for network operations
    - Shows during the operation
    - Hides once the network operation is over and fragment_detail_adapter has update or a network error

5) Added a SnackBar to display a network error if one occurs

6) Adjusted the Toolbar to have a Settings button & EditText
   - Replaced SearchView with EditText (Easier to use, and not searching anything)
            * No longer trying to use API calls to verify zipcodes dynamically.
            *Too much work for the limited amount of time, limited API calls, and may affect performance

   - Settings button is on the far left

5) Model_Address refactored to Model_Zipcode

7) Deleted Adapter_AddressSearch

8) Refactored Adapater_MyItemRecyclerView -> Adapter_RecyclerView_Zipcode

9) Refactored fragment_zipcode_itemode_item.xml -> fragment_zipcode_item.xml

10) Removed
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


// ------ Volley ------ //
    private JsonArrayRequest request;
    private RequestQueue requestQueue;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


// ------ Fragments ------ //
    private Fragment fragment_detail;
    private Fragment fragment_zip;



// ------ Interfaces ------ //
    // Used to update adapters within those fragments
    private Interface_Communicate_With_Adapter listener_Detail_Fragment;
    private Interface_Communicate_With_Adapter listener_Zipcode_Fragment;



    // ------ Views ------ //
    private ImageButton imgBtn_settings;
    private EditText et_enterZip;



// ------ Adapters ------ //
    // Creating fragment_detail_adapter for Detail
    private Adapter_RecyclerView_Detail_Item fragment_detail_adapter;
    private Adapter_RecyclerView_Zipcode fragment_zipcode_adapter;


    private ProgressDialog pDialog;
    private ArrayList<Model_Forecast> models;
    private ArrayList<Model_Zipcode> al_zipCodes;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//--------- Setting Views ---------- //

        // ToolBar & removing toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        et_enterZip = (EditText) findViewById(R.id.et_zipcode);
        imgBtn_settings = (ImageButton) findViewById(R.id.action_settings);


        //--------- End Setting Views ---------- //


        models = new ArrayList<Model_Forecast>();
        al_zipCodes = new ArrayList<Model_Zipcode>();


        pDialog = new ProgressDialog(this);


        //--------- Fragment ---------- //
        // Create the Zipcode Fragment & Fragment Manager
        fragment_zip = Fragment_Zipcode.newInstance(al_zipCodes);

        // Setting interface to allow Activity_Main to call fragment_zip.updateAdapter()
        set_Fragment_Zipcode_Listener((Interface_Communicate_With_Adapter) fragment_zip);


        FragmentManager fm = getSupportFragmentManager();

        // Replace the Fragment
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment_zip)
                .addToBackStack(null)
                .commit();

        //--------- End of Fragment ---------- //


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

                    addZipCode();

                    handled = true;
                }
                return handled;
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
    @Override
        public void onListFragmentInteraction(Model_Zipcode item) {
            Log.v(TAG, "onListFragmentInteraction");


        // Hide the softkeyboard
        hideKeyboard(this);


        JSONParser jsonParser = new JSONParser(this, "06084");
        jsonParser.fiveDayForecast();

        Bundle bundle = new Bundle();

        FragmentManager fm = getSupportFragmentManager();
        fragment_detail = Fragment_Detail.newInstance(models);

        set_Fragment_Detail_Listener((Interface_Communicate_With_Adapter) fragment_detail);


        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment_detail)
                .addToBackStack(null)
                .commit();

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
        listener_Detail_Fragment.updateAdapater();
    }

    public void updateFragment_ZipcodeAdapater(){
        // Updates the fragment_detail_adapter
        listener_Zipcode_Fragment.updateAdapater();
    }

    public void addToFiveDayForecast(Model_Forecast forecast){
        models.add(forecast);
    }

    public ArrayList<Model_Forecast> getModels() {
        return models;
    }

    public void startProcessDialog(){
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
            Snackbar.make(this.findViewById(R.id.fragment_container), "Zipcode: " + enteredZipcode + " Has Been Added!", Snackbar.LENGTH_SHORT).show();
            et_enterZip.setText("");

            al_zipCodes.add(new Model_Zipcode(enteredZipcode.toString()));
            // Updates the adapter so the newly created Model_Zipcode is added and displayed on the Recyclerview
            updateFragment_ZipcodeAdapater();


        }else{
            Snackbar.make(this.findViewById(R.id.fragment_container), "Zipcode Must be Five Digits! Try Again", Snackbar.LENGTH_SHORT).show();

        }

    }

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
}