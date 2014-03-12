package xt.crm.mobi.upapk.c.action;

import org.json.JSONObject;

import xt.crm.mobi.c.base.BaseAction;
import xt.crm.mobi.upapk.s.VersionService;


public class doCheckVersion extends BaseAction{
	/*NO. 1
	 * 因反射调用现式构造方法必须有
	 */
	public doCheckVersion() {}
	
	
	
	/**NO. 2
	 * 执行动作
	 * 此方法本身在线程中运行，
	 * 可据需求再添加线程处理 
	 */
	public void actionRun(Object... parmobj){
		JSONObject jo=VersionService.chkVer(ctrler.getCurrentActivity());
		ctrler.returnResult(jo);
	}


}

