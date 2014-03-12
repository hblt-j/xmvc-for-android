package xt.crm.mobi.o.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xt.crm.mobi.c.base.Ctrler;
import android.os.Environment;
import android.util.Log;
 
/**
 * 可控开关的日志调试,写入指定log目录，
 * 默认/xtools/order/{number}/log/[sys]yyyy-MM-dd.log，strcode加密每行信息
	手动日志方案调用方法：类似Log.i(tag,msg);
	//Debuger.d("test", "content");
	//定期搜索log下文件上传。。
	//Debuger.delLogFile();//删除超过保留期log(含系统日志)
	 
	系统日志方案调用方法：（不太可靠：不支grep过滤）//正常启动一次g2日志大小就450k上，因各系统的支持不禁相同，华为甚至需手动开启日志权限，末及时写进文件还可能被系统回收机制清掉
	//runtime直接执行命令，现改良为控制io截取输入出流的方式jiayu支持grep，但但其它三星华为魅族htc经测试都不行，
	//Debuger.cmd="pwd"//可改变默认过滤日志内容，默认过滤‘本应用d级’以上信息，为节省流量和规范测试，需清理和统一程序中的调试信息
	Debuger.SysLog();
 * @author J
 * @version 1.0
 */
public class Debuger {
	public static Boolean LOG_SWITCH = true;                       // 日志文件总开关
	public static Boolean LOG_WRITE_TO_FILE = true;                // 日志写入文件开关
	public static Boolean LOG_IS_MD = false;						// 加密否
	public static final String key = "zd2012@xt#1.0";				//加密key
	public static final String action = "ENCODE";
	public static int SDCARD_LOG_FILE_SAVE_DAYS = 0;              // sd卡中日志文件的最多保存天数
	public static String LOGFILENAME = ".log";                 	// 本类输出的日志文件名称
	public static String LOG_PATH_SDCARD_DIR = "/sdcard/logs";     // 日志文件在sdcard中的路径
	public static SimpleDateFormat LogSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   // 日志的输出格式
	public static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");           // 日志文件格式
    public static String cmd="logcat -d -b main -v time *:d";// | grep \""+android.os.Process.myPid()+"):\"";//系统日志默认过滤本应用d级以上信息
    public static void init() {//by j 
    	Ctrler ctrler = Ctrler.getInstance(null);
		if(ctrler.getSystemProperty("logpath")!=null){
	    	String logpath = Environment.getExternalStorageDirectory()+ctrler.getSystemProperty("logpath");
			LOG_PATH_SDCARD_DIR=logpath.replace("{number}", ctrler.sp.getInt("userarea", 0)+"");
		}
		File file = new File(LOG_PATH_SDCARD_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }
 
    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }
 
    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }
 
    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }
 
    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }
 
    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {
        if (LOG_SWITCH) {
            if ('i' == level) {
                Log.i(tag, msg);
            } else if ('e' == level) {
                Log.e(tag, msg);
            } else if ('w' == level) {
                Log.w(tag, msg);
            } else if ('d' == level) {
                Log.d(tag, msg);
            }else {
                Log.v(tag, msg);
            }
            if (LOG_WRITE_TO_FILE){
            	init();//by j 
                writeLogtoFile(String.valueOf(level), tag, msg);
            }
        }
    }
 
    /**
     * 打开日志文件并写入日志
     *
     * @return
     * **/
    private static void writeLogtoFile(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String needWriteFiel = logfile.format(nowtime);
        String needWriteMessage = LogSdf.format(nowtime) + " " + mylogtype + " " + tag + " " + text;
         
        File file = new File(LOG_PATH_SDCARD_DIR, needWriteFiel + LOGFILENAME);
         
        try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(LOG_IS_MD?MD.strcode(needWriteMessage, key, action):needWriteMessage);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 删除制定的日志文件(含系统日志)
     *
     */
    public static void delLogFile() {
        String needDelFiel = logfile.format(getDateBefore());
        List<String> list = FileUtil.getFileList(LOG_PATH_SDCARD_DIR, LOGFILENAME);
        for (String name : list) {
        	String name1 ="";
        	name1 = name.replaceFirst("sys", "");
			if(name1.compareTo(needDelFiel + LOGFILENAME)<=0){
				File file = new File(LOG_PATH_SDCARD_DIR, name);
		        if (file.exists()) {
		            file.delete();
		        }
			}
		}
        
    }
 
    /**
     * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
     * */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - SDCARD_LOG_FILE_SAVE_DAYS);
        return now.getTime();
    }
    
    
    //=====================SysLog======================
    private static String phoneID=android.os.Build.ID;
	public static void SysLog()
	{
		Thread th=new Thread(new Runnable()
		{
			public void run()
			{
				 if (LOG_WRITE_TO_FILE){
					 init();//by j 
					 getSysLog();
				 }
			}
		});
		th.start();
	}
	
	private static void getSysLog()
	{
		System.out.println(android.os.Process.myPid()+"--------func start--------"+cmd); 
//		try
//		{
//			ArrayList<String> clearLog=new ArrayList<String>(); 
//			clearLog.add("logcat");
//			clearLog.add("-c");
//			Process process=Runtime.getRuntime().exec(/*new String[]{"/system/bin/sh","-c",cmd}*/cmd);
//			BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
//			String str=null;
//			while((str=bufferedReader.readLine())!=null)	
//			{
//				writeSysLogtoFile(str);
//				//sendLogUDP(logToJobj(str));
//			}
//			Runtime.getRuntime().exec(clearLog.toArray(new String[clearLog.size()]));
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
		Process proc = null;
        try {
                proc = Runtime.getRuntime().exec("/system/bin/sh", null, new File("/system/bin")); // android中使用
                // proc = Runtime.getRuntime().exec("/bin/bash", null, new File("/bin"));          //Linux中使用
                // 至于windows，由于没有bash，和sh 所以这种方式可能行不通
        } catch (IOException e) {
                e.printStackTrace();
        }
        if (proc != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                out.println(cmd);
                // out.println("exit");
                try {
                        String line;
                        while ((line = in.readLine()) != null) {
                                //Log.d("command", line);
                                writeSysLogtoFile(line);
                        }  
                		System.out.println("--------func end--------");
                        // proc.waitFor(); //上面读这个流食阻塞的，所以waitfor 没太大必要性
                        in.close();
                        out.close();
                        proc.destroy();
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }
	}
	
	private static void writeSysLogtoFile(String logstr) {
		File file = new File(LOG_PATH_SDCARD_DIR, "sys"+logfile.format(new Date()) + LOGFILENAME);
		try {
            FileWriter filerWriter = new FileWriter(file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(logstr);
            bufWriter.newLine();
            bufWriter.flush();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
/*
	private static void sendLogEmail(String info)
	  // 需要 android.permission.SEND权限  
	    Intent mailIntent = new Intent(Intent.ACTION_SEND);  
	    mailIntent.setType("plain/text");  
	    String[] arrReceiver = { "sodinoopen@hotmail.com" };  
	    String mailSubject = "App Error Info[" + getPackageName() + "]";  
	    String mailBody = info;  
	    mailIntent.putExtra(Intent.EXTRA_EMAIL, arrReceiver);  
	    mailIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);  
	    mailIntent.putExtra(Intent.EXTRA_TEXT, mailBody);  
	    startActivity(Intent.createChooser(mailIntent, "Mail Sending...")); 
	}
	
	
	private static JSONObject logToJobj(String s)
	{
		
		String s1=s.substring(0,1);
		
		String s2=s.substring(s.indexOf('/')+1, s.indexOf('('));
		
		String s3=s.substring(s.indexOf('(')+1, s.indexOf(')'));
		
		String s4=s.substring(s.indexOf(':')+1);
		
		
		JSONObject obj=new JSONObject();
		try
		{
			obj.put("PhoneID", phoneID);
			obj.put("Type", s1.trim());
			obj.put("Tag", s2.trim());
			obj.put("ThreadID", s3.trim());
			obj.put("Msg", s4.trim());
			obj.put("Time", getTime());
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}			
		return obj;
	}
	
	private static void sendLogUDP(JSONObject obj)
	{
		String objStr=obj.toString();
		int port=8081;
		DatagramSocket ds=null;
		InetAddress ia=null;
		if(obj!=null)
		{
			try
			{
				ds=new DatagramSocket();
				ia=InetAddress.getByAddress(null);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			int msgLenght=objStr.length();
			byte[] msgByte=objStr.getBytes();
			
			DatagramPacket dp=new DatagramPacket(msgByte, msgLenght, ia, port);
			
			try
			{
				ds.send(dp);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static String getTime()
		{
	        Time time = new Time("GMT+8");    
	        time.setToNow();
	        int year = time.year;
	        int month = time.month;
	        int day = time.monthDay;
	        int minute = time.minute;
	        int hour = time.hour;
	        int sec = time.second;	        
	        return year+"-"+month+"-"+day+" "+hour+":"+minute+":"+sec;
		}
*/
}

