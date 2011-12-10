package com.android.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * <p>文件处理类相关优化处理</p>
 * 
 * @author  汪军
 * @version 1.00 2011/10/18 汪军 创建
 * <p>      1.00 2011/10/18 汪军 文件处理模块封装</p>
 * @see     ?
 */
public class FileUtils {
	
	private static String sdPath;//sd卡路径
	private static String filename;//文件名
	private static Activity myActivity;
	private static final String  tag= "FileUtils";//打印Log标识字符串

	public FileUtils(Activity act){
		myActivity = act;
	}
	
	/** 
	 * FileUtils构造函数
	 */
	public FileUtils(){
		sdPath = Environment.getExternalStorageDirectory()+"/";
	}
	
	/**************************************************************************/
	/**
	 * 获取Sd卡路径
	 * @param null
	 * @return /mnt/sdcard/
	 */
	public static String getSdpath(){
		return Environment.getExternalStorageDirectory()+"/";
	}

	/** 
	 * 创建新文件
	 *
	 * @param fileName 路径+文件名
	 * @return 文件操作句柄
	 */
	public static File creatFile(String filePath, String fileName) throws IOException {
		File file = new File(filePath + fileName);
		file.createNewFile();
		return file;
	}
	
	/**
	 * 删除文件
	 * @param filePath 路径
	 * @param fileName 文件名
	 * @return file 文件句柄
	 */
	public static File delFile(String filePath,String fileName){
		File file = new File(filePath+fileName);  
		if (file.exists()) {
			file.delete();
		}  
		return file;
	}
	
