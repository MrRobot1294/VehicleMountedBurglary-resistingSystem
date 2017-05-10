package user;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cjy.safecar.application.MyApplication;
import com.cjy.safecar.login.LoginActivity;
import com.cjy.safecar.map.MapActivity;
import com.cjy.safecar.service.MyService;
import com.example.safecar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UserActivity extends AppCompatActivity implements ServiceConnection {

    private ListView carList;
    private SimpleAdapter adapter;
    private ArrayList<Map<String,Object>> cars;
    private TextView username;
    private ImageView addCar;
    private Button quit;
    private User user;

    private View view;
    private AlertDialog alertDialog;
    private EditText addCarNumber;

    private ProgressDialog progressDialog;
    private AlertDialog noCarDialog;
    private AlertDialog carHasHostDialog;

    private MyService.MyBinder binder;
    private MyService myService;

    private IntentFilter carNowIntentFilter = new IntentFilter("com.broadcast.CAR_LOCATION_NOW");
    private IntentFilter carChangeIntentFilter = new IntentFilter("com.broadcast.CARCHANGE");
    private CarLocationNowReceiver carLocationNowReceiver;
    private CarChangeReceiver carChangeReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        LayoutInflater inflater = LayoutInflater.from(UserActivity.this);
        view = inflater.inflate(R.layout.car_add_alertdialog,null);
        addCarNumber = (EditText) view.findViewById(R.id.addcarnumber);
        alertDialog = new AlertDialog.Builder(UserActivity.this).setTitle("输入车辆编号").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(addCarNumber.getText().toString().trim().equals("")) {
                    Toast.makeText(getApplicationContext(),"车辆编号不能为空",Toast.LENGTH_SHORT).show();
                } else {
                    myService.carChange(addCarNumber.getText().toString().trim());
                    //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                    progressDialog = ProgressDialog.show(UserActivity.this, null, "请稍后...", false, true);
                    addCarNumber.setText("");
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addCarNumber.setText("");
                alertDialog.dismiss();
            }
        }).create();

        noCarDialog = new AlertDialog.Builder(UserActivity.this).setTitle("失败").setMessage("无此车辆，请重试").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                noCarDialog.dismiss();
            }
        }).create();

        carHasHostDialog = new AlertDialog.Builder(UserActivity.this).setTitle("失败").setMessage("此车辆已被注册，请重试").setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                carHasHostDialog.dismiss();
            }
        }).create();

        carChangeReceiver = new CarChangeReceiver();
        registerReceiver(carChangeReceiver,carChangeIntentFilter);
        carLocationNowReceiver = new CarLocationNowReceiver();
        registerReceiver(carLocationNowReceiver,carNowIntentFilter);
        bindService(new Intent(UserActivity.this,MyService.class),UserActivity.this,BIND_AUTO_CREATE);

        MyApplication myApplication = (MyApplication) this.getApplication();
        user = (User) myApplication.getHashMap().get("user");

        username = (TextView) findViewById(R.id.user);
        username.setText(user.getName());

        carList = (ListView) findViewById(R.id.carlist);

        addCar = (ImageView) findViewById(R.id.addcar);
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });

        quit = (Button) findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myService.quit();
                Intent i = new Intent(UserActivity.this , LoginActivity.class);
                startActivity(i);
                UserActivity.this.finish();
            }
        });

        cars = new ArrayList<>();
        getData(cars);
        adapter = new SimpleAdapter(this,cars,R.layout.car_list,new String[]{"state","carnumber","delete"}, new int[]{R.id.state,R.id.carnumber,R.id.delete}) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                final TextView textView = (TextView) view.findViewById(R.id.carnumber);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextView t = (TextView)v;
//                        Toast.makeText(getApplicationContext(),t.getText().toString(),Toast.LENGTH_SHORT).show();

                        myService.serachNowCarLocation(t.getText().toString());
                    }
                });

                ImageView imageView = (ImageView) view.findViewById(R.id.delete);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myService.carChange(textView.getText().toString().trim());
                        //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                        progressDialog = ProgressDialog.show(UserActivity.this, null, "请稍后...", false, true);
                    }
                });

                return view;
            }
        };
        carList.setAdapter(adapter);
    }

    private void getData(ArrayList<Map<String,Object>> cars) {
        Map<String, Object> map;
        cars.clear();

        Iterator<Car> iterator = user.getCars().iterator();
        Car car;
        while(iterator.hasNext()) {
            car = iterator.next();
            Log.v("debug",car.getNumber());

            map = new HashMap<String, Object>();
            if(car.getState().equals("NO")) {
                map.put("state",R.mipmap.normal);
            } else if(car.getState().equals("YES")) {
                map.put("state",R.mipmap.alert);
            }

            map.put("carnumber",car.getNumber());

            map.put("delete",R.mipmap.delete);

            cars.add(map);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(binder != null) {
            unbindService(UserActivity.this);
            myService = null;
            binder = null;
            unregisterReceiver(carLocationNowReceiver);
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

    public class CarLocationNowReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string;
            string = intent.getStringExtra("nowcarlocation");
            if(string.equals("error")) {

            } else {
                Intent i = new Intent(UserActivity.this, MapActivity.class);
                i.putExtra("carlocation","now" + string);
                startActivity(i);
            }
        }
    }

    public class CarChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String string;
            string = intent.getStringExtra("carchange");
            if(string.equals("error_nocar")) {
                progressDialog.dismiss();
                //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                noCarDialog.show();
            } else if (string.equals("error_carhashost")) {
                progressDialog.dismiss();
                //这里的话参数依次为,上下文,标题,内容,是否显示进度,是否可以用取消按钮关闭
                carHasHostDialog.show();
            } else {
                progressDialog.dismiss();
                MyApplication myApplication = (MyApplication) UserActivity.this.getApplication();
                user = (User) myApplication.getHashMap().get("user");
                getData(cars);
                adapter.notifyDataSetChanged();
            }
        }
    }
}
