package xt.crm.mobi.login.s;


import xt.crm.mobi.login.m.remoapi.RemoAPI;
import xt.crm.mobi.o.util.MD;


public class loginService {

	// 用户信息验证
	public static boolean auth(String username, String cominput, String pass) {
		boolean ok = false;
		if(username.length()>0 && cominput.length()>0 && pass.length()>0){
			ok = true;
		}
		return ok;
	}

	/**
	 * 登录
	 * 
	 * @Title login
	 * @author j
	 * @param name
	 *            参数
	 * @return void返回类型
	 * @throws
	 */
	public static String login(String url,String market, Object[] parmobj) {
		String result = "";
		String username = (String) parmobj[0];
		String cominput = (String) parmobj[1];
		String md = (String) parmobj[2];
		String phoneinfo = (String) parmobj[3];
		// 2.接口调用及业务实现
			RemoAPI remoapi = new RemoAPI();
			result =remoapi.login(url, username,cominput, md, phoneinfo,market);
		return result;
	}
	
	public static String upClientID(String url,String cmd,String comuser,String part,String clientid){
		String result = "";
		RemoAPI remoapi = new RemoAPI();
		result =remoapi.upClientID(url, cmd,comuser, part, clientid,getMD(comuser,part,clientid,cmd));
		return result;
	}
	
	// md 数据验证码【字符串cmd+comuser+part+clientid的md5值的前5位】
	private static String getMD(String comuser, String part, String clientid,String cmd) {
		String md = MD.getMD5Str(cmd + comuser + part + clientid).substring(0, 5);
		return md;
	}

}
