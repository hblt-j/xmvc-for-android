package xt.crm.mobi.login.z.broadcast;
import xt.crm.mobi.c.base.Ctrler;
import xt.crm.mobi.o.util.HttpUtil;
import xt.crm.mobi.o.util.PhoneInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActivaReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
		Ctrler ctrler=Ctrler.getInstance(context);
		try {
			String phoneInfo = PhoneInfo.getPhoneInfo(context)+ "&x=" + ctrler.sp.getInt("x", 0) + "&y="+ ctrler.sp.getInt("y", 0);
			String str = "market="+ctrler.getSystemProperty("market") + phoneInfo + "&pt=1";
			HttpUtil.post1(ctrler.getSystemProperty("activa"), str, 30000, 30000, 30000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}