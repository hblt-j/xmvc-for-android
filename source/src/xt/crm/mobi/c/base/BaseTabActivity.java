package xt.crm.mobi.c.base;

import xt.crm.mobi.c.base.BaseTabActivity;
import xt.crm.mobi.c.base.Ctrler;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.Toast;

/**
 * 基础通用模式封装 如在各生命周期都要做的事；通用组件封装；集成控制器；组织结构模型等
 * <br>文件名称:BaseActivity.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2012-10-8    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2012-10-8 下午12:57:52
 */
public class BaseTabActivity extends TabActivity {
	public Ctrler ctrler;// 集成控制器
	public int ISSTARTEXIT=0;
	long exitTime=0;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver(){  
    	//广播的内部类，当收到关闭事件时，调用finish方法结束activity  
        @Override  
        public void onReceive(Context context, Intent intent) {  
        	Log.d("exit", "ok");
        	Ctrler.currentActivity.finish();  
        }  
    };
	
	public BaseTabActivity() {
		// ...
	}
	/**
	 * exit=1时启用 back键再按一次退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(ISSTARTEXIT==1){
		    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){  
		        if((System.currentTimeMillis()-exitTime) > 2000){  
		            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
		            exitTime = System.currentTimeMillis();  
		        } else {
		        	ctrler.exitApp();
		        }
		        return true;  
		    }
		}
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 界面生成结构隔离保持结构清晰
	 * 
	 * @Title setContent 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	protected void setContent() {
		//设置handler通用消息处理
		ctrler.setHandler();
		//通用布局、组件及事件封装

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.tabactivity_base);
		
		// ...
		
	}

	//发送数据请求
	public void inintViewData() {
		//		ctrler.doAction(actionName, args);
	}

	public void setHander() {
		ctrler.handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.obj != null) {
					resumeView(msg.obj);
					setview();
				}
			}
		};
	}
	//初始化view
	public void resumeView(Object o) {
		
	}
	//设置view
	public void setview() {
		
	}
	// =========生命周期通用处理事件============
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//实例化控制器
		ctrler=Ctrler.getInstance(BaseTabActivity.this);
		//Log.d("testCurrentActivity0", ctrler.getCurrentActivity().toString());
		//设置通用组件及事件　
		setContent();
		
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ctrler.setCurrentActivity(BaseTabActivity.this);
		//Log.d("testCurrentActivity00", ctrler.getCurrentActivity().toString());
		inintViewData();
		setHander();
		//Log.d("reg exit","ok");
		//在当前的activity中注册exitApp广播  
        IntentFilter filter = new IntentFilter();  
        filter.addAction("ExitApp");  
        Ctrler.currentActivity.registerReceiver(broadcastReceiver, filter); 
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver); 
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		//ctrler=null;
		/*
		 * if (dbHelper != null && dbHelper.getReadableDatabase().isOpen()) {
		 * dbHelper.close(); }
		 */
		super.onDestroy();
	}

}
