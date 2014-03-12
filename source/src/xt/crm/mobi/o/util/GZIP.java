package xt.crm.mobi.o.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class GZIP {

	/**
	 *    GZIP解压
	 * Uncompress the incoming file.
	 * 
	 * @param inFileName
	 *            Name of the file to be uncompressed
	 */

	public static boolean doUncompressFile(String inFileName) throws Exception{
		GZIPInputStream in = null;
		FileOutputStream out = null;
		try {
			if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
				System.err.println("File name must have extension of \".gz\"");
				return false;
			}
			
			try {
				in = new GZIPInputStream(new FileInputStream(inFileName));
			} catch (FileNotFoundException e) {
				System.err.println("File not found. " + inFileName);
			}
			String outFileName = getFileName(inFileName);
			
			try {
				out = new FileOutputStream(outFileName);
			} catch (FileNotFoundException e) {
				System.err.println("Could not write to file. " + outFileName);
			}
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len );
			}
			out.flush();
			in.close();
			out.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if(in != null)
					in.close();
				if(out != null)
					out.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return false;

	}
	
	public static String uncompressFile(String inFileName) {
		GZIPInputStream in = null;
		String out = null;
		try {
			if (!getExtension(inFileName).equalsIgnoreCase("gz")) {
				System.err.println("File name must have extension of \".gz\"");
				return out;
			}
			
			try {
				in = new GZIPInputStream(new FileInputStream(inFileName));
			} catch (FileNotFoundException e) {
				System.err.println("File not found. " + inFileName);
			}
			
			byte[] buf = new byte[1024];
			int len;
			StringBuffer stb = new StringBuffer();
			while ((len = in.read(buf)) > 0) {
				stb.append(new String(buf, 0, len - 1));
			}
			out = stb.toString();
			in.close();
			return out;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				if(in != null)
					in.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return out;

	}

	/**
	 * 
	 * Used to extract and return the extension of a given file.
	 * 
	 * @param f Incoming file to get the extension of
	 * 
	 * @return <code>String</code> representing the extension of the
	 * incoming
	 * 
	 * file.
	 
	 */

	public static String getExtension(String f) {

		String ext = "";
		int i = f.lastIndexOf('.');
		if (i > 0 && i < f.length() - 1) {
			ext = f.substring(i + 1);
		}
		return ext;
	}

	/**
	 * 
	 * Used to extract the filename without its extension.
	 * 
	 * @param f Incoming file to get the filename
	 * 
	 * @return <code>String</code> representing the filename without its
	 * 
	 * extension.
	 
	 */

	public static String getFileName(String f) {
		String fname = "";
		int i = f.lastIndexOf('.');
		if (i > 0 && i < f.length() - 1) {
			fname = f.substring(0, i);
		}
		return fname;
	}

}
