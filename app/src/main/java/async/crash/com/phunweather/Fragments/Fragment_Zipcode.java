package async.crash.com.phunweather.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import async.crash.com.phunweather.Adapters.Adapter_RecyclerView_Zipcode;
import async.crash.com.phunweather.Helpers.RecyclerItemTouchHelper;
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
            implements Interface_Communicate_With_Adapter,
        RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private OnListFragmentInteractionListener mListener;

    private static ArrayList<Model_Zipcode> al_zipCodes;

    private RecyclerView recyclerView;
    private CoordinatorLayout coordinatorLayout;
    private Adapter_RecyclerView_Zipcode adapter;


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
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(savedInstanceState != null){

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Save the fragments state
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
                recyclerView = (RecyclerView) view;

            adapter = new Adapter_RecyclerView_Zipcode(context, al_zipCodes, mListener);


            // adding item touch helper
            // only ItemTouchHelper.LEFT added to detect Right to Left swipe
            // if you want both Right -> Left and Left -> Right
            // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
            ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
            new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                recyclerView.setAdapter(adapter);


            recyclerView.setAdapter(new Adapter_RecyclerView_Zipcode(context, al_zipCodes, mListener));
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


    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Adapter_RecyclerView_Zipcode.ViewHolder) {
            // get the removed item name to display it in snack bar

            //TODO: at async.crash.com.phunweather.Fragments.Fragment_Zipcode.onSwiped(Fragment_Zipcode.java:147)

            String zipcode = al_zipCodes.get(viewHolder.getAdapterPosition()).getZipcode();

            // backup of removed item for undo purpose
            final Model_Zipcode deletedItem = al_zipCodes.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();



            // remove the item from recycler view
//            adapter.removeItem(viewHolder.getAdapterPosition());
            al_zipCodes.remove(position);
            updateAdapter();


            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(R.id.fragment_container), zipcode + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
//                    adapter.restoreItem(deletedItem, deletedIndex);
                    al_zipCodes.add(deletedIndex, deletedItem);
                    updateAdapter();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void updateAdapter() {
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Model_Zipcode item);
    }

    public static ArrayList<Model_Zipcode> getAl_zipCodes() {
        return al_zipCodes;
    }

    public static void setAl_zipCodes(ArrayList<Model_Zipcode> al_zipCodes) {
        Fragment_Zipcode.al_zipCodes = al_zipCodes;
    }
}
