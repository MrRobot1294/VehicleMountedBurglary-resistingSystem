package com.cjy.safecar.map;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.cjy.safecar.service.MyService;
import com.example.safecar.R;

import java.util.Calendar;

public class HistorySerachActivity extends AppCompatActivity implements ServiceConnection {

    private String carnumber;

    private Button startDate;
    private Button startTime;
    private Button endDate;
    private Button endTime;
    private RadioGroup intervals;
    private Button affirm;

    private Calendar start;
    private Calendar end;
    private int interval;

    private MyService.MyBinder binder;
    private MyService myService;

    private IntentFilter carHistoryIntentFilter = new IntentFilter("com.broadcast.CAR_LOCATION_HISTORY");
    private CarLocationHistoryReceiver carLocationHistoryReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_serach);

        startDate = (Button) findViewById(R.id.startdate);
        startTime = (Button) findViewById(R.id.starttime);
        endDate = (Button) findViewById(R.id.enddate);
        endTime = (Button) findViewById(R.id.endtime);
        affirm = (Button) findViewById(R.id.affirm);

        intervals = (RadioGroup) findViewById(R.id.intervals);

        carnumber = getIntent().getStringExtra("carlocation");

        start = Calendar.getInstance();
        end = Calendar.getInstance();

        startDate.setOnClickListener(new SerachHistoryOnClikListener());
        endDate.setOnClickListener(new SerachHistoryOnClikListener());
        startTime.setOnClickListener(new SerachHistoryOnClikListener());
        endTime.setOnClickListener(new SerachHistoryOnClikListener());
        affirm.setOnClickListener(new SerachHistoryOnClikListener());

        carLocationHistoryReceiver = new CarLocationHistoryReceiver();
        bindService(new Intent(HistorySerachActivity.this, MyService.class), HistorySerachActivity.this, BIND_AUTO_CREATE);
        registerReceiver(carLocationHistoryReceiver, carHistoryIntentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binder != null) {
            unbindService(HistorySerachActivity.this);
            myService = null;
            binder = null;
            unregisterReceiver(carLocationHistoryReceiver);
//            Toast.makeText(getApplicationContext(),"unbindservice",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (MyService.MyBinder) service;
        myService = binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        binder = null;
        myService = null;
    }

    public class CarLocationHistoryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string;
            string = intent.getStringExtra("historycarlocation");
            Log.v("debug",string);
            if(string.equals("error")) {

            } else {
                Intent i = new Intent(HistorySerachActivity.this, MapActivity.class);
                i.putExtra("carlocation",string);
                startActivity(i);
                finish();
            }
        }
    }

    public class SerachHistoryOnClikListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startdate: {
                    new DatePickerDialog(HistorySerachActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            start.set(year, month, dayOfMonth);
                            Log.v("debug", year + "-" + month + "-" + dayOfMonth);
                        }
                    }, start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                }
                case R.id.enddate: {
                    new DatePickerDialog(HistorySerachActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            end.set(year, month, dayOfMonth);
                            Log.v("debug", year + "-" + month + "-" + dayOfMonth);
                        }
                    }, end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH)).show();
                    break;
                }
                case R.id.starttime: {
                    new TimePickerDialog(HistorySerachActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            start.set(start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                            Log.v("debug", hourOfDay + ":" + minute);
                        }
                    }, start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), true).show();
                    break;
                }
                case R.id.endtime: {
                    new TimePickerDialog(HistorySerachActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            end.set(end.get(Calendar.YEAR), end.get(Calendar.MONTH), end.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
                            Log.v("debug", hourOfDay + ":" + minute);
                        }
                    }, end.get(Calendar.HOUR_OF_DAY), end.get(Calendar.MINUTE), true).show();
                    break;
                }
                case R.id.affirm: {
                    if(start.compareTo(end) <= 0) {
                        String startstring = "" + start.get(Calendar.YEAR) + ":" + start.get(Calendar.MONTH) + ":" + start.get(Calendar.DAY_OF_MONTH) + ":" + start.get(Calendar.HOUR_OF_DAY) + ":" + start.get(Calendar.MINUTE) + ";";
                        String endstring = "" + end.get(Calendar.YEAR) + ":" + end.get(Calendar.MONTH) + ":" + end.get(Calendar.DAY_OF_MONTH) + ":" + end.get(Calendar.HOUR_OF_DAY) + ":" + end.get(Calendar.MINUTE) + ";";

                        for (int i = 0; i < intervals.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) intervals.getChildAt(i);
                            if (radioButton.isChecked()) {
                                myService.serachHistoryCarLocation(carnumber, startstring, endstring, i);
                                break;
                            }
                        }
                    }
                }
                default: {

                }
            }
        }
    }
}
