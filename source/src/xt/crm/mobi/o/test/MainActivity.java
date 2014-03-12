package xt.crm.mobi.o.test;

import java.io.Serializable;

import xt.crm.mobi.c.base.BaseActivity;
import xt.mobi.jar.sources.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 初次进入主页面
 * <br>文件名称:MainActivity.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2012-10-11    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2012-10-11 上午10:38:54
 */
@SuppressLint("HandlerLeak") public class MainActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 界面继承则调用super否则覆写 此方法一般无需覆写　或使用通用界面继承BASEACTIVITY　或不使用　或是覆写为空保持结构
	 */
	 protected void setContent() {
		// 1.继承通用界面
		 super.setContent();
		
		// TODO BEGIN...
		// 2.添加使用界面和事件
		setContentView(R.layout.activity_test);

		final TextView t1 =(TextView) findViewById(R.id.textView1);
		Button but1 = (Button) findViewById(R.id.button1);
		but1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ctrler.jumpTo(daoTestActivity.class);
			}
		});
		Button but2 = (Button) findViewById(R.id.button2);
		but2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Serializable parm="我是bundle parm";
				ctrler.dropTo(daoTestActivity.class, parm);//ctrler.dropToForResult(DownAndUpActivity.class, parm, 1);
			}
		});
		Button but3 = (Button) findViewById(R.id.button3);
		but3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//ioc支持可变参，下为无参调用案例
				//ctrler.doAction("cti.action.doLogin");
				//下为有参两种调用案例写法，效果一致
				ctrler.doAction("cti.action.doLogin","a","b",123);
				//ctrler.doAction("cti.action.doLogin",new Object[]{"a","b",123});

				/*推荐示例实现：
				  user.setUserName(); 
				  user.setcominput();
					...
				String phoneInfo = PhoneInfo.getPhoneInfo(LoginActivity.this)+"&x="+ display.getWidth()+"&y="+display.getHeight();
				Object[] parmObj =new Object[2];
				parmObj[0]=user;
				parmObj[1]=phonInfo;
				ctrler.doAction("doLogin",parmObj);
				*/
				
				//兼容方法二：轻量方法实现
				//dologin();
				
				//兼容方法三：简单方法直接写
				//Display display = getWindowManager().getDefaultDisplay();
				//String phoneInfo = PhoneInfo.getPhoneInfo(LoginActivity.this)+"&x="+ display.getWidth()+"&y="+display.getHeight();
				//ctrler.login(username.getText().toString(),cominput.getText().toString(),MD.getMD5Str(pass.getText().toString()),phoneInfo);
			}
		});
		
		//3.异步任务结果处理消息实例化
		ctrler.mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);  
				t1.setText(msg.obj.toString());
				Toast.makeText(ctrler.getCurrentActivity(), msg.obj.toString(), Toast.LENGTH_SHORT).show();
				// ...
			}
		};
	 }
	/* private void dologin(){//方法二
		 new Thread(){
				public void run(){
					ctrler.sendMsg(Ctrler.ACTION_RUN);  
					
					//actionRun。。。
					ctrler.returnResult("我是线程任务里的返回结果");
					
					ctrler.sendMsg(Ctrler.ACTION_END);  
				}
			}.start();
	 }*/
	 
	 
	 
	/*@Deprecated
	 //实验验证base继承自动切换，无需现式切换
	protected void onResume() {
		super.onResume();
		ctrler.setCurrentActivity(MainActivity.this);
		Log.d("testCurrentActivity1", ctrler.getCurrentActivity().toString());
	}*/

	 
}
