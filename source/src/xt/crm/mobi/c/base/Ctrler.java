package xt.crm.mobi.c.base;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
/**
 * 通用控制器
 * 在ACTIVITY 和 ACTION间调用
 * <br>文件名称:Ctrler.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2012-10-8    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2012-10-8 下午01:06:51
 */
@SuppressLint({ "WorldWriteableFiles", "WorldReadableFiles", "HandlerLeak" }) public class Ctrler {
	public static Activity currentActivity;
	public static Context currentContext;
	public SharedPreferences sp=null;
	public Properties configProerties;
    public static final int BASE_ERROR = 90;
	public static final int BASE_WARN = 91;
	public static final int ACTION_RUN = 92;
	public static final int ACTION_END = 93;
	public static final int ACTION_EMPTY = 94;
    public Handler handler;// 通用handler系统消息机制
    public Handler mHandler;//后台任务数据更新机制
	//public ClientContext context;  //废除ClientContext　BY j 2012-12-14
	//private Context contexts;
	// 单例实现
	private static Ctrler ctrler;

	private Ctrler(Activity act) {
		currentActivity = act;
		currentContext = act;
		//据消息类型作控制处理消息加type 
		//自动判断此对象或方法 
		//或是基状态配置表处理
		
		//为减少改动工作量，兼容旧版context //废除ClientContext　BY j 2012-12-14
		//sp=currentActivity.getSharedPreferences("UserInfo", Context.MODE_WORLD_WRITEABLE| Context.MODE_WORLD_READABLE);
		//context = new ClientContext();
		// 创建客户端配置参数对象
		//context.setConfigProperties(getConfigProperties(),getSharePreferences());
		setConfigProperties(getConfigProperties(),getSharePreferences());
	}
	private Ctrler(Context act) {
		currentContext= act;
		//据消息类型作控制处理消息加type 
		//自动判断此对象或方法 
		//或是基状态配置表处理
		
		//为减少改动工作量，兼容旧版context //废除ClientContext　BY j 2012-12-14
		//sp=currentContext.getSharedPreferences("UserInfo", Context.MODE_WORLD_WRITEABLE| Context.MODE_WORLD_READABLE);
		//context = new ClientContext();
		// 创建客户端配置参数对象
		//context.setConfigProperties(getConfigProperties(),getSharePreferences());
		setConfigProperties(getConfigProperties(),getSharePreferences());
	}
	public synchronized static Ctrler getInstance(Context act) {
		if (ctrler == null) {
			ctrler = new Ctrler(act);
		}
		return ctrler;
	}
	public synchronized static Ctrler getInstance(Activity act) {
		if (ctrler == null) {
			ctrler = new Ctrler(act);
		}
		return ctrler;
	}
	/**
	 * 通用消息处理即只处理系统框架级消息
	 * 后台线程任务返回数据更新消息需另行创建,自己处理
	 * 
	 * @Title setHandler 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void setHandler() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);  
				switch (msg.what) {
				case BASE_ERROR:
					
					break;
				case BASE_WARN:
					
					break;
				case ACTION_RUN:
					
					break;
				case ACTION_END:
					
					break;
				case ACTION_EMPTY:
					
					break;

				default:
					break;
				}
				
				// ...
			}
		};
	}

	/**
	 * 发送系统消息
	 * 
	 * @Title sendMsg 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void sendMsg(int msg) {
		handler.sendEmptyMessage(msg);
	}
	/**
	 * 消息返回后台异步任务处理结果
	 * 
	 * @Title returnResult 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void returnResult(Object res){
		Message message = mHandler.obtainMessage();  
	    message.obj = res;  
	    //这里这个 arg1 是Message对象携带的参数我主要用它来区分消息对象(Message)  
	    //message.arg1 = 2;  
	    //把消息发送给目标对象，目标对象就是 myHandler 就是关联我们得到的那个消息对象的Handler  
	    message.sendToTarget();
	}
	/**
	 *  跳转
	 * 
	 * @Title jumpTo 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void jumpTo(Class<?> c) {
		Intent intent = new Intent(currentActivity, c);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		currentActivity.startActivity(intent);
	}

	/**
	 * 带参跳转
	 * 
	 * @Title dropTo 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void dropTo(Class<?> next, Serializable parm) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("parm", parm);
		intent.putExtras(bundle);
		intent.setClass(currentActivity, next);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		currentActivity.startActivity(intent);
	}
	/**
	 *  带参跳转加返回
	 * 
	 * @Title dropToForResult 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void dropToForResult(Class<?> next, Serializable parm,int codeFlag) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putSerializable("parm", parm);
		intent.putExtras(bundle);
		intent.setClass(currentActivity, next);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		currentActivity.startActivityForResult(intent,codeFlag);
	}
	/**
	 * 对应dropTo.bundle获取bundleSeriaParm
	 * 此方法仅限于继承于BaseActivity类使用,且在onResume()currentActivity有值后才能正常，不支持tabactivity,原因可能是intent.content问题
	 * @Title getBundleSeriaParm 
	 * @param  name 参数
	 * @return valobj
	 * @throws
	 */
	public Object getBundleSeriaParm() {
		Object valobj = null;
		Bundle bundle = currentActivity.getIntent().getExtras();
		if (bundle != null) {
			valobj =bundle.getSerializable("parm");// 读出数据
		}
		return valobj;
	}
	/**
	 * 处理动作
	 *  据完整类名IOC反射调用执行其EXCUTE方法
	 *  现无需完整类名，据actionActivityMap找，
	 *  找不到则默认指向空action:xt.crm.mobi.c.base.BaseAction
	 * 
	 * @Title doAction 
	 * @param actionName 
	 * @return void返回类型
	 * @throws
	 */
	public void doAction(String actionName,Object... args){
		//String action=actionActivityMap.get(actionName);
		String pakage=currentContext.getPackageName();
		StringBuffer action=new StringBuffer();
		action.append(pakage+".");//"xt.crm.mobi.c.";
		action.append(actionName);
		Class<?> c = null;
		try {
			c = Class.forName(action.toString());
		} catch (ClassNotFoundException e) {
			try {
				c = Class.forName("xt.crm.mobi."+actionName);
			} catch (ClassNotFoundException e1) {
				try {
					c = Class.forName("xt.crm.mobi.c.base.BaseAction");
				} catch (ClassNotFoundException e2) {
					e1.printStackTrace();
				}
			}
		}
		Method method = null;
		try {
			method = c.getMethod("excute",Object[].class);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		try {
				method.invoke(c.newInstance(),(Object)args);
				/*Class[] argsClass = new Class[1];
				argsClass[0]=currentActivity.getClass();
				Constructor cons=c.getConstructor(argsClass);
				method.invoke(cons.newInstance(currentActivity),args);
				*/			
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	public Activity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(Activity currentActivity) {
		Ctrler.currentActivity = currentActivity;
	}
	// 清除配置
	public void cleanSP() {
		sp = currentContext.getSharedPreferences("UserInfo",Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		sp.edit().clear().commit();
	}
	// 该方法用来得到客户端配置参数
	 
	private Properties getConfigProperties() {
		Properties pro=new Properties();
		AssetManager am=currentContext.getAssets();
		try {
			InputStream is=am.open("client_config.properties");
			pro.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pro;
	}

	private SharedPreferences getSharePreferences() {
		SharedPreferences sp;
		if (currentContext == null) {
			sp = currentActivity.getSharedPreferences("UserInfo",
					Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		} else {
			sp = currentContext.getSharedPreferences("UserInfo",
					Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);
		}

		return sp;
	}

	/**
	 * 设置客户端配置参数属性集
	 * 
	 * @throws IOException
	 */
	private void setConfigProperties(Properties pro,SharedPreferences sp) {

		this.configProerties = pro;
		this.sp = sp;

	}
   //获取客户端配置数据
	public String getSystemProperty(String name) {
		return  configProerties.getProperty(name);
	}
	//发送广播通知所有窗体关闭
	public void exitApp()
	{	Intent intent = new Intent();
	    intent.setAction("ExitApp");
	    Ctrler.currentActivity.sendBroadcast(intent);
	    //Log.d("setExitApp", "ok");
	    //Ctrler.currentActivity.finish();
	    //退出后台线程,以及销毁静态变量  
	    //System.exit(1); 
	    Intent intent2 = new Intent(Intent.ACTION_MAIN);  
        intent2.addCategory(Intent.CATEGORY_HOME);  
        intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
        Ctrler.currentActivity.startActivity(intent2);  
        
        android.os.Process.killProcess(android.os.Process.myPid()); 
	}
	/**
	 * 简单提示
	 * eg:ctrler.showMessage("Version",  "Version: " + Util.CURRENT_OPENAPI_VERSION, null);
	 * @param 
	 * @return void
	 * @throws 
	 * 2013-6-18 上午11:49:13
	 */
	public void showMessage(String title, String message, DialogInterface.OnClickListener positiveListener)	{
		new AlertDialog.Builder(currentActivity)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", positiveListener)
		.create()
		.show();
	}
	/**
	 * 自定义view提示
	 * eg:
	 * final TextView textView = new TextView(activity);
		textView.setAutoLinkMask(1);
		textView.setText(Html.fromHtml("1. Download: " + "<a href=\'" + uri
				+ "\'" + ">" + uri + "</a>" + "<br />" + "2. Install<br />" + "(Warning: Test Only. Please don't publish this apk or link to others.)"));
		textView.setTextSize(18);
		ctrler.showViewMessage("Version", textView, null);
	 * @param 
	 * @return void
	 * @throws 
	 * 2013-6-18 上午11:49:13
	 */
	public void showViewMessage(String title, View view, DialogInterface.OnClickListener positiveListener)	{
		new AlertDialog.Builder(currentActivity)
		.setTitle(title)
		.setView(view)
		.setPositiveButton("Ok", positiveListener)
		.create()
		.show();
	}
}
