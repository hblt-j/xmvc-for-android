package xt.crm.mobi.login.m.remoapi;

import java.net.URLEncoder;

import xt.crm.mobi.o.util.HttpUtil;

/**
 * 远程调用接口 <br>
 * 文件名称:RemoAPI.java<br>
 * <br>
 * 内容摘要:<br>
 * <br>
 * 修改日期 修改人员 版本 修改内容 <br>
 * <br>
 * 2012-10-8 j 0.1 0.1 新建<br>
 * 
 * @author: j
 * @version: 0.1
 * @Date: 2012-10-8 下午05:45:33
 */
public class RemoAPI {


	/**
	 * 登录
	 * 
	 * @Title login
	 * @param name
	 *            参数
	 * @return void返回类型
	 * @throws
	 */
	public String login(String url, String user, String cominput, String pass,String phoneInfo,String market) {
		String result = "";
		try {
			String str = "user=" + URLEncoder.encode(user) + "&cominput="
					+ URLEncoder.encode(cominput) + "&pass=" + pass + phoneInfo
					+ "&pt=1"+ "&market="+ URLEncoder.encode(market);
			result = HttpUtil.post1(url, str, 30000, 30000, 30000);
		} catch (Exception e) {
		}
		return result;

	}

	public String upClientID(String url, String cmd, String comuser,
			String part, String clientid,String md) {
		String result = "";
		try {
			String str = "cmd="+cmd+"&comuser=" + URLEncoder.encode(comuser) + "&part="
				+ URLEncoder.encode(part) + "&clientid=" + URLEncoder.encode(clientid) + "&md="+md;
			result = HttpUtil.gets(url+"?"+str);//result = HttpUtil.get(url, str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


}
