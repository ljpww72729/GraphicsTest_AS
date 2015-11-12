package com.kk.lp.wificommunication.client;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtils {
	
	//获取链接的wifi地址,本机IP地址
	public static String getServerIP(Context context){
		WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		if(isWifiEnabled(context)){
			DhcpInfo dhcpInfo = wifiManger.getDhcpInfo();
			return intToInetAddress(dhcpInfo.ipAddress).getHostAddress();
		}
		return null;
	}
	
	
    /**
     * Convert a IPv4 address from an integer to an InetAddress.
     * @param hostAddress an int corresponding to the IPv4 address in network byte order
     */
    public static InetAddress intToInetAddress(int hostAddress) {
        byte[] addressBytes = { (byte)(0xff & hostAddress),
                (byte)(0xff & (hostAddress >> 8)),
                (byte)(0xff & (hostAddress >> 16)),
                (byte)(0xff & (hostAddress >> 24)) };

        try {
            return InetAddress.getByAddress(addressBytes);
        } catch (UnknownHostException e) {
            throw new AssertionError();
        }
    }
    
    public static boolean isWifiEnabled(Context context){
    	WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	if(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
    		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    		return netInfo.isConnected();
    	}else{
    		return false;
    	}
    }
    
    /** 
    * @Description 获取wifi名称
    * @author lipeng
    * @param context
    * @return 
    * @return String 
    */
    public static String getWifiName(Context context){
		WifiManager wifiManger = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiManger.getConnectionInfo();
        String wifiId = info != null ? info.getSSID() : null;
        return wifiId;
    }
    
    /** 
    * @Description 获取本机IP地址,只支持局域网内
    * @author lipeng
    * @return 
    * @return String 
    */
    public static String getLocalIpAddress(){
		try {
    		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
    			NetworkInterface intf = en.nextElement();
    			for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();	enumIpAddr.hasMoreElements();){
    				InetAddress inetAddress = enumIpAddr.nextElement();  
    				String mIP = inetAddress.getHostAddress().substring(0, 3); 
    				if(mIP.equals("192")){
    					  return inetAddress.getHostAddress();  
    				}
    			}
    		}
    	}catch (SocketException e){
    		e.printStackTrace();
    	}
		return null;
	}
    
}
