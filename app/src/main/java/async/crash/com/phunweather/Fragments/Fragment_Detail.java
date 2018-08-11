package async.crash.com.phunweather.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ramotion.foldingcell.FoldingCell;

import java.lang.reflect.Type;
import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_Unfolding;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_With_Adapter;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class Fragment_Detail extends Fragment
implements Interface_Communicate_With_Adapter {

    private static final String TAG = Fragment_Detail.class.getSimpleName();


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private RecyclerView.Adapter adapter;
    private Adapter_Unfolding adapter;

    private ListView listView;

    private static ArrayList<Model_Forecast> weather_forecast;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_Detail() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
//    public static Fragment_Detail newInstance(int columnCount) {
//        Fragment_Detail fragment = new Fragment_Detail();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static Fragment_Detail newInstance(ArrayList<Model_Forecast> forecast) {
        Fragment_Detail fragment = new Fragment_Detail();
        Bundle args = new Bundle();

        weather_forecast = forecast;

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Fragment_Detail OnCreate!");

        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.tool_bar);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);


        System.out.println("OnCreateView");

        if(weather_forecast.size() > 0) {
            weather_forecast.get(0).setRequestBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set the adapter
//        if (view instanceof RecyclerView) {
        if (view instanceof ListView) {

            System.out.println("Setting the Adapter!!!!");
            Context context = view.getContext();
            listView = (ListView) view;

            // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
            adapter = new Adapter_Unfolding(getActivity(), weather_forecast, mListener);

            // add default btn handler for each request btn on each item if custom handler not found
            adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity().getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
                }
            });

            listView.setAdapter(adapter);

            // set on click event listener to list view
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    // toggle clicked cell state
                    ((FoldingCell) view).toggle(false);
                    // register in adapter that state for selected cell is toggled
                    adapter.registerToggle(pos);
                }
            });

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        System.out.println("Fragment_Detail: OnActivityCreated!");

        for(Model_Forecast item: weather_forecast){
            System.out.println("FRAGMENT_DETAIL: " + item.getDate());
        }

//        if(weather_forecast == null) {
//            loadData();
//        }

        if(savedInstanceState != null) {
//            loadData();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void updateAdapter() {
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

//        saveData();
    }
    // Overrides from Interface_Communicate_With_Adapater


    // Save data to shared preferences
    //  1) GrabShared Preferences Obj
    //  2) Create Gson Object
    //  3) Save arraylist of zipcodes as JSON by using GSON
    private void saveData(){

        System.out.println("Saving Detail Data");

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

//        for(Model_Forecast item: weather_forecast){

//            editor.putString("current temp", gson.toJson(item.getCurrentTemp()));
//            editor.putString("date", gson.toJson(item.getDate()));
//            editor.putString("description", gson.toJson(item.getDescription()));
//            editor.putString("min temp", gson.toJson(item.getMinTemp()));
//            editor.putString("max temp", gson.toJson(item.getMaxTemp()));
//            editor.putString("humidity", gson.toJson(item.getHumidity()));


//        public int getCurrentTemp() {
//                return currentTemp;
//            }
//
//            public int getMinTemp() {
//                return minTemp;
//            }
//
//            public int getMaxTemp() {
//                return maxTemp;
//            }
//
//            public int getHumidity() {
//                return humidity;
//            }
//
//            public int getWindSpeed() {
//                return windSpeed;
//            }
//
//            public String getDescription() {
//                return description;
//            }
//
//            public String getDate() {
//                return date;
//            }
//
//            public int getDrawableID() {
//                return drawableID;
//            }
//
//            public ArrayList<Integer> getMinTemps() {
//                return minTemps;
//            }
//
//            public ArrayList<Integer> getMaxTemps() {
//                return maxTemps;
//            }
//
//            public ArrayList<Integer> getHumidities() {
//                return humidities;
//            }
//
//            public ArrayList<Integer> getWindSpeeds() {
//                return windSpeeds;
//            }
//
//            public ArrayList<Long> getTimes() {
//                return times;
//            }
//
//            public ArrayList<CharSequence> getSunrises() {
//                return sunrises;
//            }
//
//            public ArrayList<CharSequence> getSunsets() {
//                return sunsets;
//            }
//
//            public ArrayList<String> getWeather_Descriptions() {
//                return weather_Descriptions;
//            }
//
//            public ArrayList<Integer> getWeatherIcons() {
//                return weatherIcons;
//            }



//            editor.apply();
        }




//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(weather_forecast);
//        editor.putString("weather forecast", json);
//        editor.apply();

    // Recover data from SharedPreferences
    private void loadData(){

        System.out.println("Loading Detail Data");


        for(Model_Forecast item: weather_forecast){
            System.out.println("FRAGMENT_DETAIL: " + item.getDate());
        }


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("weather forecast", null);
        Type type = new TypeToken<ArrayList<Model_Forecast>>(){}.getType();
        weather_forecast = gson.fromJson(json, type);

//        updateAdapter();

        System.out.println("Weather Forecast Info: " + weather_forecast);


        if (weather_forecast == null) {
            System.out.println("Weather Forecast is null!!!!: ");
            weather_forecast = new ArrayList<Model_Forecast>();
        }
        adapter.notifyDataSetChanged();

    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnDetailFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onListFragmentInteraction(ArrayList<Model_Forecast> item);
//    }


    public interface OnFragmentInteractionListener {
        void onListFragmentInteraction(Model_Forecast item);
    }

}
