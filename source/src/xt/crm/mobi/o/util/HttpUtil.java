package xt.crm.mobi.o.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import xt.crm.mobi.c.base.Ctrler;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.net.Uri;

/*
 * 网络传输类
 */
public class HttpUtil {
	private static HttpUtil httpUtil;// 单例对象
	public static int TIME_OUT = 10000; // 超时时间
	/*
	 * private static final int CONNECT_TIME_OUT = 8000; // HTTP连接超时时间(毫秒)
	 * private static final int READ_TIME_OUT = 8000; // HTTP读超时时间(毫秒)
	 */
	//private static final String key = "A7E276FA";
	private static final String action = "ENCODE";
	private static final String CHARSET = HTTP.UTF_8;
	private static final String POST = "json";
	private static Ctrler ctrler=Ctrler.getInstance(null);
	public static int timeout=1000,contimeout=2000,sotimeout=4000;//超时时间
	private HttpUtil() {  
	}

	// 获取httpUtil对象
	public static HttpUtil getHttpUtil() {
		if (httpUtil == null) {
			httpUtil = new HttpUtil();
		}
		return httpUtil;
	}
     /*
      * httplicent下载文件
      */
	public static InputStream  get(String url) throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity entity = response.getEntity();
			if(entity != null){
				return entity.getContent();
			}else{
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
    public static String gets(String url) throws Exception {
    	String RESULT = "";
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		HttpResponse response;
		try {
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				RESULT=EntityUtils.toString(entity,CHARSET);
				Debuger.e("return RESULT", RESULT);
				return RESULT;
			} else {
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
	public static String get(String url, String str) throws Exception {
		String getUrl = url+"?"
				+ URLEncoder.encode(MD.strcode(str,ctrler.getSystemProperty("httpkey"), action)
						.replace("\n", ""));
		String RESULT = "";
		HttpGet httpGet = new HttpGet(getUrl);
		try {
			HttpClient httpClient = httpClientUtil.getHttpClient();
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity,CHARSET);
			Debuger.e("return RESULT", RESULT);
			return RESULT;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}

	// post方式通信。参数。url网络地址，params 需要传输的数据，names 数据所对应的标识
	public static String post(String url, List<String> params,
			List<String> names) throws Exception {
		String RESULT = "";
		try {
			// 编码参数
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			for (int i = 0; i < params.size(); i++) {
				String str = MD.strcode(params.get(i), ctrler.getSystemProperty("httpkey"), action).replace("\n", "");
				NameValuePair param = new BasicNameValuePair(names.get(i), str);
				formparams.add(param);
			}
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,CHARSET);
			// 创建POST请求
			HttpPost request = new HttpPost(url);
			request.setEntity(entity);
			// 发送请求
			HttpClient client = httpClientUtil.getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity,CHARSET);
			Debuger.e("return RESULT", RESULT);
			return RESULT;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}

	// post方式通信。参数。url网络地址，map key是对应的标识。value是对应的参数
	public static String post(String url, Map<String, String> map) throws Exception {
		try {String RESULT = "";
			// 编码参数
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			Set<Map.Entry<String, String>> set = map.entrySet();
			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
				NameValuePair param = new BasicNameValuePair(entry.getKey(), MD.strcode(entry.getValue(), ctrler.getSystemProperty("httpkey"), action));
				formparams.add(param);
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams,CHARSET);
			// 创建POST请求
			HttpPost request = new HttpPost(url);
			request.setEntity(entity);
			// 发送请求
			HttpClient client = httpClientUtil.getHttpClient();
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity,CHARSET);
			Debuger.e("return RESULT", RESULT);
			return RESULT;
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}

	// post方式通信。参数。url网络地址，params 需要传输的数据，names 数据所对应的标识
	public static String post1(String url, String str,int timeout,int contimeout,int sotimeout) throws Exception {
		Debuger.e("parmstr",str);
		String act = MD.strcode(str, ctrler.getSystemProperty("httpkey"), action).replace("\n", "");
		Debuger.e("action",act);
		String RESULT = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("action", act));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient httpClient = httpClientUtil.getHttpClient(timeout,contimeout,sotimeout);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = httpResponse.getEntity();
				RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
				Debuger.e("return RESULT", RESULT);
				return RESULT;
			} else {
				throw new RuntimeException("请求失败");
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("连接失败", e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException("连接失败", e);
		}catch (ConnectTimeoutException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
	// post方式通信。参数。url网络地址，params 需要传输的数据，names 数据所对应的标识
	public static String post1(String url, String str,String postkey, String postval,int timeout,int contimeout,int sotimeout) throws Exception {
		Debuger.e("parmstr",str);
		String act = MD.strcode(str, ctrler.getSystemProperty("httpkey"), action).replace("\n", "");
		Debuger.e("action",act);
		String RESULT = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> params = new LinkedList<NameValuePair>();
			params.add(new BasicNameValuePair("action", act));
			params.add(new BasicNameValuePair(postkey, postval));
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpClient httpClient = httpClientUtil.getHttpClient(timeout,contimeout,sotimeout);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = httpResponse.getEntity();
				RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
				Debuger.e("return RESULT", RESULT);
				return RESULT;
			} else {
				throw new RuntimeException("请求失败");
			}

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("连接失败", e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException("连接失败", e);
		}catch (ConnectTimeoutException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
	// post方式通信。参数。url网络地址，params 需要传输的数据，names 数据所对应的标识
	public static String multpost(String url, String str,String fild,File file,int timeout,int contimeout,int sotimeout) throws Exception {
		Debuger.e("parmstr",str);
		String act = MD.strcode(str, ctrler.getSystemProperty("httpkey"), action).replace("\n", "");
		Debuger.e("action",act);
		String RESULT = "";
		try {
			HttpPost httpPost = new HttpPost(url);
			MultipartEntity entity = new MultipartEntity();  
	        entity.addPart(fild, new FileBody(file));  
	        entity.addPart("action", new StringBody(act));
			httpPost.setEntity(entity);
			HttpClient httpClient = httpClientUtil.getHttpClient(timeout,contimeout,sotimeout);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity resEntity = httpResponse.getEntity();
				RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity, CHARSET);
				Debuger.e("return RESULT", RESULT);
				return RESULT;
			} else {
				throw new RuntimeException("请求失败");
			}

		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("连接失败", e);
		} catch (ClientProtocolException e) {
			throw new RuntimeException("连接失败", e);
		}catch (ConnectTimeoutException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
	
	public static int uploadFile(File file,String field, String RequestURL) {
		int res = 0;String RESULT = "";
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);// 缓存的最长时间
			conn.setConnectTimeout(TIME_OUT);// 连接的最长时间
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
					+ BOUNDARY);

			if (file != null) {
				/**
				 * 当文件不为空，把文件包装并且上传
				 */
				DataOutputStream dos = new DataOutputStream(conn
						.getOutputStream());
				StringBuffer sb = new StringBuffer();
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINE_END);
				/**
				 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
				 * filename是文件的名字，包含后缀名的 比如:abc.png
				 */
				sb.append("Content-Disposition: form-data; name=\""+field+"\"; filename=\""
								+ file.getName() + "\"" + LINE_END);
				sb.append("Content-Type: application/octet-stream; charset="+ CHARSET + LINE_END);
				sb.append(LINE_END);
				dos.write(sb.toString().getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
						.getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
			
				 res = conn.getResponseCode();
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				RESULT = sb1.toString();
				Debuger.e("return RESULT", RESULT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	// 版本更新
	public static String updateGetContentS(String url) throws Exception {
		try {String RESULT = "";
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpClient = httpClientUtil.getHttpClient();
			HttpResponse response = httpClient.execute(httpGet);
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new RuntimeException("请求失败");
			}
			HttpEntity resEntity = response.getEntity();
			RESULT=(resEntity == null) ? null : EntityUtils.toString(resEntity,CHARSET);
			Debuger.e("return RESULT", RESULT);
			return RESULT;
		}catch (UnknownHostException e){
			throw new RuntimeException("连接失败", e);
		} catch (SocketException e) {
			throw new RuntimeException("连接失败", e);
		}catch (SocketTimeoutException e){
			throw new RuntimeException("连接失败", e);
		} catch (UnsupportedEncodingException e) { 
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			throw new RuntimeException("连接失败", e);
		}
	}
    public static void openCustomerView(String customerinfo,Context contexts,String viewuri){
		String URL = viewuri+ URLEncoder.encode(MD.strcode(customerinfo, ctrler.getSystemProperty("httpkey"), action).replace("\n", ""));
    	 Uri uri = Uri.parse(URL);
         Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         contexts.getApplicationContext().startActivity(intent);
    }

	/*
	 * 标准post算法
	 * 调用方法 HttpUtil.baseGet(map);
	 * 传入入参数Map<String,string> get,post
	 * eg:get：url:
	 *         key:user.login
	 *         st:
	 *    get传递的是基本参数
	 *    post:phonto:base64加密
	 *        :imel=设备吗
	 *    post传递的是大数据
	 * 返回数据：jsonobject,
	 *         基本格式{"ok":"","msg":"","sid":""}
	 *         ok 是状态。包括（0,1,2,11,22）0表示返回错误。msg表示原因，1表示正常，
	 *                   2.表示，11表示网络超时，22表示请求失败
	 *         msg;表示原因，
	 *         sid:表示session_id,
	 */
	public static JSONObject basePost(Map<String,String> get, JSONObject json){
			JSONObject res = null;
			String md=MD.getMD5Str(json.toString());//算成json的MD5值
			String encodeStr =sortS(get,md);//把md加入到参数中。合成URL
			Debuger.d("base_post",encodeStr);
			List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
			NameValuePair param = new BasicNameValuePair(POST, json.toString());//json 不用加密
			formparams.add(param);
			UrlEncodedFormEntity entity = null;
			try {
				entity = new UrlEncodedFormEntity(formparams,CHARSET);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			res=BPost(encodeStr, entity);
			//－－－－－－－－集成网络超时三次错误　超时间设置　msg生成－－－－－－－
			try {
				if(res.getInt("ok")>10 && res.getInt("ok")<20){
					int i=0;
					while(i<2){
						res=BPost(encodeStr, entity);
						if(res.getInt("ok")<10){//成功则返回res
							break;
						}else if (i==1){//三次失败则网络超时变为错误，代码为21
							res.remove("ok");
							res.put("ok", 21);
						}
						i++;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			//－－－－－－集成网络超时三次错误　by j－－－－－－－－－
			
			return res;
			
	}
	//basePost重构方法
	public static JSONObject basePost(Map<String,String> get, String json){
		JSONObject res = null;
		String md=MD.getMD5Str(json);//算成json的MD5值
		String encodeStr =sortS(get,md);//把md加入到参数中。合成URL
		Debuger.d("base_post",encodeStr);
		List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
		NameValuePair param = new BasicNameValuePair(POST, json);//json 不用加密
		formparams.add(param);
		UrlEncodedFormEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(formparams,CHARSET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		res=BPost(encodeStr, entity);
		//－－－－－－－－集成网络超时三次错误　超时间设置　msg生成－－－－－－－
		try {
			if(res.getInt("ok")>10 && res.getInt("ok")<20){
				int i=0;
				while(i<2){
					res=BPost(encodeStr, entity);
					if(res.getInt("ok")<10){//成功则返回res
						break;
					}else if (i==1){//三次失败则网络超时变为错误，代码为21
						res.remove("ok");
						res.put("ok", 21);
					}
					i++;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//－－－－－－集成网络超时三次错误　by j－－－－－－－－－
		
		return res;
		
	}
	/** 
	 * 
	 * @param 
	 * @return JSONObject
	 * @throws 
	 * 2012-12-3 下午03:04:58
	 */
	public static JSONObject BPost(String encodeStr,UrlEncodedFormEntity entity) {
		JSONObject res = null;
		// 创建POST请求
		HttpPost request = new HttpPost(encodeStr);
		request.setEntity(entity);
		// 发送请求
		HttpClient client = httpClientUtil.getHttpClient(timeout,contimeout,sotimeout);
		HttpResponse response = null;
		try {
			response = client.execute(request);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			try {
				res=new JSONObject("{\"ok\":\"11\",\"err_msg\":\"网络超时\"}");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return res;
		}
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			try {
				return new JSONObject("{\"ok\":\"28\",\"err_msg\":\"请求失败,请稍后再试\",\"sid\":\"0\"}");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		HttpEntity resEntity = response.getEntity();
		String jsonS = null;
		try {
			jsonS = (resEntity == null) ? "{\"ok\":\"27\",\"err_msg\":\"云端数据同步错误，请稍后重试\"}" : EntityUtils.toString(resEntity,CHARSET);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {Debuger.e("return RESULT", jsonS);
			res = new JSONObject(jsonS);
		} catch (JSONException e) {
			res = new JSONObject();
			try {
				res.put("jsonarr", jsonS);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		}
		return  res;
	}
/*	public static JSONObject baseGet(Map<String,String> map, JSONObject json){
//	try {
		String md=MD.getMD5Str(json.toString());//算成json的MD5值
		Log.i("md  ", md);
//		String get =sortS(map,md);//把md加入到参数中。合成URL
//		Log.i("base_get",encodeStr);
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>(); // 请求参数
//		NameValuePair param = new BasicNameValuePair(POST, json.toString());//json 不用加密
//		formparams.add(param);
//		UrlEncodedFormEntity entity = null;
//		try {
//			entity = new UrlEncodedFormEntity(formparams,CHARSET);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		String get = sortS(map,md);
		get+="&json="+URLEncoder.encode(json.toString());
		JSONObject res = BGet(get);
		//－－－－－－－－集成网络超时三次错误　超时间设置　msg生成－－－－－－－
		try {
			if(res.getInt("ok")>10 && res.getInt("ok")<20){
				int i=0;
				while(i<2){
					res =  BGet(get);
					if(res.getInt("ok")<10){//成功则返回res
						break;
					}else if (i==1){//三次失败则网络超时变为错误，代码为21
						res.remove("ok");
						res.put("ok", 21);
					}
					i++;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//－－－－－－集成网络超时三次错误　by j－－－－－－－－－
		
		return res;
	}*/
	/*
	 * 标准get方法，
	 * 调用方法 :HttpUtil.baseGet(map);
	 * 出入参数:Map<String,String> map
	 *        eg:url:表示URL
	 *           key:user.login表示接口标识
	 *           st:表示时间戳
	 *返回数据：jsonobject,
	 *         基本格式{"ok":"","msg":"","sid":""}
	 *         ok 是状态。包括（0,1,2,11,22）0表示返回错误。msg表示原因，1表示正常，
	 *                   2.表示，11表示网络超时，22表示请求失败
	 *         msg;表示原因，
	 *         sid:表示session_id,
	 */
	public static JSONObject baseGet(Map<String ,String> map){
		String get = sortS(map,"");
		Debuger.d("base_get",get);
		JSONObject res = BGet(get);
		//－－－－－－－－集成网络超时三次错误　超时间设置　msg生成－－－－－－－
		try {
			if(res.getInt("ok")>10 && res.getInt("ok")<20){
				int i=0;
				while(i<2){
					res =  BGet(get);
					if(res.getInt("ok")<10){//成功则返回res
						break;
					}else if (i==1){//三次失败则网络超时变为错误，代码为21
						res.remove("ok");
						res.put("ok", 21);
					}
					i++;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//－－－－－－集成网络超时三次错误　by j－－－－－－－－－
		
		return res;
	}
	public static JSONObject BGet(String get){
		JSONObject res = null;
		HttpGet httpGet = new HttpGet(get);
		HttpClient httpClient = httpClientUtil.getHttpClient();
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpGet);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
			try {
				res=new JSONObject("{\"ok\":\"11\",\"err_msg\":\"网络超时\"}");
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			return res;
		}
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			try {
				return new JSONObject("{\"ok\":\"28\",\"err_msg\":\"请求失败\",\"sid\":\"0\"}");
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		HttpEntity resEntity = response.getEntity();
		String jsonStr = null;
		try {
			jsonStr = (resEntity == null) ? "{\"ok\":\"27\",\"err_msg\":\"云端数据同步错误，请稍后重试\"}" : EntityUtils.toString(resEntity,CHARSET);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {Debuger.e("return RESULT", jsonStr);
			res = new JSONObject(jsonStr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return  res;
	}
	/**
	    * 遍历map 生成url
	    * 通用key有：url key st
	    * post增加key:md json
	    */
	public static String sortS(Map<String,String> map,String md){
			 String str="key="+URLEncoder.encode(map.get("key"));
			 String url =map.get("url");
			 Set<Map.Entry<String, String>> set = map.entrySet();
			 for (Iterator<Map.Entry<String,String>> it = set.iterator(); it.hasNext();) {
				 Map.Entry<String,String> entry = (Map.Entry<String, String>) it.next();
				 if(!entry.getKey().equals("url")&&!entry.getKey().equals("key"))
		               str=str+"&"+entry.getKey()+"="+URLEncoder.encode(entry.getValue());
		     }
			 if(!md.equals("")){
				 str=str+"&md="+md;
			 }
			 //$this->get_string .= "&st=".time(); //增加当前时间戳，房子url被二次利用　　//要求参数传入
			 Debuger.w("baseget_str", str);
			 String encode = url+"?act="+ MD.strcode(str, ctrler.getSystemProperty("httpkey"), action)/*.replace("\n", "")*/;
			 return encode;
		}
	
}