package async.crash.com.phunweather.Models;

import java.util.ArrayList;

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

    private int currentTemp, humidity, windSpeed;
    private String description, date;

    private int maxTemp = -99;
    private int minTemp = 99;

    private ArrayList<Integer> minTemps = new ArrayList<Integer>();
    private ArrayList<Integer> maxTemps = new ArrayList<Integer>();
    private ArrayList<Integer> humidities = new ArrayList<Integer>();
    private ArrayList<Integer> windSpeeds = new ArrayList<Integer>();
    private ArrayList<String> times = new ArrayList<String>();
    private ArrayList<Integer> weatherIcons = new ArrayList<Integer>();




    //    private Drawable weatherIcon;
    private int drawableID;

    public Model_Forecast(int currentTemp, int minTemp, int maxTemp, int humidity, int windSpeed, String description, String date, int drawableID) {
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.date = date;
        this.drawableID = drawableID;
    }

    public Model_Forecast(int minTemp, int maxTemp, int humidity, int windSpeed, String description, String date, int drawableID) {
        this.currentTemp = currentTemp;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.description = description;
        this.date = date;
        this.drawableID = drawableID;
    }

    public Model_Forecast(ArrayList<Integer> minTemps, ArrayList<Integer> maxTemps, ArrayList<Integer> humidities, ArrayList<Integer> windSpeeds, ArrayList<String> times, int drawableID) {
        this.minTemps = minTemps;
        this.maxTemps = maxTemps;
        this.humidities = humidities;
        this.windSpeeds = windSpeeds;
        this.times = times;
        this.drawableID = drawableID;

//        this.minTemp = calculateMinTemp();
//        this.maxTemp = calculateMaxTemp();

    }

    public Model_Forecast() {
//        this.minTemp = calculateMinTemp();
//        this.maxTemp = calculateMaxTemp();
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public void setCurrentTemp(int currentTemp) {
        this.currentTemp = currentTemp;
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

    public ArrayList<Integer> getMinTemps() {
        return minTemps;
    }

    public void setMinTemps(ArrayList<Integer> minTemps) {
        this.minTemps = minTemps;
    }

    public ArrayList<Integer> getMaxTemps() {
        return maxTemps;
    }

    public void setMaxTemps(ArrayList<Integer> maxTemps) {
        this.maxTemps = maxTemps;
    }

    public ArrayList<Integer> getHumidities() {
        return humidities;
    }

    public void setHumidities(ArrayList<Integer> humidities) {
        this.humidities = humidities;
    }

    public ArrayList<Integer> getWindSpeeds() {
        return windSpeeds;
    }

    public void setWindSpeeds(ArrayList<Integer> windSpeeds) {
        this.windSpeeds = windSpeeds;
    }

    public ArrayList<String> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<String> times) {
        this.times = times;
    }

    public void addMinTemp(int minTemp){
        minTemps.add(minTemp);

        if(this.minTemp > minTemp){
            this.minTemp = minTemp;
        }

    }

    public void addMaxTemp(int maxTemp){
        maxTemps.add(maxTemp);

        if(this.maxTemp < maxTemp){
            this.maxTemp = maxTemp;
        }
    }

    public void addHumidity(int humidity){
        humidities.add(humidity);
    }

    public void addWindSpeed(int windSpeed){
        windSpeeds.add(windSpeed);
    }

    public void addTime(String time){
        times.add(time);
    }

    public void print(){
        System.out.println("Printing forecast!!!: ");
        for(int temp: minTemps){
            System.out.println("Min Temp: " + temp);
        }
        for(int temp: maxTemps){
            System.out.println("Max Temp: " + temp);
        }
        for(int humidity: humidities){
            System.out.println("Min humidity: " + humidity);
        }
    }

    public void calculateAverageTemp(){
        currentTemp = (minTemp + maxTemp)/2;
    }

    public void calculateAverageTemp(int temp){
        currentTemp = temp;
    }

    public void addWeatherIconPath(int id){weatherIcons.add(id);}


    // TODO: Create a method to generate the most common icon
    // Needs to match the description
    public void calculateWeatherIcon(){
//        for(int id: weatherIcons){
//
//        }
        if(weatherIcons.size() > 0) {
            drawableID = weatherIcons.get(0);
        }

    }
//
//
//    public int calculateMaxTemp(){
//
//        for(int maxTemp: maxTemps){
//            if(maxTemp > currMaxTemp){
//                currMaxTemp = maxTemp;
//            }
//        }
//        return currMaxTemp;
//    }
}
