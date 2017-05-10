package user;

/**
 * Created by 骄阳 on 2017/4/25.
 */

public class Car {
    private String number;
    private String state;

    public Car() {
    }

    public Car(String number, String state) {
        this.number = number;
        this.state = state;
    }

    public String getNumber() {
        return number;
    }

    public String getState() {
        return state;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setState(String state) {
        this.state = state;
    }
}
