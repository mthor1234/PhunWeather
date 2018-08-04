package async.crash.com.phunweather.Models;

/**
 * Created by mitchthornton on 8/1/18.
 *
 * Holds the data for the weather forecast
 *
 * https://www.youtube.com/watch?v=Px9KanE-ohk
 *
 *
 * JSON Response from OpenWeatherMap
 *  {
 "cod":"200",
 "message":0.0042,
 "cnt":38,
 "list":[
 {
 "dt":1533276000,
 "main":{
 "temp":298.81,
 "temp_min":298.685,
 "temp_max":298.81,
 "pressure":1010.75,
 "sea_level":1029.94,
 "grnd_level":1010.75,
 "humidity":58,
 "temp_kf":0.13
 },
 "weather":[
 {
 "id":800,
 "main":"Clear",
 "description":"clear sky",
 "icon":"01d"
 }
 ],
 "clouds":{
 "all":0
 },
 "wind":{
 "speed":2.41,
 "deg":309.506
 },
 "sys":{
 "pod":"d"
 },
 "dt_txt":"2018-08-03 06:00:00"
 },
 */

public class Model_Forecast {

    private double currentTemp, minTemp, maxTemp, humidity, windSpeed;
    private String description, date;
//    private Drawable weatherIcon;
    private int drawableID;

    public Model_Forecast(double currentTemp, double minTemp, double maxTemp, double humidity, double windSpeed, String description, String date, int drawableID) {
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.date = date;
        this.drawableID = drawableID;
    }

//    public Model_Forecast(double minTemp, double maxTemp, double humidity, double windSpeed, String description, Bitmap weatherIcon) {
//        this.currentTemp = currentTemp;
//        this.minTemp = minTemp;
//        this.maxTemp = maxTemp;
//        this.humidity = humidity;
//        this.windSpeed = windSpeed;
//        this.description = description;
//        this.weatherIcon = weatherIcon;
//    }

    public Model_Forecast() {
    }

    public double getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Bitmap getWeatherIcon() {
//        return weatherIcon;
//    }
//
//    public void setWeatherIcon(Bitmap weatherIcon) {
//        this.weatherIcon = weatherIcon;
//    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDrawableID() {
        return drawableID;
    }

    public void setDrawableID(int drawableID) {
        this.drawableID = drawableID;
    }
}
