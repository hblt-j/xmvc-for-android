package xt.crm.mobi.o.util;

import java.net.URLEncoder;

import xt.crm.mobi.c.base.Ctrler;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class PhoneInfo {
	
	public static String getPhoneInfo(Context context){
		String phoneInfo="";
		Ctrler ctrler = Ctrler.getInstance(context);
		String app_name =ctrler.getSystemProperty("app_name");
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			if(WifiUtil.HttpTest(context).equals("ok")){
				phoneInfo ="&os=Android&os_ver="+URLEncoder.encode(android.os.Build.VERSION.RELEASE)+"&model="+URLEncoder.encode(android.os.Build.MODEL)
				+"&apn="+URLEncoder.encode(getAPN(context))+"&imei="+URLEncoder.encode(tm.getDeviceId())+"&app="+URLEncoder.encode(app_name/*context.getString(R.string.app_name)*/)
				+"&app_ver="+URLEncoder.encode(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)+"&memo=Operator"+URLEncoder.encode(tm.getNetworkOperatorName());

			}else{
				phoneInfo ="&os=Android&os_ver="+URLEncoder.encode(android.os.Build.VERSION.RELEASE)+"&model="+URLEncoder.encode(android.os.Build.MODEL)
				+"&imei="+tm.getDeviceId()+"&app="+URLEncoder.encode(app_name/*context.getString(R.string.app_name)*/)
				+"&app_ver="+URLEncoder.encode(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)+"&memo=Operator"+URLEncoder.encode(tm.getNetworkOperatorName());
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return phoneInfo;
	}
	public static String getAPN(Context context) {
		String apn = "";
		// 通过context得到ConnectivityManager连接管理
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 通过ConnectivityManager得到NetworkInfo网络信息
		NetworkInfo info = manager.getActiveNetworkInfo();
		// 获取NetworkInfo中的apn信息
			apn = info.getExtraInfo();
		return apn+"";
	}
	public static String getIMEI(Context context){
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getDeviceId()+"";
	}
}
