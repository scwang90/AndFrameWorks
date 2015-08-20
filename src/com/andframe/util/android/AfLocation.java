package com.andframe.util.android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.andframe.exception.AfToastException;
import com.andframe.helper.java.AfTimeSpan;


public class AfLocation
{
    private static LocationManager mManager = null;
    
    private static List<String> ltDisableProvider = new ArrayList<String>();

    private static String mProvider = null;
    
    private static Date mlastdate = new Date(0);
    private static Location mLocation = null;

    /**
     * 定位获取 Location
     * @param context
     * @return Location
     * @throws WaitException 等待通知
     * @throws Exception 错误提示
     */
    public static Location getLocation(Context context) throws WaitException, Exception
    {
        //实例化一个LocationManager对象  
        if(mManager==null){
            mManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        }
        
        //Provider的类型  
        if(mProvider == null){
            //使用标准集合，让系统自动选择可用的最佳位置提供器，提供位置
            Criteria tCriteria = new Criteria();
            tCriteria.setAccuracy(Criteria.ACCURACY_FINE);   //高精度  
            tCriteria.setAltitudeRequired(false);    //不要求海拔  
            tCriteria.setBearingRequired(false); //不要求方位  
            tCriteria.setCostAllowed(false); //不允许有话费  
            tCriteria.setPowerRequirement(Criteria.POWER_LOW);   //低功耗 
            
            mProvider = mManager.getBestProvider(tCriteria,true);
            mProvider = mProvider == null?LocationManager.NETWORK_PROVIDER:mProvider;
        }
        /* 
         * 第二个参数表示更新的周期，单位为毫秒；第三个参数的含义表示最小距离间隔，单位是米 
         * 设定每30秒进行一次自动定位 
         */
        mManager.requestLocationUpdates(mProvider, 30000, 50,mListener);

        //通过最后一次的地理位置来获得Location对象  
        mLocation = mManager.getLastKnownLocation(mProvider);
        if(mLocation == null){
            if(ltDisableProvider.size() >= 2){
                throw new AfToastException("定位不可用，请设置GPS，或者查看是否被管理软件限制定位。");
            }
            throw new WaitException("等待一下再试");
        }else{
            if(AfTimeSpan.FromDate(mlastdate, new Date()).getTotalMinutes() > 30){
                mLocation = null;
                mlastdate = new Date(0);
                throw new WaitException("等待一下再试");
            }
        }
        
        return mLocation;
    }

    /**
     * 根据 Location 获取 地址信息
     * @param location
     * @param context
     * @return Address 列表
     * @throws IOException 网络异常
     */
    public static List<Address> getAddressByLocation(Location location,Context context) throws IOException
    {
        //获取经纬度
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        
        //此对象能通过经纬度来获取相应的城市等信息  
        Geocoder tGeocoder = new Geocoder(context,Locale.CHINA);

        //解析经纬度 
        List<Address> ltAdress = tGeocoder.getFromLocation(lat, lng, 1); 
        
        return ltAdress;
    }

    /**
     * 根据 Adress 获取 城市名称信息
     * @param ltAdress Adress列表
     */
    public static String getCityNameByAddress(List<Address> ltAdress)
    {
        String tCityName = "";

        //此对象能通过经纬度来获取相应的城市等信息  
        if (ltAdress != null && ltAdress.size() > 0)
        {
            for (Address address : ltAdress)
            {
                tCityName += address.getLocality();
            }
        }
        return tCityName;
    }
    /**
     * 通过经纬度获取地址信息的另一种方法
     * @param latitude
     * @param longitude
     * @return 城市名
     */
    public static String GetCityNameByTude(Location location)
    {
        String addr = "";
        /* 
         * 也可以是http://maps.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s，不过解析出来的是英文地址 
         * 密钥可以随便写一个key=abc 
         * output=csv,也可以是xml或json，不过使用csv返回的数据最简洁方便解析     
         */
        String url = String.format(
                "http://ditu.google.cn/maps/geo?output=csv&key=abcdef&q=%s,%s",
                String.valueOf(location.getLatitude()), 
                String.valueOf(location.getLongitude()));
        URL myURL = null;
        URLConnection httpsConn = null;
        try
        {

            myURL = new URL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            return null;
        }

        try
        {

            httpsConn = (URLConnection) myURL.openConnection();

            if (httpsConn != null)
            {
                InputStreamReader insr = new InputStreamReader(
                        httpsConn.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(insr);
                String data = null;
                if ((data = br.readLine()) != null)
                {
                    String[] retList = data.split(",");
                    if (retList.length > 2 && ("200".equals(retList[0])))
                    {
                        addr = retList[2];
                    }
                    else
                    {
                        addr = "";
                    }
                }
                insr.close();
            }
        }
        catch (IOException e)
        {

            e.printStackTrace();
            return null;
        }
        return addr;
    }

    /**
     * 方位改变时触发，进行调用
     */
    private final static LocationListener mListener = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            mLocation = location;
            mlastdate = new Date();
            //移除监听器，在只有一个widget的时候，这个还是适用的  
            mManager.removeUpdates(mListener);
        }

        public void onProviderDisabled(String provider)
        {
            Boolean isHasDisable = false;
            for (int i = 0 ; i < ltDisableProvider.size() ; i++)
            {
                String tProvider = ltDisableProvider.get(i);
                if(tProvider.endsWith(provider)){
                    isHasDisable = true;
                }
            }
            if(isHasDisable == false){
                ltDisableProvider.add(provider);
            }
            this.setBestProvider();
        }


        @Override
        public void onProviderEnabled(String provider)
        {
            for (int i = 0 ; i < ltDisableProvider.size() ; i++)
            {
                String tProvider = ltDisableProvider.get(i);
                if(tProvider.endsWith(provider)){
                    ltDisableProvider.remove(i);
                    break;
                }
            }
            this.setBestProvider();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            
        }

        private void setBestProvider()
        {
            Boolean isDisableNet = false;
            Boolean isDisableGps = false;
            for (int i = 0 ; i < ltDisableProvider.size() ; i++)
            {
                String tProvider = ltDisableProvider.get(i);
                if(tProvider.endsWith(LocationManager.NETWORK_PROVIDER)){
                    isDisableNet = true;
                }
                if(tProvider.endsWith(LocationManager.GPS_PROVIDER)){
                    isDisableGps = true;
                }
            }
            if(isDisableNet == false){
                mProvider = LocationManager.NETWORK_PROVIDER;
            }else if(isDisableGps == false){
                mProvider = LocationManager.GPS_PROVIDER;
            }else{
                mProvider = null;
            }
        }
    };

    /**
     * 请求等待 异常
     * @author 树朾
     *  如果接收到异常 处理方法是 稍等片刻再调用一次接口
     */
    public static class WaitException extends Throwable{

        private static final long serialVersionUID = 1L;
        
        private String Message;
        
        public WaitException(String msg){
            Message = msg;
        }

        public String getMessage()
        {
            return Message;
        }
        
    }

}
