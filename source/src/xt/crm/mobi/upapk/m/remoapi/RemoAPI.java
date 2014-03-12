package xt.crm.mobi.upapk.m.remoapi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import xt.crm.mobi.c.base.Ctrler;
import xt.crm.mobi.o.util.FileUtil;
import xt.crm.mobi.o.util.HttpUtil;
import android.os.Handler;
import android.os.Message;

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
	 * @throws Exception
	 * @throws JSONException
	 *             获取版本信息
	 * 
	 * @Title getVerInfo
	 * @param name
	 *            参数
	 * @return JSONObject返回类型
	 * @throws
	 */
	public JSONObject getVerInfo() {
		Ctrler ctrler = Ctrler.getInstance(null);
		JSONObject jsono = null;
		JSONArray json = null;
		try {
			json = new JSONArray(HttpUtil.updateGetContentS(ctrler.getSystemProperty("checkVersionUrl")));
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			jsono =json.getJSONObject(0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsono;
	}


	/**
	 * 文件下载
	 * 
	 * @Title downFile
	 * @param name
	 *            参数
	 * @return void返回类型
	 * @throws
	 */
	public void downFile(String url, String savepath, String filename,
			Handler handler) throws Exception {
		Message err = handler.obtainMessage();
		// SD卡检测
		if (FileUtil.isSD()) {
			URLConnection connection = new URL(url).openConnection();
			// 网络检测
			if (connection.getReadTimeout() == 5) {
				err.obj = new JSONObject(
						"{\"type\":\"df\",\"err\":\"远端连接有误，请检查重试。\",\"stat\":\"\",\"jd\":\"\"}");
				err.sendToTarget();
			} else {
				// 创建文件的保存路径和文件
				File file1 = new File(savepath);
				if (!file1.exists()) {
					boolean b = file1.mkdirs();
					if (!b) {
						Message star = handler.obtainMessage();
						star.obj = new JSONObject(
								"{\"type\":\"df\",\"err\":\"创建下载目录失败，请检查重试。\",\"stat\":\"\",\"jd\":\"\"}");
						star.sendToTarget();
					}
				}
				File file = new File(savepath + filename);
				file.createNewFile();
				Message star = handler.obtainMessage();
				star.obj = new JSONObject(
						"{\"type\":\"df\",\"err\":\"\",\"stat\":\"begin\",\"jd\":\"\"}");
				star.sendToTarget();
				InputStream inputStream = connection.getInputStream();
				OutputStream outputStream = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int FileLength = connection.getContentLength();
				int count;
				int DownedFileLength = 0;
				while ((count = inputStream.read(buffer)) != -1) {
					DownedFileLength += count;
					outputStream.write(buffer, 0, count);
					Message m = handler.obtainMessage();
					m.obj = new JSONObject(
							"{\"type\":\"df\",\"err\":\"\",\"stat\":\"downloading\",\"jd\":\""
									+ (int) ((double) DownedFileLength
											/ (double) FileLength * 100.0)
									+ "\"}");
					m.sendToTarget();
				}
				FileLength = 0;
				DownedFileLength = 0;
			}
			Message f = handler.obtainMessage();
			f.obj = new JSONObject(
					"{\"type\":\"df\",\"err\":\"\",\"stat\":\"end\",\"jd\":\"\"}");
			f.sendToTarget();

		} else {
			err.obj = new JSONObject(
					"{\"type\":\"df\",\"err\":\"SD卡有误，请检查重试。\",\"stat\":\"\",\"jd\":\"\"}");
			err.sendToTarget();
		}
	}

}
