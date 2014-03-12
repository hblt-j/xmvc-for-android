package xt.crm.mobi.o.util;
import xt.crm.mobi.c.base.Ctrler;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class UpdateConfig {
	private static final String TAG = "Config";
	
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}

	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;

	}

	public static String getAppName(Context context) {
		Ctrler ctrler = Ctrler.getInstance(context);
		String app_name=ctrler.getSystemProperty("app_name");
		String verName = app_name/*context.getResources().getText(R.string.app_name)*/
				.toString();
		return verName;
	}
}
