package xt.crm.mobi.c.base;

import android.app.Activity;
/**
 * action基类
 * <br>文件名称:BaseAction.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2012-10-8    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2012-10-8 下午01:01:41
 */
public class BaseAction {
	public Ctrler ctrler;
	public BaseAction(){
		ctrler=Ctrler.getInstance(null);
		//Log.d("CurrentActivity0", ctrler.getCurrentActivity().toString());
	}
	@Deprecated
	public BaseAction(Activity act){
		ctrler=Ctrler.getInstance(act);
	}
	/**
	 * 统一执行动作入口方法，自动创建线程
	 * （用于框架调用）
	 * @Title excute 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void excute(final Object... parmobj) {
		if(ctrler.handler==null)ctrler.setHandler();
		new Thread(){
			public void run(){
				ctrler.sendMsg(Ctrler.ACTION_RUN);        
				actionRun(parmobj);
				ctrler.sendMsg(Ctrler.ACTION_END);  
			}
		}.start();
	}
	/**
	 * 实际执行动作
	 * （用于继承覆写）
	 * @Title actionRun 
	 * @param  name 参数
	 * @return void返回类型
	 * @throws
	 */
	public void actionRun(Object... parmobj){
		//防错误请求　空方法　无动作　
		//或提示无效或错误请求
		ctrler.sendMsg(Ctrler.ACTION_EMPTY);
	}
}
