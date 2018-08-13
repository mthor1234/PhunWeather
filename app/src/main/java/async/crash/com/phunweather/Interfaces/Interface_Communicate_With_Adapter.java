package async.crash.com.phunweather.Interfaces;

import java.util.ArrayList;

import async.crash.com.phunweather.Models.Model_Zipcode;

/**
 * Created by mitchthornton on 8/4/18.
 * Used for Activity <----> Fragment communication
 */

public interface Interface_Communicate_With_Adapter {
//    void updateAdapter(Model_Zipcode zipcode);

    void addArrayListItem(Model_Zipcode zipCode);


    void setArrayList(ArrayList<Model_Zipcode> zipCodes);

    void updateAdapter();
}
