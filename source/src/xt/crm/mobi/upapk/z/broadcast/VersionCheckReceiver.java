package xt.crm.mobi.upapk.z.broadcast;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import xt.crm.mobi.c.base.Ctrler;
import xt.crm.mobi.o.util.UpdateConfig;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 版本检测 广播
 * 
 * @author zb
 * 
 */
public class VersionCheckReceiver extends BroadcastReceiver {
	int  newVerCode;
	String newVerName;
	ProgressDialog pBar;
	Handler handler=new Handler();
	private Ctrler ctrler;
	private int vercode ;

	public void onReceive(final Context context, Intent intent) {
		ctrler = Ctrler.getInstance(null);
		ctrler.mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);  
				if(msg.obj !=null){
					if(msg.obj.getClass().getSimpleName().equals("JSONObject")){
						JSONObject o=(JSONObject) msg.obj;
						
						try {
							newVerCode =Integer.parseInt(o.getString("verCode"));
							vercode = UpdateConfig.getVerCode(ctrler.getCurrentActivity());
							newVerName =o.getString("verName");
						} catch (Exception e) {
							newVerCode = 0;
						}
						if(newVerCode>vercode){
							showUpVerDialog(Ctrler.currentActivity,ctrler);
						}
					}
				}
			}
		};
		ctrler.doAction("upapk.c.action.doCheckVersion");
	}

	/** 
	 * @Title showUpVerDialog 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws 
	 */
	public void showUpVerDialog(final Context context,final Ctrler ctrler) {
		String verName = UpdateConfig.getVerName(ctrler.getCurrentActivity());
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本");
		sb.append(verName+";");
		sb.append("发现新版本:");
		sb.append(newVerName);
		sb.append(", 是否更新?");
		Dialog dialog = new AlertDialog.Builder(context)
				.setTitle("软件更新")
				.setMessage(sb.toString())
				.setPositiveButton("更新", new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							pBar = new ProgressDialog(context);
							pBar.setTitle("正在下载");
							pBar.setMessage("请稍候...");
							pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
							pBar.show();
							ctrler.doAction("upapk.c.action.doDownApk");
							ctrler.handler = new Handler() {
								@Override
								public void handleMessage(Message msg) {
									super.handleMessage(msg);  
									if(msg.obj != null){
										
										try {
											JSONObject j = (JSONObject)msg.obj;
											String type = j.getString("type");
											if(type.equals("df")){
												String stat=j.getString("stat");
												if(stat.length()>0 && stat.equals("end")){
													//安装
													handler.post(new Runnable() {
														public void run() {
															pBar.cancel();
															Intent intent = new Intent(Intent.ACTION_VIEW);
															String apkpath =Environment.getExternalStorageDirectory()+ctrler.getSystemProperty("apkpath");
															intent.setDataAndType(Uri.fromFile(new File(apkpath, ctrler.getSystemProperty("apkname"))),
															"application/vnd.android.package-archive");
															context.startActivity(intent);
														}
													});
												}
											}
										} catch (JSONException e) {
											e.printStackTrace();
										}
									}
								}
							};		
									
							
						}
					})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.cancel();
							}
						}).create();
		dialog.show();
	}


}
