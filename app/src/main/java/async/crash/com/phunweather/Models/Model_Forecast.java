package async.crash.com.phunweather.Models;

import android.graphics.Bitmap;

/**
 * Created by mitchthornton on 8/1/18.
 *
 * Holds the data for the weather forecast
 *
 * https://www.youtube.com/watch?v=Px9KanE-ohk
 *
 *
 * JSON Response from OpenWeatherMap
 * {"city":{"id":1851632,"name":"Shuzenji",
 "coord":{"lon":138.933334,"lat":34.966671},
 "country":"JP",
 "cod":"200",
 "message":0.0045,
 "cnt":38,
 "list":[{
 "dt":1406106000,
 "main":{
 "temp":298.77,
 "temp_min":298.77,
 "temp_max":298.774,
 "pressure":1005.93,
 "sea_level":1018.18,
 "grnd_level":1005.93,
 "humidity":87,
 "temp_kf":0.26},
 "weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],
 "clouds":{"all":88},
 "wind":{"speed":5.71,"deg":229.501},
 "sys":{"pod":"d"},
 "dt_txt":"2014-07-23 09:00:00"}
 ]}
 */

public class Model_Forecast {

    private int minTemp, maxTemp, humidity, windSpeed;
    private String description;
    private Bitmap weatherIcon;

    public Model_Forecast(int minTemp, int maxTemp, int humidity, int windSpeed, String description, Bitmap weatherIcon) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.weatherIcon = weatherIcon;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(Bitmap weatherIcon) {
        this.weatherIcon = weatherIcon;
    }
}
