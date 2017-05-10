package user;

import java.util.Calendar;

/**
 * Created by 骄阳 on 2017/4/26.
 */

public class CarLocation {
    private String carNumber;
    private Calendar date;
    private String state;
    private String longitude;
    private String latitude;

    public CarLocation(String carNumber, Calendar date, String latitude, String state, String longitude) {
        this.carNumber = carNumber;
        this.date = date;
        this.latitude = latitude;
        this.state = state;
        this.longitude = longitude;
    }

    public CarLocation() {
    }

    public String getCarNumber() {
        return carNumber;
    }

    public Calendar getDate() {
        return date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getState() {
        return state;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setState(String state) {
        this.state = state;
    }
}
