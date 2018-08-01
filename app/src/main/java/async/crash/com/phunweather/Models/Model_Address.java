package async.crash.com.phunweather.Models;

/**
 * Created by mitchthornton on 7/31/18.
 */

public class Model_Address {

    private String zipcode;
    private String city;
    private String state;

    public Model_Address(String zipcode, String city, String state) {
        this.zipcode = zipcode;
        this.city = city;
        this.state = state;
    }

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
}
