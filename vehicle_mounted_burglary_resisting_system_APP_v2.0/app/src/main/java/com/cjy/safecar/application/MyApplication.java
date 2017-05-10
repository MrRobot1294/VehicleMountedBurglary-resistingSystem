package com.cjy.safecar.application;

import android.app.Application;

import java.util.HashMap;

/**
 * Created by 骄阳 on 2017/4/25.
 */

public class MyApplication extends Application {
    private HashMap<String,Object> hashMap;

    @Override
    public void onCreate() {
        super.onCreate();
        hashMap = new HashMap<String, Object>();


//        User user = new User("aaa","11111111111","123456","user");
//        user.addCars(new Car("10000","NO"));
//        user.addCars(new Car("10001","YES"));
//        hashMap.put("user",user);

//        double latitude = +31.973901;
//        double longitude = +119.378666;
//        CarLocation car;
//        LinkedList<CarLocation> carLocations = new LinkedList<>();
//        for(int i = 1; i < 10; i++) {
//            car = new CarLocation("10000", new GregorianCalendar(2017,0,1,i,0,0),new Double(latitude + i * 0.01).toString(),"NO",new Double(longitude + i * 0.01).toString());
//            carLocations.add(car);
//        }
//        for(int i = 1; i < 10; i++) {
//            car = new CarLocation("10000", new GregorianCalendar(2017,0,1,9 + i,0,0),new Double(latitude + i * 0.01 + 0.10).toString(),"YES",new Double(longitude + i * 0.01 + 0.10).toString());
//            carLocations.add(car);
//        }
//        hashMap.put("10000",carLocations);
    }

    public HashMap<String,Object> getHashMap() {
        return hashMap;
    }
}
