package xt.crm.mobi.o.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.text.format.Time;

/**
 * 时间处理工具类
 * @author lj
 * 
 */

public class TimeUtil {
	//Convert Unix timestamp to format date style  
	public static String TimeStamp2Date(String format,String timestampString){  
	  Long timestamp = Long.parseLong(timestampString)*1000;  
	  String date = new java.text.SimpleDateFormat(format).format(new java.util.Date(timestamp));  
		return date;
	}

	//Convert format date to Unix timestamp style  
	public static int Date2TimeStamp(String format,String formtDateString){
		int res = 0 ;
		try {
			res = (int)new java.text.SimpleDateFormat(format).parse(formtDateString).getTime()/1000;
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return res;
	}
	public static int getlongTime(String format,String formtDateString){
		int res = 0 ;
		try {
			res = (int)new java.text.SimpleDateFormat(format).parse(formtDateString).getTime();
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

		return res;
	}
	/*
	 * 把毫秒数转换成倒计时格式 count为毫秒数
	 */
	public static  String TimeAsk(int count){
		String format="";
		int honr = 0;
		if (count >= 3600) {
			honr = count / 3600;
		}
		int c = count % 3600;
		int m = c % 60;
		String miao = m + "";
		if (m < 10) {
			miao = "" + m;
		}
		int f = c / 60;
		String fen = f + "";
		if (f < 10) {
			fen = "" + f;
		}
		if (honr == 0) {
			if(fen.equals("00")||fen.equals("0")){
				format= miao + "秒";
			}else{
				format=fen + "分:"+ miao + "秒";
			}
			
		} else {
			format= honr + "时:"+ fen + "分:" + miao + "秒" ;
		}
		return format;
	}
	/*
	 * 返回时间格式 date 为日期。format为格式
	 */
	public static String DateFormat(String format,String date){
		SimpleDateFormat sfd = new SimpleDateFormat(format);
		return sfd.format(new Date(Long.parseLong(date)));
	}
	public static String getDateFor(String format){
		SimpleDateFormat sfd = new SimpleDateFormat(format);
		return sfd.format(new Date(System.currentTimeMillis()));
	}

	public static long getDateBwttun(String date2) throws ParseException {
		Time times = new Time("GMT+8");
		times.setToNow();
		int year = times.year;
		int month = times.month;
		int day = times.monthDay;
		String date1 = year + "-" + (month + 1) + "-" + day;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = sdf.parse(date1);
		Date d2 = sdf.parse(date2);
		return (d2.getTime() - d1.getTime() + 1000000) / (3600 * 24 * 1000);
}
 /*  public static String  getBwttunDay(String date2) throws ParseException{
	   Time times = new Time("GMT+8");       
       times.setToNow();      
       int year = times.year;      
       int month = times.month;      
       int day = times.monthDay;  
      
       String date1=year+"-"+(month+1)+"-"+day;
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	  // System.out.println("timerutil date1 "+ date1+"  date2 "+date2);
	   Date d1=sdf.parse(date1);  Date d2=sdf.parse(date2);  
	   System.out.println(d1.getTime()+"  date 1  date  2  " +d2.getTime());
	   System.out.println("timerutil "+(d2.getTime()-d1.getTime()+1000000));
	   System.out.println("timerutil "+(d2.getTime()-d1.getTime()));
	   System.out.println("timerutil "+(d2.getTime()-d1.getTime())/(3600*24*1000));
	   long bwttun = (d2.getTime()-d1.getTime()+1000000)/(3600*24*1000);
	   if(bwttun<0){
		   return (0-bwttun)+"天前";
	   }else if(bwttun==0){
		   return "今天";
	   }else if(bwttun>0){
		   return bwttun+"天后";
	   }else{
		   return "";
	   }
	  
	 //  return (d2.getTime()-d1.getTime()+1000000)/(3600*24*1000); 
	  
   }*/
   public static String  getBwttunDay(int y,int m,int d,int ismonth,int isyinli) throws ParseException{
	   Time times = new Time("GMT+8");       
       times.setToNow();      
       int year = times.year;      
       int month = times.month;      
       int day = times.monthDay;  
      
       String date1=year+"-"+(month+1)+"-"+day;
	   SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
	   String date2="";
	   if(ismonth==2){
		   if(isyinli==1){
			   //把阳历转换成阴历
			   Calendar canlendar = Calendar.getInstance();
		          canlendar.setTime(DateParse.getLunarCalendar(y+"-"+m+"-"+d));
				   int days =canlendar.get(Calendar.DATE);
				   //阴转阳
				   canlendar.setTime(DateParse.getGregorianCalendar(year+"-"+(month+1)+"-"+days));
				   date2=canlendar.get(Calendar.YEAR)+"-"+(canlendar.get(Calendar.MONTH)+1)+"-"+canlendar.get(Calendar.DATE);
				   
		   }else{
			   date2=year+"-"+(month+1)+"-"+d;
		   }
	   }else{
		   if(isyinli==1){
			   //把阳历转换成阴历
			   Calendar canlendar = Calendar.getInstance();
		          canlendar.setTime(DateParse.getLunarCalendar(y+"-"+m+"-"+d));
				   int days =canlendar.get(Calendar.DATE);
				   int months =canlendar.get(Calendar.MONTH);
				   //阴转阳
				   canlendar.setTime(DateParse.getGregorianCalendar(year+"-"+(months+1)+"-"+days));
				   date2=canlendar.get(Calendar.YEAR)+"-"+(canlendar.get(Calendar.MONTH)+1)+"-"+canlendar.get(Calendar.DATE);
				   
		   }else{
			   
			   date2=year+"-"+m+"-"+d;
		   }
	   }
	   
	   Date d1=sdf.parse(date1);  Date d2=sdf.parse(date2);  
	   long bwttun = (d2.getTime()-d1.getTime())/(3600*24*1000);
	   if(bwttun<0){
		   return (0-bwttun)+"天前";
	   }else if(bwttun==0){
		   return "今天";
	   }else if(bwttun>0){
		   return bwttun+"天后";
	   }else{
		   return "";
	   }
	  
	 //  return (d2.getTime()-d1.getTime()+1000000)/(3600*24*1000); 
	  
   }

	public static String getWeek(String pTime) {

		  
		String Week = "周";


		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {

			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}
   public static  String getMonth(int m) {

		  String month="";
		  switch(m){
		  case 1:
			  month="一";
			  break;
		  case 2:
			  month="二";
			  break;
		  case 3:
			  month="三";
			  break;
		  case 4:
			  month="四";
			  break;
		  case 5:
			  month="五";
			  break;
		  case 6:
			  month="六";
			  break;
		  case 7:
			  month="七";
			  break;
		  case 8:
			  month="八";
			  break;
		  case 9:
			  month="九";
			  break;
		  case 10:
			  month="十";
			  break;
		  case 11:
			  month="十一";
			  break;
		  case 12:
			  month="十二";
			  break;
			  
		  }
		  return month+"月";
	}

}
