package xt.crm.mobi.o.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  MD 加密函数
 *  URL加密函数   decode(String str, String key, String action)
 * @author Administrator
 *
 */
public class MD {

	// URL加密
	public static String strcode(String str, String key, String action) {

		byte[] arrUrl = str.getBytes();
		byte[] arrKey = key.getBytes();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		int k = 0;
		int length = arrKey.length;
		for (int i = 0; i < arrUrl.length; i++) {
			k = i % length;
			result.write((arrUrl[i] ^ arrKey[k]));
		}

		String ret = Base64.encode(result.toByteArray());
		try {
			result.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;

	}

	// MD5加密(UTF-8)
	public static String getMD5Str(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}

	// MD5加密(GB2312)
	public static String getMD5Strgbk(String str) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("gb2312"));
		} catch (NoSuchAlgorithmException e) {
			System.exit(-1);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();

		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
				md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
			else
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
		}
		return md5StrBuff.toString();
	}
}
