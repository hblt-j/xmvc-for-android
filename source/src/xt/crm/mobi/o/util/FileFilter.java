package xt.crm.mobi.o.util;

import java.io.File;
import java.io.FilenameFilter;
/*
 * 文件过滤类，实现文件过滤接口
 * */
  public class FileFilter implements FilenameFilter{
	  
    private String type;//过滤文件的了类型  例如“.amr”
    
    public FileFilter(String type){
    	this.type = type;
    }
    
	public boolean accept(File dir, String filename) {
		// TODO Auto-generated method stub
		return filename.endsWith(type);
		
	}
	
}
