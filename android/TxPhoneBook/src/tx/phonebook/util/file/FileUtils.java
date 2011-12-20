package tx.phonebook.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDPATH;

	public String getSDPATH(){
		return SDPATH;
	}
	public FileUtils(){

		SDPATH = Environment.getExternalStorageDirectory() + "/";
		System.out.println(SDPATH);
	}

	public File creatSDFile(String fileName) throws IOException {
		File file = new File(SDPATH + fileName);
		file.createNewFile();
		return file;

	}

	public File creatSDDir(String dirName){
		boolean bresult;
		File dir = new File(SDPATH+dirName);
		bresult = dir.mkdir();
		return dir;
	}

	public boolean isFileExist(String fileName){
		File file = new File(SDPATH+fileName);
		return file.exists();
	}

	public File write2SDFromInput(String path, String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		int temp = 0;
		try{
			creatSDDir(path);
			file = creatSDFile(path+fileName);
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
    public File writeToSDCard(String path, String fileName, String strInput) {
    	File file = null;
        OutputStream output = null;
        try{
        	creatSDDir(path);
            file = creatSDFile(path + "/" + fileName);
            output = new FileOutputStream(file);

	        FileOutputStream stream = new FileOutputStream(file);
	        String s = strInput;
	        byte[] buf = s.getBytes();
	        stream.write(buf);
	        stream.close();

            output.flush();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                output.close();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return file;
    }

    public String readFromSDCard(String path,String fileName){
    	String strSDPath;
    	File file=null;
    	String b;
    	String temp = "";
    	strSDPath = Environment.getExternalStorageDirectory() + "/";
  	  	file= new File(strSDPath+path+"/"+fileName);
	    if (file.isFile())
	    {
	    	try{
	    		BufferedReader sb = new BufferedReader(new FileReader(new File(strSDPath+path+"/"+fileName)));
	    		while((b = sb.readLine()) != null){
	    			temp+=b;
	    			temp+="\n";
	    			}
	    		}
	    	catch (Exception e){
	    		e.printStackTrace();
	    		}
	    }
	    return temp;
    }
}


