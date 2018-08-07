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
public class Adapter_Unfolding extends ArrayAdapter<Model_Forecast> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;

    public Adapter_Unfolding(Context context, List<Model_Forecast> objects, Fragment_Detail.OnFragmentInteractionListener listener) {
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


            viewHolder.tv_maxTemp =  (TextView) cell.findViewById(R.id.tv_maxtemp);
            viewHolder.tv_minTemp =  (TextView) cell.findViewById(R.id.tv_mintemp);
            viewHolder.tv_currentTemp =  (TextView) cell.findViewById(R.id.tv_currenttemp);
//        tv_dayOfWeek =  (TextView) view.findViewById(R.id.tv_dayofweek);
            viewHolder.tv_date =  (TextView) cell.findViewById(R.id.tv_date);
            viewHolder.tv_weatherDescription = (TextView) cell.findViewById(R.id.tv_weather_description);
            viewHolder.iv_weatherIcon = (ImageView) cell.findViewById(R.id.forecast_iv_thumbnail);

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

        // bind data from selected element to view through view holder
//        viewHolder.tv_maxTemp.setText(item.getPrice());
//        viewHolder.tv_date.setText(item.getFromAddress());
//        viewHolder.tv_weatherDescription.setText(item.getToAddress());
//        viewHolder.iv_weatherIcon.setText(String.valueOf(item.getRequestsCount()));
//        viewHolder.tv_minTemp.setText(item.getPledgePrice());

        viewHolder.tv_date.setText(item.getDate());
        viewHolder.tv_maxTemp.setText(Double.toString(item.getMaxTemp()) + "F");
        viewHolder.tv_minTemp.setText(Double.toString(item.getMinTemp()) + "F");
        viewHolder.tv_currentTemp.setText(Double.toString(item.getCurrentTemp()) + "F");
        viewHolder.tv_weatherDescription.setText(item.getDescription());
        viewHolder.iv_weatherIcon.setImageResource(item.getDrawableID());

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            viewHolder.tv_currentTemp.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            viewHolder.tv_currentTemp.setOnClickListener(defaultRequestBtnClickListener);
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
        ImageView iv_weatherIcon;

    }
}



