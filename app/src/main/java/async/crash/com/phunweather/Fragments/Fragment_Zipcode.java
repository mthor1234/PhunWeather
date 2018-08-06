package async.crash.com.phunweather.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Zipcode;
import async.crash.com.phunweather.Interfaces.Interface_Communicate_With_Adapter;
import async.crash.com.phunweather.Models.Model_Zipcode;
import async.crash.com.phunweather.R;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class Fragment_Zipcode extends Fragment
            implements Interface_Communicate_With_Adapter{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private static ArrayList<Model_Zipcode> al_zipCodes;

    private RecyclerView recyclerView;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Fragment_Zipcode() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Fragment_Zipcode newInstance(ArrayList<Model_Zipcode> items) {
        Fragment_Zipcode fragment = new Fragment_Zipcode();

        al_zipCodes = items;

        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, 1);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.tool_bar);

        /*
        TODO: Null pointers on toolbar items
           Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.ImageButton.setOnClickListener(android.view.View$OnClickListener)' on a null object reference
            at async.crash.com.phunweather.Fragments.Fragment_Zipcode.onCreateView(Fragment_Zipcode.java:154)
         */

        // ImageButtons
//        imgBtn_settings = (ImageButton) toolbar.findViewById(R.id.action_settings);
//        imgbtn_addZip = (ImageButton) toolbar.findViewById(R.id.zipcode_imgbtn_add);
//
//        // EditText
//        et_enterZip = (EditText) toolbar.findViewById(R.id.et_zipcode);

//        toolbar.inflateMenu(R.menu.menu_main);
//        toolbar.setOnMenuItemClickListener(this);



        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
                recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
//            recyclerView.setAdapter(new Adapter_RecyclerView_Zipcode(DummyContent.ITEMS, mListener));
            recyclerView.setAdapter(new Adapter_RecyclerView_Zipcode(al_zipCodes, mListener));
        }


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
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
    public void updateAdapater() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Model_Zipcode item);
    }

}
