package xt.crm.mobi.login.c.action;

import org.json.JSONException;
import org.json.JSONObject;

import xt.crm.mobi.c.base.BaseAction;
import xt.crm.mobi.login.s.loginService;
import android.os.Message;

/**
 * 登录动作
 * <br>文件名称:doLogin.java<br>
 * <br>内容摘要:<br>
 * <br>修改日期       修改人员   版本	   修改内容 <br>
 * <br>2012-10-12    j     0.1    0.1 新建<br>
 * @author:   j 
 * @version:  0.1  
 * @Date:     2012-10-12 下午05:58:24
 */
public class doLogin extends BaseAction {
	/*NO. 1
	 * 因反射调用现式构造方法必须有
	 */
	public doLogin() {}

	
	/**NO. 2
	 * 执行动作
	 * 此方法本身在线程中运行，
	 * 可据需求再添加线程处理
	 */
	public void actionRun(Object... parmobj){
		try {
			String result =	loginService.login(ctrler.getSystemProperty("login"),ctrler.getSystemProperty("market"),parmobj);
			JSONObject res = new JSONObject(result);	
			Message msg = ctrler.handler.obtainMessage();
			msg.obj = res;
			msg.sendToTarget();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
