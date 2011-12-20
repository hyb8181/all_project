package com.ruysing.filemanager.util;

import java.io.File;

public class FileUtil {

	/**
	 * 獲取文件類型
	 * @param f
	 * @return
	 */
	public static String getMimeType(File f){
		String mimeType = null;
		if(f == null || !f.exists()){
			return "*/*";
		}
		int index = f.getName().lastIndexOf(".");
		if(index == -1){
			return "*/*";
		}
		String extName = f.getName().substring(index).toLowerCase();
		if(".png".equals(extName) || ".gif".equals(extName) || ".jpg".equals(extName)){
			mimeType = "image";
		}else if(".mp3".equals(extName) || ".wav".equals(extName) || ".ogg".equals(extName) || ".mid".equals(extName)){
			mimeType = "audio";
		}else if(".apk".equals(extName)){
			mimeType = "application/vnd.android.package-archive";
		}else if(".txt".equals(extName)){
			mimeType ="text";
		}else{
			mimeType = "*";
		}
		if(!".apk".equals(extName)){
			mimeType += "/*";
		}
		return mimeType;
	}
	
}
