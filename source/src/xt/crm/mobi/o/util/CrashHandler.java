package xt.crm.mobi.o.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import xt.crm.mobi.c.base.Ctrler;
  
/** 
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告. 
 *  
 * @author user 
 *  
 */  
public class CrashHandler implements UncaughtExceptionHandler {  
	
    public static final String TAG = "CrashHandler";  
      
    //系统默认的UncaughtException处理类   
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
    //CrashHandler实例  
    private static CrashHandler INSTANCE = new CrashHandler();  
    //程序的Context对象  
    private Context mContext;  
    //用来存储设备信息和异常信息  
    private Map<String, String> infos = new HashMap<String, String>();  
  
    //用于格式化日期,作为日志文件名的一部分  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //默认存放路径
	private static String LOG_PATH_SDCARD_DIR;  
	private Ctrler ctrler ;
	private static final String action = "ENCODE";
	private static final String CHARSET = HTTP.UTF_8;
	
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    /** 
     * 初始化 
     *  
     * @param context 
     */  
    public void init(Context context) {  
        mContext = context;  
        ctrler = Ctrler.getInstance(mContext);
        //获取系统默认的UncaughtException处理器  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        //设置该CrashHandler为程序的默认处理器  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            //如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
                Log.e(TAG, "error : ", e);  
            } 
//            ctrler.handler=new Handler() {
//    			@Override
//    			public void handleMessage(Message msg) {
//    				super.handleMessage(msg); 
//    				if(msg.what==ctrler.BASE_ERROR){
//    				}
//    			}
//    		};
			 //退出程序  
             ctrler.exitApp();
//           System.exit(1); 
//           android.os.Process.killProcess(android.os.Process.myPid());
//           ActivityManager manager = (ActivityManager) mContext.getSystemService(mContext.ACTIVITY_SERVICE);
//           manager.killBackgroundProcesses(mContext.getPackageName());
        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return false;  
        }  
        //使用Toast来显示异常信息  
        new Thread() {  
            @Override  
            public void run() {  
                Looper.prepare();  
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();  
                Looper.loop();  
            }  
        }.start();  
        //收集设备参数信息   
        collectDeviceInfo(mContext);  
        //保存日志文件   
        //saveCrashInfo2File(ex);
        postCrashInfo(ex);
        return true;  
    }  
      
    /** 
     * 收集设备参数信息 
     * @param ctx 
     */  
    public void collectDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                String versionName = pi.versionName == null ? "null" : pi.versionName;  
                String versionCode = pi.versionCode + "";  
                infos.put("versionName", versionName);  
                infos.put("versionCode", versionCode);  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "an error occured when collect package info", e);  
        }  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                field.setAccessible(true);  
                infos.put(field.getName(), field.get(null).toString());  
                Log.d(TAG, field.getName() + " : " + field.get(null));  
            } catch (Exception e) {  
                Log.e(TAG, "an error occured when collect crash info", e);  
            }  
        }  
    }  
    /** 
     * 保存错误信息到文件中 
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private String saveCrashInfo2File(Throwable ex) {  
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
        String result = writer.toString();  
        sb.append(result);  
        try {  
            long timestamp = System.currentTimeMillis();  
            String time = formatter.format(new Date());  
            String fileName = "crash-" + time + "-" + timestamp + ".log";  
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
             	String logpath = Environment.getExternalStorageDirectory()+ctrler.getSystemProperty("logpath");
             	LOG_PATH_SDCARD_DIR=logpath.replace("{number}", ctrler.sp.getInt("userarea", 0)+"");
                 
            	File dir = new File(LOG_PATH_SDCARD_DIR);  
                if (!dir.exists()) {  
                    dir.mkdirs();  
                }  
                FileOutputStream fos = new FileOutputStream(LOG_PATH_SDCARD_DIR + fileName);  
                fos.write(sb.toString().getBytes());  
                fos.close();  
            }  
            return fileName;  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while writing file...", e);  
        }  
        return null;  
    }  
    /** 
     * POST错误信息到服务器 
     *  
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */  
    private void postCrashInfo(Throwable ex) {  
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, String> entry : infos.entrySet()) {  
            String key = entry.getKey();  
            String value = entry.getValue();  
            sb.append(key + "=" + value + "\n");  
        }  
        Writer writer = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(writer);  
        ex.printStackTrace(printWriter);  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
        printWriter.close();  
        final String result = writer.toString();  
        sb.append(result);  
        Log.d(TAG, result);
        try {  
            final String comuser=ctrler.sp.getString("ccn", ""),part=ctrler.sp.getString("part", ""),key=ctrler.getSystemProperty("errlogkey"),imei=PhoneInfo.getIMEI(Ctrler.currentContext),app=ctrler.getSystemProperty("app_name");//new String(ctrler.getSystemProperty("app_name").getBytes("ISO-8859-1"));
			final String md=MD.getMD5Str(key+comuser+part+imei+app);Log.d("app", app);
			final String date=formatter.format(new Date()),tag=result.substring(0, result.indexOf("\n")),err_msg=sb.toString();
            final String application=Ctrler.currentContext.getPackageName();
			new Thread(){
				public void run(){
					String url=ctrler.getSystemProperty("errlogapi");
					StringBuffer str = new StringBuffer();
					str.append("comuser="+URLEncoder.encode(comuser));
					str.append("&part="+URLEncoder.encode(part));
					str.append("&md="+URLEncoder.encode(md.substring(0,5)));
					str.append(PhoneInfo.getPhoneInfo(Ctrler.currentContext));
					str.append("&date="+URLEncoder.encode(date));
					str.append("&tag="+URLEncoder.encode(tag));
					str.append("&application="+URLEncoder.encode(application));
					//str.append("&err_msg="+URLEncoder.encode(Base64.encode(err_msg.getBytes()/*result*/)));
					try {
						String res=/*HttpUtil.*/post1(url, str.toString(),"err_msg",Base64.encode(err_msg.getBytes()/*result*/), 30000, 30000, 30000);
						Log.e("res:",res);
						JSONObject resjson=new JSONObject(res);
						Log.e("resjson:",resjson.toString());
						//ctrler.sendMsg(ctrler.BASE_ERROR);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while posting errlog...", e);  
        }  
    }  
    
    public String post1(String url, String str,String postkey, String postval,int timeout,int contimeout,int sotimeout) throws Exception {
    	Debuger.e("parmstr",str);
    	String act = MD.strcode(str, "A7E276FA"/*ctrler.getSystemProperty("httpkey")*/, action).replace("\n", "");
    	Debuger.e("action",act);
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
    			return (resEntity == null) ? null : EntityUtils.toString(
    					resEntity, CHARSET);
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
}