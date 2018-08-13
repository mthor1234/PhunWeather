package async.crash.com.phunweather.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.ramotion.foldingcell.FoldingCell;
import java.util.HashSet;
import java.util.List;
import async.crash.com.phunweather.Fragments.Fragment_Detail;
import async.crash.com.phunweather.Models.Model_Forecast;
import async.crash.com.phunweather.R;

/**
 * Created by mitchthornton on 8/6/18.
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Adapter_ListView_Detail extends ArrayAdapter<Model_Forecast> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    // Imperial by default
    private boolean unitType = true;

    public Adapter_ListView_Detail(Context context, List<Model_Forecast> objects, Fragment_Detail.OnFragmentInteractionListener listener) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // get item for selected view
        Model_Forecast item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder

// ------------- Settings Folded Views --------------- //
            viewHolder.tv_maxTemp =  (TextView) cell.findViewById(R.id.tv_maxtemp);
            viewHolder.tv_minTemp =  (TextView) cell.findViewById(R.id.tv_mintemp);
            viewHolder.tv_currentTemp =  (TextView) cell.findViewById(R.id.tv_currenttemp);
            viewHolder.tv_humidity =  (TextView) cell.findViewById(R.id.tv_humidity);
//        tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
            viewHolder.tv_date =  (TextView) cell.findViewById(R.id.tv_date);
            viewHolder.tv_weatherDescription = (TextView) cell.findViewById(R.id.tv_weather_description);
            viewHolder.tv_windSpeed = (TextView) cell.findViewById(R.id.tv_windspeed);
            viewHolder.tv_sunRise = (TextView) cell.findViewById(R.id.tv_sunrise);
            viewHolder.tv_sunSet = (TextView) cell.findViewById(R.id.tv_sunset);

            viewHolder.iv_weatherIcon = (ImageView) cell.findViewById(R.id.forecast_iv_thumbnail);



// ------------- Settings Unfolded Views --------------- //
            viewHolder.tv_maxTemp_unfolded =  (TextView) cell.findViewById(R.id.tv_maxtemp_unfolded);
            viewHolder.tv_minTemp_unfolded =  (TextView) cell.findViewById(R.id.tv_mintemp_unfolded);
            viewHolder.tv_currentTemp_unfolded=  (TextView) cell.findViewById(R.id.tv_currenttemp_unfolded);
            viewHolder.tv_humidity =  (TextView) cell.findViewById(R.id.tv_humidity);
            viewHolder.tv_weatherDescription_unfolded =  (TextView) cell.findViewById(R.id.tv_weather_description_unfolded);
//        tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
            viewHolder.tv_date_unfolded =  (TextView) cell.findViewById(R.id.tv_date_unfolded);
            viewHolder.tv_weatherDescription_unfolded = (TextView) cell.findViewById(R.id.tv_weather_description_unfolded);
            viewHolder.tv_windSpeed = (TextView) cell.findViewById(R.id.tv_windspeed);
            viewHolder.tv_sunRise = (TextView) cell.findViewById(R.id.tv_sunrise);
            viewHolder.tv_sunSet = (TextView) cell.findViewById(R.id.tv_sunset);

            viewHolder.iv_weatherIcon_unfolded = (ImageView) cell.findViewById(R.id.forecast_iv_thumbnail_unfolded);

            cell.setTag(viewHolder);


        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (null == item)
            return cell;

        if(unitType) {

            // Imperial

            // Folded Views
            viewHolder.tv_date.setText(item.getDate());
            viewHolder.tv_maxTemp.setText(Integer.toString(item.getMaxTemp()) + "°F");
            viewHolder.tv_minTemp.setText(Integer.toString(item.getMinTemp()) + "°F");
            viewHolder.tv_currentTemp.setText(Integer.toString(item.getCurrentTemp()) + "°F");
            viewHolder.iv_weatherIcon.setImageResource(item.getDrawableID());


            // Imperial

            // Unfolded Views
            viewHolder.tv_date_unfolded.setText(item.getDate());
            viewHolder.tv_maxTemp_unfolded.setText(Integer.toString(item.getMaxTemp()) + "°F");
            viewHolder.tv_minTemp_unfolded.setText(Integer.toString(item.getMinTemp()) + "°F");
            viewHolder.tv_currentTemp_unfolded.setText(Integer.toString(item.getCurrentTemp()) + "°F");
            viewHolder.tv_windSpeed.setText(Integer.toString(item.getWindSpeed()) + "MPH");
            viewHolder.tv_humidity.setText(Integer.toString(item.getHumidity()) + "%");
            viewHolder.iv_weatherIcon_unfolded.setImageResource(item.getDrawableID());

        }
        else {
            // Metric

            // Folded Views
            viewHolder.tv_date.setText(item.getDate());
            viewHolder.tv_maxTemp.setText(Integer.toString(item.getMaxTemp_Metric()) + "°C");
            viewHolder.tv_minTemp.setText(Integer.toString(item.getMinTemp_Metric()) + "°C");
            viewHolder.tv_currentTemp.setText(Integer.toString(item.getCurrentTemp_Metric()) + "°C");
            viewHolder.iv_weatherIcon.setImageResource(item.getDrawableID());


            // Metric

            // Unfolded Views
            viewHolder.tv_date_unfolded.setText(item.getDate());
            viewHolder.tv_maxTemp_unfolded.setText(Integer.toString(item.getMaxTemp_Metric()) + "°C");
            viewHolder.tv_minTemp_unfolded.setText(Integer.toString(item.getMinTemp_Metric()) + "°C");
            viewHolder.tv_currentTemp_unfolded.setText(Integer.toString(item.getCurrentTemp_Metric()) + "°C");
            viewHolder.tv_windSpeed.setText(Integer.toString(item.getWindSpeed_Metric()) + "KPH");
            viewHolder.tv_humidity.setText(Integer.toString(item.getHumidity()) + "%");
            viewHolder.iv_weatherIcon_unfolded.setImageResource(item.getDrawableID());

        }

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.tv_currentTemp.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.tv_currentTemp.setOnClickListener(defaultRequestBtnClickListener);
        }

        if(item.getWeather_Descriptions().size() > 0){
            viewHolder.tv_weatherDescription.setText(item.getWeather_Descriptions().get(0));
            viewHolder.tv_weatherDescription_unfolded.setText(item.getWeather_Descriptions().get(0));
        }else{
            viewHolder.tv_weatherDescription.setText(R.string.dummy_content);
            viewHolder.tv_weatherDescription_unfolded.setText(R.string.dummy_content);

        }

        // Getting sunrises and sunsets
        if(item.getSunrises().size() > 0) {
//            viewHolder.tv_sunRise.setText( item.getSunrises().get(position));
            viewHolder.tv_sunRise.setText( item.getSunrises().get(0));
        }
        else{
            viewHolder.tv_sunRise.setText(R.string.dummy_content);
        }

        if(item.getSunsets().size() > 0) {
//            viewHolder.tv_sunSet.setText( item.getSunsets().get(position));
            viewHolder.tv_sunSet.setText( item.getSunsets().get(0));
        }
        else{
            viewHolder.tv_sunSet.setText(R.string.dummy_content);
        }


        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView tv_maxTemp;
        TextView tv_currentTemp;
        TextView tv_minTemp;
        TextView tv_date;
        TextView tv_weatherDescription;
        TextView tv_windSpeed;
        TextView tv_humidity;
        TextView tv_sunRise;
        TextView tv_sunSet;
        ImageView iv_weatherIcon;

        TextView tv_maxTemp_unfolded;
        TextView tv_currentTemp_unfolded;
        TextView tv_minTemp_unfolded;
        TextView tv_date_unfolded;
        TextView tv_weatherDescription_unfolded;
        ImageView iv_weatherIcon_unfolded;


    }

    public void changeText(boolean unitType){

    }

    public void setUnitType(boolean unitType) {
        this.unitType = unitType;
        this.notifyDataSetChanged();
    }
}