	/** 
	 * 将内容写入SD卡中
	 *
	 * @param filePath 路径
	 * @param fileName 文件名
	 * @param input 输入内容
	 * @return 文件操作句柄
	 */
	public File write(String filePath, String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatDir(filePath);
			file = creatFile(filePath,fileName);
			output = new FileOutputStream(file);
			byte buffer[] = new byte[4*1024];
			while((input.read(buffer))!= -1){
				output.write(buffer,0,buffer.length);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				output.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/** 
	 * 将内容写入SD卡中
	 *
	 * @param filePath 路径
	 * @param fileName 文件名
	 * @param strInput 输入内容
	 * @throws IOException 文件操作失败时抛出异常
	 * @return 文件操作句柄
	 */
    public static File write(String filePath, String fileName, String strInput) throws IOException {
    	File file = null;
        OutputStream output = null;
        try{
        	creatDir(filePath);
        	
        	if(false == isExist(filePath,fileName)){
        		file = creatFile(filePath,fileName);
        	}
        	else{
        		file= new File(filePath+fileName);
        	}
        		
            output = new FileOutputStream(file);

	        FileOutputStream stream = new FileOutputStream(file);
	        String s = strInput;
	        byte[] buf = s.getBytes();
	        stream.write(buf);
	        stream.close();

            output.flush();
        }
        catch(Exception e){
            throw new IOException();
        }
        finally{
            try{
                output.close();
            }
            catch(Exception e){
                throw new IOException();
            }
        }
        return file;
    }
    
	/** 
	 * 读取文件内容
	 *
	 * @param path 路径
	 * @param fileName 文件名
	 * @throws IOException 文件操作失败时抛出异常
	 * @return String 文件内容
	 */
    public static String read(String filePath, String fileName) throws IOException{
    	File file=null;
    	String b;
    	String temp = "";
  	  	file= new File(filePath+fileName);
	    if (file.isFile())
	    {
	    	try{
	    		BufferedReader sb = new BufferedReader(new FileReader(new File(filePath+fileName)));
	    		while((b = sb.readLine()) != null){
	    			temp+=b;
	    			temp+="\n";
	    			}
	    		}
	    	catch (Exception e){
	            throw new IOException();
	    		}
	    }
	    return temp;
    }
    
	/** 
	 * 文件是否存在
	 *
	 * @param fileName 文件路径
	 * @param fileName 文件名
	 * @return true-文件存在 false-文件不存在
	 */
	public static boolean isExist(String filePath, String fileName){
		File file = new File(filePath + fileName);
		return file.exists();
	}
	
	/**
	 * 清空文件
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return null
	 * @throws IOException 
	 */
	 public static void clean(String filePath,String fileName) throws IOException{
		    delFile(filePath,fileName);
		    creatFile(filePath,fileName);
	 }

	 /** 
	 * 创建新文件夹
	 *
	 * @param dirName 目录名称
	 * @return 文件操作句柄
	 */
	public static File creatDir(String path){
		File dir = new File(path);
		dir.mkdir();
		return dir;
	}
	
	 /**
     * 删除文件夹里面的所有文件
     * 
     * @param path String 文件夹路径
     */
    public static void delAllFile(String path) {
            File file = new File(path);
            if (!file.exists()) {
                    return;
            }
            if (!file.isDirectory()) {
           return;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                    if (path.endsWith(File.separator)) {
                            temp = new File(path + tempList[i]);
                    }
                    else {
                            temp = new File(path + File.separator + tempList[i]);
                    }
                    if (temp.isFile()) {
                            temp.delete();
                    }
                    if (temp.isDirectory()) {
                            delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
                            delDir(path+"/"+ tempList[i]);//再删除空文件夹
                    }
            }
    }
    
	/**
	 * 删除文件夹
	 * @param folderPath 目录名
	 */
	public static void delDir(String folderPath) {
        try {
                delAllFile(folderPath); //删除完里面所有内容
                String filePath = folderPath;
                filePath = filePath.toString();
                java.io.File myFilePath = new java.io.File(filePath);
                myFilePath.delete(); //删除空文件夹

        }
        catch (Exception e) {
                System.out.println("删除文件夹操作出错");
                e.printStackTrace();
        }
	}
	
	/**复制文件
	 * @param sourceFile 原文件句柄
	 * @param targetFile 新文件句柄
	 * @throws IOException
	 * @return null
	 */
	public static void copyFile(String  scrPathName, String scrFileName, String desPathName, String desFileName) throws IOException{
		File scrFile = new File(scrPathName+scrFileName);
		File desFile = new File(desPathName+desFileName);
		creatFile(desPathName,desFileName);
		copyFile(scrFile,desFile);
	}
	
	/**复制文件
	 * @param sourceFile 原文件句柄
	 * @param targetFile 新文件句柄
	 * @throws IOException
	 * @return null
	 */
	public static void copyFile(File sourceFile,File targetFile) throws IOException{   
	        // 新建文件输入流并对它进行缓冲    
	        FileInputStream input = new FileInputStream(sourceFile);   
	        BufferedInputStream inBuff=new BufferedInputStream(input);   
	  
	        // 新建文件输出流并对它进行缓冲    
	        FileOutputStream output = new FileOutputStream(targetFile);   
	        BufferedOutputStream outBuff=new BufferedOutputStream(output);   
	           
	        // 缓冲数组    
	        byte[] b = new byte[1024 * 5];   
	        int len;   
	        while ((len =inBuff.read(b)) != -1) {   
	            outBuff.write(b, 0, len);   
	        }   
	        // 刷新此缓冲的输出流    
	        outBuff.flush();   
	        
	        //关闭流    
	        inBuff.close();
	        outBuff.close();
	        output.close();
	        input.close();
	    }

	/**
	 * 复制文件夹
	 * @param sourceDir 原文件夹
	 * @param targetDir 目标文件夹
	 * @throws IOException
	 * @return null
	 */
	 public static void copyDir(String sourceDir, String targetDir) throws IOException {
		 // 新建目标目录
		 (new File(targetDir)).mkdirs();   
		 // 获取源文件夹当前下的文件或目录    
		 File[] file = (new File(sourceDir)).listFiles();   
		 for (int i = 0; i < file.length; i++) {   
			 if (file[i].isFile()) {   
				 // 源文件    
				 File sourceFile=file[i];   
				 // 目标文件    
				 File targetFile=new File(new File(targetDir).getAbsolutePath()+File.separator+file[i].getName());   
				 copyFile(sourceFile,targetFile);
			 }   
		     if (file[i].isDirectory()) {   
		         // 准备复制的源文件夹    
		         String dir1=sourceDir + "/" + file[i].getName();   
		         // 准备复制的目标文件夹    
		         String dir2=targetDir + "/"+ file[i].getName();   
		         copyDir(dir1, dir2);   
		         }   
	     	}
		 }   
	 
	 /**
	  * 检查SD卡是否存在
	  * @param null
	  * @return true-存在 false-不存在
	  */
	 public static boolean sdIsExist(){
		 return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		 
	 }
	 
	 /**
	 * 追加文件内容
	 *
	 * @param fileName
	 * @param content
	 */
	 public static void writeAdd(String filePath, String fileName, String content) {
		 try
		 {
			 FileWriter writer = new FileWriter(filePath+fileName, true);// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件 
			 writer.write(content);
			 writer.close();
		 }catch (IOException e) {
			 e.printStackTrace();
			 }
	 }   
	 
	 /**
	  * 判断文件夹剩余空间
	  * @param path
	  * @return
	  */
	  public static long dirFree(String path)
	   {
	       File pathFile = new File(path);                       // 取得sdcard文件路径  
	       android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());          
	       long nTotalBlocks = statfs.getBlockCount();            // 获取SDCard上BLOCK总数   
	       long nBlocSize = statfs.getBlockSize();                   // 获取SDCard上每个block的SIZE    
	       long nAvailaBlock = statfs.getAvailableBlocks();          // 获取可供程序使用的Block的数量  
	       long nFreeBlock = statfs.getFreeBlocks();                 // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)   
	       long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;   // 计算 SDCard 剩余大小MB 
	       return nSDFreeSize;
	   }
	  
	  /**
	   * 判断文件夹使用空间大小
	   * @param path
	   * @return
	   */
	  public static long dirUsed(String path)
	   {
	       File pathFile = new File(path);                       // 取得sdcard文件路径  
	       android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());          
	       long nTotalBlocks = statfs.getBlockCount();            // 获取SDCard上BLOCK总数   
	       long nBlocSize = statfs.getBlockSize();                   // 获取SDCard上每个block的SIZE    
	       long nAvailaBlock = statfs.getAvailableBlocks();          // 获取可供程序使用的Block的数量   
	       long nFreeBlock = statfs.getFreeBlocks();                  // 获取剩下的所有Block的数量(包括预留的一般程序无法使用的块)   
	       long nSDFreeSize = nAvailaBlock * nBlocSize / 1024 / 1024;   // 计算 SDCard 剩余大小MB
	       long nSDTotalSize = nTotalBlocks * nBlocSize / 1024 / 1024;  // 计算SDCard 总容量大小MB
	      long nSDUsedSize  = (nTotalBlocks - nAvailaBlock) * nBlocSize/1024/1024;    
	      return  nSDUsedSize ; 
	   }
	  
