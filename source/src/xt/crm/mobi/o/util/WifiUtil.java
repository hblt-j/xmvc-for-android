package xt.crm.mobi.o.util;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class WifiUtil {
	private static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
	private static String proxyHost = "";
	private static int proxyPort = 0;

	// 网络状态：0-未联网，1-wifi上网，2-通过手机上网(cmnet)，3-通过代理上网(cmwap)
	private static int networkState;

	public static int getNetworkState() {
		return networkState;
	}

	public static String getProxyHost() {
		return proxyHost;
	}

	public static int getProxyPort() {
		return proxyPort;
	}

	/**
	 * 检测网络状态：0-未联网，1-wifi上网，2-通过手机上网(cmnet)，3-通过代理上网(cmwap)
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getNetworkState(Context mContext) {

		return getNetworkStatus(mContext);
	}

	public static int getNetworkStatus(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		WifiManager wm = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		if (wm.getWifiState() == WifiManager.WIFI_STATE_ENABLED
				&& activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			networkState = 1;
		} else {
			NetworkInfo mobNetInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobNetInfo != null
					&& mobNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				getApnProxyInfo(mContext);
				if (proxyPort != 0) {
					networkState = 3;
				} else {
					networkState = 2;
				}
			} else {
				networkState = 0;
			}
		}
		return networkState;
	}

	/*
	 * 取得手机代理上网的信息
	 */
	public static void getApnProxyInfo(Context context) {
		Cursor cProxy = context.getContentResolver().query(PREFERRED_APN_URI,
				null, null, null, null);
		cProxy.moveToFirst();
		if (cProxy != null && cProxy.getCount() >= 1) {
			String proxy = cProxy.getString(cProxy.getColumnIndex("proxy"));
			if (!"".equals(proxy) && proxy != null) {
				proxyHost = cProxy.getString(cProxy.getColumnIndex("proxy"));
				proxyPort = Integer.parseInt(cProxy.getString(cProxy
						.getColumnIndex("port")));
			} else {
				proxyHost = "";
				proxyPort = 0;
			}
		}
		cProxy.close();
	}

	public static boolean isNetworkAvailable(Context mActivity) {
		Context context = mActivity.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static String HttpTest(final Context mActivity) {
		if (!isNetworkAvailable(mActivity)) {
			return "no";
		} else {
			return "ok";
		}
	}
   
	//=========================获取网络类型是否是2G、3G、wap、wifi等===========================
	/** 没有网络 */  
    public static final int NETWORKTYPE_INVALID = 0;  
    /** wap网络 */  
    public static final int NETWORKTYPE_WAP = 1;  
    /** 2G网络 */  
    public static final int NETWORKTYPE_2G = 2;  
    /** 3G和3G以上网络，或统称为快速网络 */  
    public static final int NETWORKTYPE_3G = 3;  
    /** wifi网络 */  
    public static final int NETWORKTYPE_WIFI = 4;  
    
    //判断是否是FastMobileNetWork，将3G或者3G以上的网络称为快速网络
    public static boolean isFastMobileNetwork(Context context) {  
    	TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
        switch (telephonyManager.getNetworkType()) {  
            case TelephonyManager.NETWORK_TYPE_1xRTT:  
                return false; // ~ 50-100 kbps  
            case TelephonyManager.NETWORK_TYPE_CDMA:  
                return false; // ~ 14-64 kbps  
            case TelephonyManager.NETWORK_TYPE_EDGE:  
                return false; // ~ 50-100 kbps  
            case TelephonyManager.NETWORK_TYPE_EVDO_0:  
                return true; // ~ 400-1000 kbps  
            case TelephonyManager.NETWORK_TYPE_EVDO_A:  
                return true; // ~ 600-1400 kbps  
            case TelephonyManager.NETWORK_TYPE_GPRS:  
                return false; // ~ 100 kbps  
            case TelephonyManager.NETWORK_TYPE_HSDPA:  
                return true; // ~ 2-14 Mbps  
            case TelephonyManager.NETWORK_TYPE_HSPA:  
                return true; // ~ 700-1700 kbps  
            case TelephonyManager.NETWORK_TYPE_HSUPA:  
                return true; // ~ 1-23 Mbps  
            case TelephonyManager.NETWORK_TYPE_UMTS:  
                return true; // ~ 400-7000 kbps  
            //case TelephonyManager.NETWORK_TYPE_EHRPD:  
            //    return true; // ~ 1-2 Mbps  
            //case TelephonyManager.NETWORK_TYPE_EVDO_B:  
            //    return true; // ~ 5 Mbps  
            //case TelephonyManager.NETWORK_TYPE_HSPAP:  
            //    return true; // ~ 10-20 Mbps  
            case TelephonyManager.NETWORK_TYPE_IDEN:  
                return false; // ~25 kbps  
            //case TelephonyManager.NETWORK_TYPE_LTE:  
            //    return true; // ~ 10+ Mbps  
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:  
                return false;  
            default:  
                return false;  
            }  
        }  


     //获取网络类型是否是2G、3G、wap、wifi等

    /** 
     * 获取网络状态，wifi,wap,2g,3g. 
     * 
     * @param context 上下文 
     * @return int 网络状态 {@link #NETWORKTYPE_2G},{@link #NETWORKTYPE_3G},          *{@link #NETWORKTYPE_INVALID},{@link #NETWORKTYPE_WAP}* <p>{@link #NETWORKTYPE_WIFI} 
     */  
  
    public static int getNetWorkType(Context context) {  
        int mNetWorkType = 0;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();  
  
		if (networkInfo != null && networkInfo.isConnected()) {  
            String type = networkInfo.getTypeName();  
  
            if (type.equalsIgnoreCase("WIFI")) {  
                mNetWorkType = NETWORKTYPE_WIFI;  
            } else if (type.equalsIgnoreCase("MOBILE")) {  
                String proxyHost = android.net.Proxy.getDefaultHost();  
  
                mNetWorkType = TextUtils.isEmpty(proxyHost)  
                        ? (isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G)  
                        : NETWORKTYPE_WAP;  
            }  
        } else {  
            mNetWorkType = NETWORKTYPE_INVALID;  
        }  
  
        return mNetWorkType;  
    }   
}
