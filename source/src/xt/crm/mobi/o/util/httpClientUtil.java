package xt.crm.mobi.o.util;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

public class httpClientUtil {
	public static final String CHARSET = HTTP.UTF_8;
	public static HttpClient customerHttpClient;
    public static int timeout=1000,contimeout=2000,sotimeout=4000;//超时时间

    private httpClientUtil() {
    }
    public static synchronized HttpClient getHttpClient(int timeOut,int conTimeOut,int soTimeOut) {
    	 if (null== customerHttpClient) {
             HttpParams params =new BasicHttpParams();
             // 设置一些基本参数
             HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
             HttpProtocolParams.setContentCharset(params,
                     CHARSET);
             HttpProtocolParams.setUseExpectContinue(params, true);
             HttpProtocolParams
                     .setUserAgent(
                             params,
                             "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                     +"AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
             // 超时设置
             /* 从连接池中取连接的超时时间 */
             ConnManagerParams.setTimeout(params, timeOut);
             /* 连接超时 */
             HttpConnectionParams.setConnectionTimeout(params, conTimeOut);
             /* 请求超时 */
             HttpConnectionParams.setSoTimeout(params, soTimeOut);
             
             // 设置我们的HttpClient支持HTTP和HTTPS两种模式
             SchemeRegistry schReg =new SchemeRegistry();
             schReg.register(new Scheme("http", PlainSocketFactory
                     .getSocketFactory(), 80));
             schReg.register(new Scheme("https", SSLSocketFactory
                     .getSocketFactory(), 443));

             // 使用线程安全的连接管理来创建HttpClient
             ClientConnectionManager conMgr =new ThreadSafeClientConnManager(
                     params, schReg);
             customerHttpClient =new DefaultHttpClient(conMgr, params);
             
         }
         return customerHttpClient;
    }
    public static synchronized HttpClient getHttpClient() {
       return getHttpClient(timeout, contimeout, sotimeout);

    }
}