package async.crash.com.phunweather.Models;

/**
 * Created by mitchthornton on 7/31/18.
 */

public class Model_Zipcode {

    private String zipcode;
    private String city;
    private String state;
    private String id;

// ------------- Constructors ------------------ //

    //  Default Constructor //
    public Model_Zipcode() {
    }

    public Model_Zipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public Model_Zipcode(String zipcode, String city, String state) {
        this.zipcode = zipcode;
        this.city = city;
        this.state = state;
    }

// ------------- Constructors End --------------- //

// ------------- Getters / Setters -------------- //
    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // ------------- Getters / Setters End  ----------- //

}




