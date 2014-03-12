package xt.crm.mobi.upapk.s;

import org.json.JSONObject;

import xt.crm.mobi.c.base.Ctrler;
import xt.crm.mobi.o.util.WifiUtil;
import xt.crm.mobi.upapk.m.remoapi.RemoAPI;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;



public class VersionService {
	
/**
 * 检测新版本
 * 
 * @Title chkVer 
 * @param  name 参数
 * @return int返回类型
 * @throws
 */
	public static JSONObject chkVer(final Context context) {
				JSONObject o=null;	
		if (WifiUtil.HttpTest(context).equals("ok")) {
					RemoAPI remoapi = new RemoAPI();
						try {
							o=  remoapi.getVerInfo();
						} catch (Exception e) {
							e.printStackTrace();
						}
		}
		return o;
	}
	/**
	 * downapk
	 * @param context
	 * @param handler
	 */
	public static void downapk(final Ctrler ctrler,Handler handler){
		try {
			RemoAPI remoapi = new RemoAPI();
			String url = ctrler.getSystemProperty("downloadNewVersion");
			String savepath = Environment.getExternalStorageDirectory()+ ctrler.getSystemProperty("apkpath");
			remoapi.downFile(url, savepath, ctrler.getSystemProperty("apkname"), handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
