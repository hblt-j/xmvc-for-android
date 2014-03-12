package xt.crm.mobi.o.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Environment;
import android.os.StatFs;

/**
 * 文件操作，sd卡的判断类
 * 
 */
public class FileUtil {

	private static FileUtil fileUtil;// 文件对象。单例
	public static String sdPath=Environment.getExternalStorageDirectory() + "";// SD卡根目录

	private FileUtil() {

	}

	// 创建单例对象
	public static FileUtil getFileManager() {
		if (fileUtil == null) {
			fileUtil = new FileUtil();
		}
		return fileUtil;
	}

	// 创建多级目录,传入参数文件目录
	public static boolean createDirs(String dri) {
		boolean sucess=false;
		try {
			File file = new File(dri);
		
			if (!file.exists()) {
				file.mkdirs();
				sucess=true;
			}else{
				sucess = true;
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return sucess;
	}

	// 创建一级目录,传入参数文件目录
	public static void createDir(String dri) {
		try {
			File file = new File(dri);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
  
	// 判断文件/目录是否存在，传入参数文件目录URL ，如果存在返回true。不存在false
	public static boolean isExistDir(String dri) {
		File file = new File(dri);
		if (file.exists()) {
			return true;
		}
		return false;
	}
	public  static File createFile(String filename){
		return new File(filename);
	}

	// 判断目录是否存在文件。参数 文件的url(路径)，文件的类型 ，如果存在返回true。不存在返回false
	public static boolean isExistFile(String path, String type) {
		File home = new File(path);
		if (home!=null&&home.listFiles(new FileFilter(type)).length > 0) {
			return true;
		}
		return false;
	}

	/*
	 * 给文件改名 path 文件的目录 oldFileName文件的原来名字，newFileName 文件的新名字
	 */
	public void changeFileName(String oldFileName, String newFileName,
			String path) {
		File oldf = new File(path + File.separator + oldFileName);
		File newf = null;

		if (oldf != null) {
			newf = new File(path + File.separator + newFileName);
			oldf.renameTo(newf);
		}

	}

	// 判断SD 卡是否存在
	public static boolean isSD() {
		if (Environment.getExternalStorageState().equals(// 判断sd卡是否插好
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * 获取SD容量的大小还是sd卡剩余容量的大小 true 代表SD卡的总大小，false 剩余SD卡的大小 返回为SD卡的大小 单位为kb
	 */
	public int SDVOlumn(boolean is) {
		int size;
		File file = Environment.getExternalStorageDirectory();
		StatFs statfs = new StatFs(file.getPath());// 获取块对象
		if (is) {
			// 获取存储块的大小，获取存储块总共的数量 ，单位都是字节
			size = (int) (((long) statfs.getBlockSize()
					* (long) statfs.getBlockCount() / 1024));
		} else {
			// 获取存储块的大小，获取空闲存储块的数量，单位都是字节
			size = (int) (((long) statfs.getBlockSize()
					* (long) statfs.getAvailableBlocks() / 1024));
		}
		return size;
	}

	/*
	 * 遍历目录下的文件 path 文件目录，type文件类型 返回的是文件名的集合
	 */

	public static List<String> getFileList(String path, String type) {
		List<String> list = new ArrayList<String>();
		File home = new File(path);
		for (File file : home.listFiles(new FileFilter(type))) {
			list.add(file.getName());
		}
		return list;

	}

	/*
	 * 删除文件 path 文件路径 
	 */
	public static  void delectFile(String path) {
		try {
			File file = new File(path);
			file.delete();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	//给文件命时间戳的名 字，type 文件的后缀名，可以为“”，返回的是文件的名字
	public String getFileName(String type){
		return new Date().getTime() + type;
	}
	/** 
     * 获得指定文件的byte数组 
     */  
    public static byte[] getBytes(String filePath){  
        byte[] buffer = null; 
        File file;
        FileInputStream fis;
        ByteArrayOutputStream bos;
        try {  
            file = new File(filePath);  
            fis = new FileInputStream(file);  
            bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            while ((n = fis.read(b)) != -1) {  
                bos.write(b, 0, n);  
            }  
            fis.close();  
            bos.close();  
            buffer = bos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }finally{
        	file=null;
        	fis=null;
        	bos=null;
        }
        return buffer;  
    }  
  
    /** 
     * 根据byte数组，生成文件 
     */  
    public static void getFile(byte[] bfile, String filePath,String fileName) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null,dir=null;  
        try {  
            dir = new File(filePath);  
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+fileName);  
            fos = new FileOutputStream(file);  
            bos = new BufferedOutputStream(fos);  
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
        	file=null;dir=null;  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    } 
}

