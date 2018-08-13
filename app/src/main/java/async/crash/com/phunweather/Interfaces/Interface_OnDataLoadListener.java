package async.crash.com.phunweather.Interfaces;

import android.content.Context;

import java.util.ArrayList;

import async.crash.com.phunweather.Models.Model_Forecast;

/**
 * Created by mitchthornton on 8/12/18.
 * Used for Activity <----> Fragment communication
 */

public interface Interface_OnDataLoadListener {
    void onDataLoaded(Context context, ArrayList<Model_Forecast> forecast);
}
