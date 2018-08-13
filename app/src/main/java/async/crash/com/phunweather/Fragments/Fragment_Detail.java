package async.crash.com.phunweather.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_ListView_Detail;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_UnitType;
import async.crash.com.phunweather.Interfaces.Interface_OnDataLoadListener;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;

/**
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 *
 * SUMMARY: Fragment used to display weather forecast information
 *          Views support flipping via RAMotion library and ListView
 *          First date represents today's forecast
 *          Subsequent days represent the rest of the forecast
 */
public class Fragment_Detail extends Fragment
implements Interface_Communicate_UnitType, Interface_OnDataLoadListener{

// ------- Static ------ //
    private static final String TAG = Fragment_Detail.class.getSimpleName();

// ------- Interface ------ //
    private OnFragmentInteractionListener mListener;

// ------- boolean ------ //
    private boolean unitType = true;

// ------- Adapter ------ //
    private Adapter_ListView_Detail adapter;

// ------- ListView ------ //
    private ListView listView;

// ------- ArrayList ------ //
    private ArrayList<Model_Forecast> weather_forecast = new ArrayList<Model_Forecast>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_Detail() {
    }

    public static Fragment_Detail newInstance() {
        Fragment_Detail fragment = new Fragment_Detail();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_detail, container, false);
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.tool_bar);

        // Unit Switch
        Switch switch_units = (Switch) toolbar.findViewById(R.id.switch_units);

        // Create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        adapter = new Adapter_ListView_Detail(getActivity(), weather_forecast, mListener);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // Set the adapter
        if (view instanceof ListView) {
            Context context = view.getContext();
            listView = (ListView) view;

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

        // Handles when the user switches between Metric and Imperial
        // Imperial by default
        switch_units.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    unitType = true;
                    adapter.setUnitType(unitType);
                }else{
                    unitType = false;
                    adapter.setUnitType(unitType);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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


    // Used to update units from Metric <---> Imperial
    @Override
    public void changeUnitType(boolean unitType) {
        adapter.setUnitType(unitType);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDataLoaded(Context context,ArrayList<Model_Forecast> forecast) {
        if(forecast != null) {
            weather_forecast = forecast;
        }
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

    public interface OnFragmentInteractionListener {
        void onListFragmentInteraction(Model_Forecast item);
    }

}
