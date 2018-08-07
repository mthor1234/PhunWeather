package async.crash.com.phunweather.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import async.crash.com.phunweather.Fragments.Fragment_Detail.OnFragmentInteractionListener;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Model_Forecast} and makes a call to the
 * specified {@link OnFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class Adapter_RecyclerView_Detail_Item extends RecyclerView.Adapter<Adapter_RecyclerView_Detail_Item.ViewHolder> {

//    private final List<DummyItem> mValues;
    private final List<Model_Forecast> mValues;
    private final OnFragmentInteractionListener mListener;

    private TextView tv_minTemp, tv_maxTemp, tv_currentTemp, tv_date, tv_weatherDescription;
    private ImageView iv_weatherIcon;


    public Adapter_RecyclerView_Detail_Item(List<Model_Forecast> mValues, OnFragmentInteractionListener listener) {
        this.mValues = mValues;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.forecast_row_item, parent, false);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cell, parent, false);

        tv_maxTemp =  (TextView) view.findViewById(R.id.tv_maxtemp);
        tv_maxTemp =  (TextView) view.findViewById(R.id.tv_mintemp);
        tv_currentTemp =  (TextView) view.findViewById(R.id.tv_currenttemp);
//        tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
        tv_date =  (TextView) view.findViewById(R.id.tv_date);
        tv_weatherDescription = (TextView) view.findViewById(R.id.tv_weather_description);
        iv_weatherIcon = (ImageView) view.findViewById(R.id.forecast_iv_thumbnail);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
//        holder.tv_dayOfWeek.setText("Today");
        holder.tv_date.setText(mValues.get(position).getDate());
        holder.tv_maxTemp.setText(Double.toString(mValues.get(position).getMaxTemp()) + "F");
        holder.tv_minTemp.setText(Double.toString(mValues.get(position).getMinTemp()) + "F");
        holder.tv_currentTemp.setText(Double.toString(mValues.get(position).getCurrentTemp()) + "F");
        holder.tv_weatherDescription.setText(mValues.get(position).getDescription());
        holder.iv_weatherIcon.setImageResource(mValues.get(position).getDrawableID());



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


//    public void updateAdapter(List<Model_Forecast> test){
//            System.out.println("MValues clear size: " + test.size());
////            this.mValues.clear();
////        mValues.add(test.get(0));
//            System.out.println("MValues post clear size: " + test.size());
//
////            this.mValues.addAll(test);
//            System.out.println("MValues updated size: " + this.mValues.size());
//            notifyDataSetChanged();
//        }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView tv_maxTemp, tv_minTemp, tv_currentTemp, tv_date, tv_weatherDescription;
        public final ImageView iv_weatherIcon;
//        public DummyItem mItem;
        public Model_Forecast mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_maxTemp =  (TextView) view.findViewById(R.id.tv_maxtemp);
            tv_minTemp =  (TextView) view.findViewById(R.id.tv_mintemp);
            tv_currentTemp =  (TextView) view.findViewById(R.id.tv_currenttemp);
//            tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
            tv_date =  (TextView) view.findViewById(R.id.tv_date);
            tv_weatherDescription =  (TextView) view.findViewById(R.id.tv_weather_description);
            iv_weatherIcon = (ImageView) view.findViewById(R.id.forecast_iv_thumbnail);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + tv_date.getText() + "'";
        }
    }

}
