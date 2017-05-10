package com.cjy.safecar.map;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.cjy.safecar.application.MyApplication;
import com.cjy.safecar.map.overlay.DrivingRouteOverlay;
import com.cjy.safecar.map.overlay.WalkRouteOverlay;
import com.example.safecar.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

import user.CarLocation;

public class MapActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener {
    private String carnumber;

    private TextView carTitle;
    private ImageView refresh;
    private Button history;

    private AMap aMap;
    private MapView mMapView = null;
    private UiSettings mUiSettings;//定义一个UiSettings对象

    private LocationSource.OnLocationChangedListener mListener;

    private WalkRouteResult mWalkRouteResult;
    private DriveRouteResult mDriveRouteResult;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        carTitle = (TextView) findViewById(R.id.cartitle);
        refresh = (ImageView) findViewById(R.id.refresh);
        history = (Button) findViewById(R.id.history);

        mapInit(savedInstanceState);

        String string = getIntent().getStringExtra("carlocation");
        getIntent().removeExtra("carlocation");
        Log.v("debug", string);

        if (string.charAt(0) == 'n') {
            carnumber = string.substring(3,string.length());
            drawNow(string);
        } else {
            carnumber = string;
            drawHistory();
        }

        carTitle.setText(carnumber);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapActivity.this, HistorySerachActivity.class);
                i.putExtra("carlocation",carnumber);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mLocationClient.onDestroy();
        mLocationClient = null;
        mLocationOption = null;
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    public void mapInit(Bundle savedInstanceState) {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mMapView.getMap();
            mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        }
        // 设置定位监听
        aMap.setLocationSource(new LocationSource() {
            @Override
            public void activate(OnLocationChangedListener onLocationChangedListener) {
                mListener = onLocationChangedListener;
                if (mLocationClient == null) {
                    //初始化定位
                    mLocationClient = new AMapLocationClient(getApplicationContext());
//设置定位回调监听
                    mLocationClient.setLocationListener(new AMapLocationListener() {
                        @Override
                        public void onLocationChanged(AMapLocation amapLocation) {
                            if (amapLocation != null) {
                                if (amapLocation.getErrorCode() == 0) {
                                    mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                                } else {
                                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                                    Log.e("AmapError", "location Error, ErrCode:"
                                            + amapLocation.getErrorCode() + ", errInfo:"
                                            + amapLocation.getErrorInfo());
                                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });

                    //初始化AMapLocationClientOption对象
                    mLocationOption = new AMapLocationClientOption();
                    //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
                    mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

                    //给定位客户端对象设置定位参数
                    mLocationClient.setLocationOption(mLocationOption);
//启动定位
                    mLocationClient.startLocation();
                }

            }

            @Override
            public void deactivate() {

                mListener = null;
                if (mLocationClient != null) {
                    mLocationClient.stopLocation();
                    mLocationClient.onDestroy();
                }
                mLocationClient = null;
            }
        });

        mUiSettings.setMyLocationButtonEnabled(true); //显示默认的定位按钮
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        aMap.setMyLocationEnabled(true);
// 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        //        LatLng latLng = new LatLng(39.906901,116.397972);
//        MarkerOptions markerOption = new MarkerOptions();
//        markerOption.position(latLng);
//        markerOption.title("西安市").snippet("西安市：34.341568, 108.940174");
//
//        markerOption.draggable(true);//设置Marker可拖动
//        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                .decodeResource(getResources(),R.mipmap.b_poi_1)));
//        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//        markerOption.setFlat(false);//设置marker平贴地图效果
//        aMap.addMarker(markerOption);


//        //初始化定位
//        mLocationClient = new AMapLocationClient(getApplicationContext());
////设置定位回调监听
//        mLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation amapLocation) {
//                if (amapLocation != null) {
//                    if (amapLocation.getErrorCode() == 0) {
////                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
////                        amapLocation.getLatitude();//获取纬度
////                        amapLocation.getLongitude();//获取经度
////                        amapLocation.getAccuracy();//获取精度信息
////                        amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
////                        amapLocation.getCountry();//国家信息
////                        amapLocation.getProvince();//省信息
////                        amapLocation.getCity();//城市信息
////                        amapLocation.getDistrict();//城区信息
////                        amapLocation.getStreet();//街道信息
////                        amapLocation.getStreetNum();//街道门牌号信息
////                        amapLocation.getCityCode();//城市编码
////                        amapLocation.getAdCode();//地区编码
////                        amapLocation.getAoiName();//获取当前定位点的AOI信息
////                        amapLocation.getBuildingId();//获取当前室内定位的建筑物Id
////                        amapLocation.getFloor();//获取当前室内定位的楼层
////                        amapLocation.getGpsAccuracyStatus();//获取GPS的当前状态
//
//                        // 获取定位时间
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(amapLocation.getTime());
//                        df.format(date);
//
//                        // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
//                        if (isFirstLoc) {
//                            //设置缩放级别
//                            aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
//                            //将地图移动到定位点
//                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
//                            //添加图钉
//                            //aMap.addMarker(getMarkerOptions(amapLocation));
//                            //获取定位信息
//                            StringBuffer buffer = new StringBuffer();
//                            buffer.append(amapLocation.getCountry() + ""
//                                    + amapLocation.getProvince() + ""
//                                    + amapLocation.getCity() + ""
//                                    + amapLocation.getProvince() + ""
//                                    + amapLocation.getDistrict() + ""
//                                    + amapLocation.getStreet() + ""
//                                    + amapLocation.getStreetNum());
//                            Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
//                            isFirstLoc = false;
//                        }
//                    } else {
//                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
//                        Log.e("AmapError", "location Error, ErrCode:"
//                                + amapLocation.getErrorCode() + ", errInfo:"
//                                + amapLocation.getErrorInfo());
//                        Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//
//        //初始化AMapLocationClientOption对象
//        mLocationOption = new AMapLocationClientOption();
//        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//
//        //给定位客户端对象设置定位参数
//        mLocationClient.setLocationOption(mLocationOption);
////启动定位
//        mLocationClient.startLocation();
    }

    public void drawNow(String string) {
        MyApplication myApplication = (MyApplication) getApplication();
        CarLocation nowCarLocation = (CarLocation) myApplication.getHashMap().get(string);

        MarkerOptions markerOption = new MarkerOptions();
        markerOption.position(new LatLng(new Double(nowCarLocation.getLatitude()), new Double(nowCarLocation.getLongitude())));
        GregorianCalendar date = (GregorianCalendar) nowCarLocation.getDate();
        markerOption.title(nowCarLocation.getState()).snippet(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) + " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND));

        markerOption.draggable(true);//设置Marker可拖动

        markerOption.setFlat(false);//设置marker平贴地图效果

        if (nowCarLocation.getState().equals("NO")) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.navi_along_search_default)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        } else if (nowCarLocation.getState().equals("YES")) {
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                    .decodeResource(getResources(), R.mipmap.b_poi)));
            // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        }

        aMap.addMarker(markerOption);
    }

    public void drawHistory() {
        Log.v("debug", "drawHistory");
        MyApplication myApplication = (MyApplication) getApplication();
        LinkedList<CarLocation> carLocations = (LinkedList<CarLocation>) myApplication.getHashMap().get(carnumber);
        Iterator<CarLocation> iterator = carLocations.iterator();
        CarLocation carLocation;

        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.WalkRouteQuery walkquery = null;
        RouteSearch.DriveRouteQuery drivequery = null;
        RouteSearch.FromAndTo fromAndTo = null;
        LatLonPoint start = null;
        LatLonPoint end = null;

        for (int i = carLocations.size(); i > 0; i--) {
            carLocation = iterator.next();
            MarkerOptions markerOption = new MarkerOptions();

            Log.v("debug", new Double(carLocation.getLatitude()) + "" + new Double(carLocation.getLongitude()));

            end = new LatLonPoint(new Double(carLocation.getLatitude()), new Double(carLocation.getLongitude()));
            if (start != null) {
                fromAndTo = new RouteSearch.FromAndTo(start, end);
//                walkquery = new RouteSearch.WalkRouteQuery(fromAndTo,RouteSearch.WALK_DEFAULT);
                drivequery = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_SHORTEST, null, null, "");
                routeSearch.calculateDriveRouteAsyn(drivequery);
            }

            markerOption.position(new LatLng(new Double(carLocation.getLatitude()), new Double(carLocation.getLongitude())));
            GregorianCalendar date = (GregorianCalendar) carLocation.getDate();
            markerOption.title(carLocation.getState()).snippet(date.get(Calendar.YEAR) + "-" + (date.get(Calendar.MONTH) + 1) + "-" + date.get(Calendar.DAY_OF_MONTH) + " " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND));

            markerOption.draggable(true);//设置Marker可拖动

            markerOption.setFlat(false);//设置marker平贴地图效果

            if (carLocation.getState().equals("NO")) {
                if (i <= 10) {
                    try {
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.mipmap.class.getField("b_poi_" + i + "_hl").getInt(null))));
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                } else {
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.blue)));
                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                }
            } else if (carLocation.getState().equals("YES")) {
                if (i <= 10) {
                    try {
                        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                                .decodeResource(getResources(), R.mipmap.class.getField("b_poi_" + i).getInt(null))));
                        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                } else {
                    markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                            .decodeResource(getResources(), R.mipmap.red)));
                    // 将Marker设置为贴地显示，可以双指下拉地图查看效果
                }
            }

            aMap.addMarker(markerOption);

            start = end;
            end = null;
        }

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    DrivingRouteOverlay drivingRouteOverlay = new DrivingRouteOverlay(
                            this.getApplicationContext(), aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();

                } else if (result != null && result.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }

            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult result, int errorCode) {
//        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mWalkRouteResult = result;
                    final WalkPath walkPath = mWalkRouteResult.getPaths()
                            .get(0);
                    WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(
                            this, aMap, walkPath,
                            mWalkRouteResult.getStartPos(),
                            mWalkRouteResult.getTargetPos());
                    walkRouteOverlay.removeFromMap();
                    walkRouteOverlay.addToMap();
                    walkRouteOverlay.zoomToSpan();
                } else if (result != null && result.getPaths() == null) {
//                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
//                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
//            ToastUtil.showerror(this.getApplicationContext(), errorCode);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
