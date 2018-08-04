package async.crash.com.phunweather.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import async.crash.com.phunweather.Fragments.DetailFragment.OnFragmentInteractionListener;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;
import async.crash.com.phunweather.dummy.DummyContent.DummyItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_RecyclerView_Detail_Item extends RecyclerView.Adapter<Adapter_RecyclerView_Detail_Item.ViewHolder> {

//    private final List<DummyItem> mValues;
    private final List<Model_Forecast> mValues;
    private final OnFragmentInteractionListener mListener;

    private TextView tv_dayTemp, tv_nightTemp, tv_currentTemp, tv_dayOfWeek, tv_date, tv_weatherDescription;
    private ImageView iv_weatherIcon;

//    public Adapter_RecyclerView_Detail_Item(List<DummyItem> items, OnFragmentInteractionListener listener) {
    public Adapter_RecyclerView_Detail_Item(List<Model_Forecast> items, OnFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.forecast_row_item, parent, false);

        tv_dayTemp =  (TextView) view.findViewById(R.id.tv_daytemp);
        tv_nightTemp =  (TextView) view.findViewById(R.id.tv_nighttemp);
        tv_currentTemp =  (TextView) view.findViewById(R.id.tv_currenttemp);
        tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
        tv_date =  (TextView) view.findViewById(R.id.tv_date);
        tv_weatherDescription = (TextView) view.findViewById(R.id.tv_weather_description);
        iv_weatherIcon = (ImageView) view.findViewById(R.id.forecast_iv_thumbnail);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

        holder.tv_dayOfWeek.setText("Today");
        holder.tv_date.setText(mValues.get(position).getDate());
        holder.tv_dayTemp.setText(Double.toString(mValues.get(position).getMaxTemp()));
        holder.tv_nightTemp.setText(Double.toString(mValues.get(position).getMinTemp()));
        holder.tv_currentTemp.setText(Double.toString(mValues.get(position).getCurrentTemp()));
        holder.tv_weatherDescription.setText(mValues.get(position).getDescription());
        holder.iv_weatherIcon.setImageResource(mValues.get(position).getDrawableID());


//        android:id="@+id/forecast_iv_thumbnail"


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
//                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
//        public final View mView;
//        public final TextView mIdView;
//        public final TextView mContentView;
////        public DummyItem mItem;
//        public Model_Forecast mItem;
//
//        public ViewHolder(View view) {
//            super(view);
//            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
//            mContentView = (TextView) view.findViewById(R.id.content);
//        }

        public final View mView;
        public final TextView tv_dayTemp, tv_nightTemp, tv_currentTemp, tv_dayOfWeek, tv_date, tv_weatherDescription;
        public final ImageView iv_weatherIcon;
//        public DummyItem mItem;
        public Model_Forecast mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_dayTemp =  (TextView) view.findViewById(R.id.tv_daytemp);
            tv_nightTemp =  (TextView) view.findViewById(R.id.tv_nighttemp);
            tv_currentTemp =  (TextView) view.findViewById(R.id.tv_currenttemp);
            tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
            tv_date =  (TextView) view.findViewById(R.id.tv_date);
            tv_weatherDescription =  (TextView) view.findViewById(R.id.tv_weather_description);
            iv_weatherIcon = (ImageView) view.findViewById(R.id.forecast_iv_thumbnail);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tv_dayOfWeek.getText() + "'";
        }
    }
}
