package com.ruysing.filemanager;

import java.io.File;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.ruysing.filemanager.bean.MyFile;
/**
 * 掃描sdcard文件
 * @author RuySing By 2010-10-28 08:47
 */
public class SearchFileThread implements Runnable {
	private MainActivity context;
	private String keyword;
	private File ROOT_FILE = Environment.getExternalStorageDirectory();
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			context.refreshGridView();
		};
	};
	public SearchFileThread(MainActivity context,String keyword){
		this.context = context;
		this.keyword = keyword;
	}	
	@Override
	public void run() {
		Looper.prepare();
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			return;
		}
		MyFile root = new MyFile();
		root.setFile(ROOT_FILE);
		root.setIcon(context.getFolderIcon());
		root.setAliasName("根目录");
		root.setAliasPath("/sdcard");
		context.clearData();
		context.addData(root);
//		mHandler.sendEmptyMessage(0);
		searchFile(ROOT_FILE);
		mHandler.sendEmptyMessage(0);
	}
	private void searchFile(File dir){
		File[] files = dir.listFiles();
		if(files != null){
			for(File f : files){				
				if(f.getName().toLowerCase().trim().indexOf(keyword.toLowerCase().trim()) != -1){
					context.show(f.getName());
					MyFile mf = new MyFile();
					mf.setFile(f);
					mf.setAliasPath(f.getPath());
					if(f.isDirectory()){
						mf.setIcon(context.getFolderIcon());
					}else{
						mf.setIcon(context.getFileIcon());
					}
					context.addData(mf);
					if(f.isDirectory()){
						searchFile(f);
					}
				}				
			}
		}
	}	
}
