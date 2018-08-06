package async.crash.com.phunweather.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import async.crash.com.phunweather.Fragments.Fragment_Zipcode;
import async.crash.com.phunweather.Models.Model_Zipcode;
import async.crash.com.phunweather.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Model_Zipcode} and makes a call to the
 * specified {@link Fragment_Zipcode.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_RecyclerView_Zipcode extends RecyclerView.Adapter<Adapter_RecyclerView_Zipcode.ViewHolder> {

    private final List<Model_Zipcode> mValues;
    private final Fragment_Zipcode.OnListFragmentInteractionListener mListener;

//    public Adapter_RecyclerView_Zipcode(List<DummyItem> items, Fragment_Zipcode.OnListFragmentInteractionListener listener) {
    public Adapter_RecyclerView_Zipcode(List<Model_Zipcode> items, Fragment_Zipcode.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_zipcode_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getId());
        holder.mContentView.setText(mValues.get(position).getZipcode());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Model_Zipcode mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
