package com.cjy.safecar.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.cjy.safecar.application.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import user.Car;
import user.CarLocation;
import user.User;

public class MyService extends Service {
    private int flag = 0;
    private Socket socket;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private Intent intentSignIn = new Intent("com.broadcast.SIGNIN");
    private Intent intentCarLocationNow = new Intent("com.broadcast.CAR_LOCATION_NOW");
    private Intent intentCarLocationHistory = new Intent("com.broadcast.CAR_LOCATION_HISTORY");
    private Intent intentSignUp = new Intent("com.broadcast.SIGNUP");
    private Intent intentCarChange = new Intent("com.broadcast.CARCHANGE");

    private MyApplication myApplication;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        myApplication = (MyApplication) getApplication();
        Thread thread = new Thread(new TCPReceive());
        thread.start();
    }

    @Override
    public void onDestroy() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (printWriter != null) {
                printWriter.close();
            }
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    public void send(String s) {
        Thread thread = new Thread(new TCPSend(s));
        thread.start();
    }

    public void userSignIn(User user) {
        send("U;" + user.getTelePhoneNumber() + ";" + user.getPassword());
        Log.v("debug", "U;" + user.getTelePhoneNumber() + ";" + user.getPassword());
    }

    public void signInAnswer(String string) {
        String[] strings = string.split(";");

        User user = new User(strings[3], strings[1], strings[2], strings[4]);

        Car car;
        for (int i = 5; i < strings.length; i++) {
            String[] strings1 = strings[i].split(":");
            Log.v("debug", strings1[1]);
            car = new Car(strings1[0], strings1[1]);
            user.addCars(car);
        }

        myApplication.getHashMap().remove("user");
        myApplication.getHashMap().put("user", user);
        intentSignIn.putExtra("signin", "success");
        sendBroadcast(intentSignIn);
        Log.v("debug", "signinsuccess");
    }

    public void userSignUp(User user) {
        send("R;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + user.getName());
        Log.v("debug", "R;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + user.getName());
    }

    public void carChange(String car) {
        User user = (User) myApplication.getHashMap().get("user");
        send("C;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + car);
        Log.v("debug", "C;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + car);
    }

    public void carChangeAnswer(String string) {
        String[] strings = string.split(";");

        User user = new User(strings[3], strings[1], strings[2], strings[4]);

        Car car;
        for (int i = 5; i < strings.length; i++) {
            String[] strings1 = strings[i].split(":");
            Log.v("debug", strings1[1]);
            car = new Car(strings1[0], strings1[1]);
            user.addCars(car);
        }

        myApplication.getHashMap().remove("user");
        myApplication.getHashMap().put("user", user);
        intentCarChange.putExtra("carchange", "success");
        sendBroadcast(intentCarChange);
        Log.v("debug", "carchangesuccess");
    }

    public void serachNowCarLocation(String car) {
        User user = (User) myApplication.getHashMap().get("user");
        send("N;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + car);
    }

    public void serachNowCarLocationAnswer(String string) {
        String[] strings = string.split(";");
        String[] carLocationStrings = strings[2].split(":");

        CarLocation carLocation = new CarLocation(strings[1], (Calendar) new GregorianCalendar(new Integer(carLocationStrings[1]), new Integer(carLocationStrings[2]), new Integer(carLocationStrings[3]), new Integer(carLocationStrings[4]), new Integer(carLocationStrings[5])), carLocationStrings[7], carLocationStrings[0], carLocationStrings[6]);
        myApplication.getHashMap().remove("now" + strings[1]);
        myApplication.getHashMap().put("now" + strings[1],carLocation);
        intentCarLocationNow.putExtra("nowcarlocation",strings[1]);
        sendBroadcast(intentCarLocationNow);
        Log.v("debug", "now" + strings[1]);
    }

    public void serachHistoryCarLocation(String car, String start, String end, int interval) {
        User user = (User) myApplication.getHashMap().get("user");
        send("H;" + user.getTelePhoneNumber() + ";" + user.getPassword() + ";" + car + ";" + start + end + interval);
    }

    public void serachHistoryCarLocationAnswer(String string) {
        String[] strings = string.split(";");
        String[] carLocationStrings;
        CarLocation carLocation;
        LinkedList<CarLocation> carLocations = new LinkedList<>();
        for (int i = 2; i < strings.length; i++) {
            carLocationStrings = strings[i].split(":");

            carLocation = new CarLocation(strings[1], (Calendar) new GregorianCalendar(new Integer(carLocationStrings[1]), new Integer(carLocationStrings[2]), new Integer(carLocationStrings[3]), new Integer(carLocationStrings[4]), new Integer(carLocationStrings[5])), carLocationStrings[7], carLocationStrings[0], carLocationStrings[6]);
            carLocations.add(carLocation);
        }
        myApplication.getHashMap().remove(strings[1]);
        myApplication.getHashMap().put(strings[1],carLocations);
        intentCarLocationHistory.putExtra("historycarlocation",strings[1]);
        sendBroadcast(intentCarLocationHistory);
    }

    public void quit() {
        flag = 1;
        while (flag == 1) {

        }
        Thread thread = new Thread(new TCPReceive());
        thread.start();
    }

    public class TCPReceive implements Runnable {
        @Override
        public void run() {
            String string = null;

            try {
                socket = new Socket("XXX.XXX.XXX.XXX", XXXX);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

                Log.v("debug", "link");

                while ((string = bufferedReader.readLine()) != null && flag == 0) {
                    Log.v("debug", string);
                    switch (string.charAt(0)) {
                        case 'U': {
                            if (string.equals("USER_ERROR")) {
                                intentSignIn.putExtra("signin", "error");
                                sendBroadcast(intentSignIn);
                            } else {
                                signInAnswer(string);
                            }
                            break;
                        }
                        case 'N': {
                            if (string.equals("NOW_CAR_LOCATION_ERROR")) {
                                Log.v("debug", "NOW_CAR_LOCATION_ERROR");
                                intentCarLocationNow.putExtra("nowcarlocation", "error");
                                sendBroadcast(intentCarLocationNow);
                            } else {
                                Log.v("debug", "NOW_CAR_LOCATION");
                                serachNowCarLocationAnswer(string);
                            }
                            break;
                        }
                        case 'H': {
                            if (string.equals("HISTORY_CAR_LOCATION_ERROR")) {
                                Log.v("debug", "HISTORY_CAR_LOCATION_ERROR");
                                intentCarLocationHistory.putExtra("historycarlocation", "error");
                                sendBroadcast(intentCarLocationHistory);
                            } else {
                                Log.v("debug", "HISTORY_CAR_LOCATION");
                                serachHistoryCarLocationAnswer(string);
                            }
                            break;
                        }
                        case 'R': {
                            if (string.equals("REGISTER_ERROR")) {
                                Log.v("debug", "REGISTER_ERROR");
                                intentSignUp.putExtra("signup", "error");
                                sendBroadcast(intentSignUp);
                            } else if(string.equals("REGISTER_SUCCESS")){
                                Log.v("debug", "REGISTER");
                                intentSignUp.putExtra("signup", "success");
                                sendBroadcast(intentSignUp);
                            }
                            break;
                        }
                        case 'C': {
                            if (string.equals("CARCHANGE_ERROR_NOCAR")) {
                                intentCarChange.putExtra("carchange", "error_nocar");
                                sendBroadcast(intentCarChange);
                            } else if (string.equals("CARCHANGE_ERROR_CARHASHOST")) {
                                intentCarChange.putExtra("carchange", "error_carhashost");
                                sendBroadcast(intentCarChange);
                            } else {
                                carChangeAnswer(string);
                            }
                            break;
                        }
                        default: {

                        }
                    }
                }

                bufferedReader.close();
                printWriter.close();
                socket.close();
                flag = 0;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }
    }

    public class TCPSend implements Runnable {
        String string;

        public TCPSend(String s) {
            string = s;
        }

        @Override
        public void run() {
            Log.v("debug", string);
            printWriter.println(string);
            printWriter.flush();
        }
    }

    public class MyBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

}
