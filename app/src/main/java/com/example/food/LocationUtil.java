package com.example.food;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

public class LocationUtil {
    public OnLocationListenter onLocationListenter;
    public AMapLocationClient aMapLocationClient;

    /**
     * 高德地图参数配置
     *
     * @return
     */
    public AMapLocationClientOption parameterConfiguration() {
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式（采取高精度定位模式）
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置单次定位
        mLocationOption.setOnceLocation(true);
        //获取3s内最精确的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
//        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
//        mLocationOption.setHttpTimeOut(20000);
        return mLocationOption;
    }

    /**
     * 开始定位
     *
     * @return
     */
    public void startLocation(Context context, OnLocationListenter onLocationListenter) {
        this.onLocationListenter = onLocationListenter;
        //初始化定位
        aMapLocationClient = new AMapLocationClient(context);
        //设置定位成功监听
        aMapLocationClient.setLocationOption(parameterConfiguration());
        //成功结果监听
        aMapLocationClient.setLocationListener(aMapLocationListener);
        //检测是否有GPS权限

    }

    /**
     * 定位监听
     */
    public interface OnLocationListenter {
        /**
         * 获取经纬度
         *
         * @param longitude 经度
         * @param latitude  纬度
         */
        void getLatitudeAndLongitude(double longitude, double latitude);
    }

    AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (onLocationListenter != null) {
                aMapLocationClient.onDestroy();
                onLocationListenter.getLatitudeAndLongitude(aMapLocation.getLongitude(), aMapLocation.getLatitude());
            }
        }
    };

}
