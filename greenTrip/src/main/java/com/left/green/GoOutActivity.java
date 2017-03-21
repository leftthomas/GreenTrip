package com.left.green;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;

public class GoOutActivity extends Activity implements LocationSource, AMapLocationListener {
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private TextView city;
    private String cityid;
    private ImageView go;
    private double lat;
    private double lon;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_go);
        mapView = (MapView) findViewById(R.id.map);
        city=(TextView) findViewById(R.id.city);
        go=(ImageView) findViewById(R.id.go);
        mapView.onCreate(savedInstanceState);
        init();
        go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(GoOutActivity.this, SearchActivity.class);
				intent.putExtra("city", cityid);
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				startActivity(intent);
			}
		});
    }
 
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }
    }
 
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }
 
    /**
     * 此方法需存在
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
 
    /**
     * 此方法需存在
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }
 
    /**
     * 此方法需存在
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
 
    /**
     * 此方法已经废弃
     */
    @Override
    public void onLocationChanged(Location location) {
    }
 
    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                city.setText(amapLocation.getCity());
                lat=amapLocation.getLatitude();
                lon=amapLocation.getLongitude();
                cityid=amapLocation.getCityCode();
            }
        }
    }
 
    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(this);
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法     
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60*1000, 10, this);
        }
    }
 
 
    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

	@Override
	public void onProviderDisabled(String arg0) {
	
	}

	@Override
	public void onProviderEnabled(String arg0) {
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		
	}
}