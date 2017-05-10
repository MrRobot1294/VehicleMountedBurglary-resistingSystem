package user;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by 骄阳 on 2017/4/25.
 */

public class User {
    private String name;
    private String telePhoneNumber;
    private String password;
    private String identity;
    private Set<Car> cars = new HashSet<Car>();

    public User() {
    }

    public User(String name, String telePhoneNumber, String password, String identity) {
        this.name = name;
        this.telePhoneNumber = telePhoneNumber;
        this.password = password;
        this.identity = identity;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTelePhoneNumber() {
        return telePhoneNumber;
    }
    public void setTelePhoneNumber(String telePhoneNumber) {
        this.telePhoneNumber = telePhoneNumber;
    }
    public Set<Car> getCars() {
        return cars;
    }
    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
    public void addCars(Car car) {
        cars.add(car);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public String getPassword() {
        return password;
    }
}