	  /**
	   * 获取文件大小？？
	   */
	  public static int fileSize(String pathName, String fileName) throws IOException{
		  File file = new File(pathName+ fileName);
          FileInputStream fis;
          fis = new FileInputStream(file);
         return fis.available();
	  }
	  
	  /**
	   * 判断SD卡是否存在，不存在---0，存在----1
	   * @return 
	   */
	  public static Boolean SdExist(){
		  return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	  }
	  
	  /**
	   * 获取手机内存目录
	   * @return FILE 返回句柄
	   */
	  public static File getLocalPath(){
		  return myActivity.getFilesDir();
	  }
	  
	  /**
	   * 获取SD卡目录
	   * @return FILE 返回句柄
	   */
	  public static File getSdPath(){
			  return Environment.getExternalStorageDirectory();
	  }
	  
	  /** 
		 * 用于文件处理类测试用
		 *
		 * @param null
		 * @return String SD卡路径字符串
		 * @throws IOException 
		 */
		public static void main(String args[]) throws IOException{

			//检测SD卡是否存在
			Log.i(tag,"检测SD卡是否存在,true为正确\n结果:"+sdIsExist());
			
			//获取SD卡路径
			Log.i(tag, "获取SD卡路径,/mnt/sdcard/为正确\n结果:"+getSdpath());
			
			
			//创建文件夹
			creatDir(getSdpath()+"knet");
			Log.i(tag, "创建新文件夹是否成功,true则正确\n结果:"+isExist(getSdpath()+"knet/",""));
			delDir(getSdpath()+"knet");
			
			//删除文件夹
			creatDir(getSdpath()+"knet");
			delDir(getSdpath()+"knet");
			Log.i(tag,"删除文件夹是否成功?false则为删除成功\n结果:"+isExist(getSdpath()+"knet/",""));
			
			//创建新文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			Log.i(tag,"创建新文件是否成功?true则为创建成功\n结果:"+isExist(getSdpath()+"knet/","data.dat"));
			delDir(getSdpath()+"knet");
			
			//删除文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			delFile(getSdpath()+"knet/","data.dat");
			Log.i(tag,"删除文件是否成功?false则为删除成功\n结果:"+isExist(getSdpath()+"knet/","data.dat"));
			delDir(getSdpath()+"knet");
			
			//复制文件夹
			creatDir(getSdpath()+"knetsrc");
			creatFile(getSdpath()+"knetsrc/","data.dat");
			copyDir(getSdpath()+"knetsrc",getSdpath()+"knetdes");
			Log.i(tag,"复制文件夹,true则为成功\n结果:"+isExist(getSdpath()+"knetdes/",""));
			delDir(getSdpath()+"knetsrc");
			delDir(getSdpath()+"knetdes");
			
			//复制文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			copyFile(getSdpath()+"knet/","data.dat",getSdpath()+"knet/","data2.dat");
			delDir(getSdpath()+"knet");
			
			
			//删除文件夹里所有文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data1.dat");
			creatFile(getSdpath()+"knet/","data2.dat");
			delAllFile(getSdpath()+"knet/");
			Log.i(tag,"删除文件夹里所有文件,2个false则正确\n"+isExist(getSdpath()+"kne/t","data.dat")+"&," +
					isExist(getSdpath()+"knet/","data2.dat"));
			delDir(getSdpath()+"knet");
		
			//检查文件是否存在
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			Log.i(tag, "检查文件是否成功,true则正确\n结果:"+isExist(getSdpath()+"knet/","data.dat"));
			delDir(getSdpath()+"knet");
			
			//流形式写入sd卡中
			//TODO:流形式写入SD卡中还未测试
			
			//字符串形式写入sd中/读取文件内容
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			write(getSdpath()+"knet/","data.dat","hello");
			Log.i(tag,"写入读取文件内容,输出hello则正确\n结果:"+read(getSdpath()+"knet/","data.dat"));
			delDir(getSdpath()+"knet");
			
			//清空文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			write(getSdpath()+"knet/","data.dat","hello");
			clean(getSdpath()+"knet/","data.dat");
			Log.i(tag,"读出数据为空,则正确\n结果:"+ read(getSdpath()+"knet/","data.dat"));
			
			//追加写入文件
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			write(getSdpath()+"knet/","data.dat","hello");
			writeAdd(getSdpath()+"knet/","data.dat","add");
			Log.i(tag,"写入读取文件内容,输出helloadd则正确\n结果:"+read(getSdpath()+"knet/","data.dat"));
			delDir(getSdpath()+"knet");
			
			//判断文件夹剩余空间大小
			Log.i(tag, "文件夹剩余空间大小:\n结果:"+dirFree(getSdpath())+"MB");
			
			//判断文件夹大小
			creatDir(getSdpath()+"knet");
			creatFile(getSdpath()+"knet/","data.dat");
			write(getSdpath()+"knet/","data.dat","hellofdsafdsafdsfdsafdsafsafdsafdsafs");
			Log.i(tag, "文件夹使用空间大小:\n结果:"+dirUsed(getSdpath())+"MB");
			delDir(getSdpath()+"knet");
			
			//判断文件大小
			Log.i(tag, "文件大小:\n结果:"+Integer.toString(fileSize(getSdpath(),"ES_file.apk")));
		}
}


