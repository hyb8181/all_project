package com.AndroidTool.update;

import java.io.File;   
import java.io.FileOutputStream;   
import java.io.IOException;
import java.io.InputStream;   

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.AndroidTool.R;
import com.android.model.SettingUpdateLocalInfo;
import com.android.model.SettingUpdateServerInfo;
import com.android.utils.WebdataUtil;

import android.app.Activity;   
import android.app.AlertDialog;   
import android.app.Dialog;
import android.app.ProgressDialog;   
import android.content.Context;   
import android.content.DialogInterface;   
import android.content.Intent;   
import android.content.pm.PackageManager.NameNotFoundException;   
import android.net.Uri;   
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;   
/**  
 * 自动更新
 * @author wj
 */  
public class SettingUpdateActivity{
	
	private static Activity myActivity;
	public static final String UPDATE_SERVER = "http://192.168.1.155:8082/software/version/";
	public static final String UPDATE_VERJSON = "upload.do";
	public static final String UPDATE_SAVENAME = "contactsupdate.apk";
	//TODO:这里要改成包的名字
	public static final String PACKAGE_NAME = "com.knet.contacts";
	//TODO:发布版本时需要改写渠道号,默认0000
	public static final String CHANNEL = "0000";
	//TODO:发布版本时需要改写此项内容
	public static final String PVERSION = "1.2.1";
	
	public final static int CONNECT_BUSY = 100;
	public final static int CONNECT_REQUEST= 101;
	public final static int CONNECT_ERROR = 102;
	public final static int CONNECT_FAIL = 103;
	public final static int CONNECT_LASTEST = 104;//最新版本
	public final static int CONNECT_UPDATE = 105;//进行升级
	
	private static final String TAG = "SettingUpdateActivity";
	
	private static ProgressDialog pDialog;
	
	private static ProgressDialog pBar;
	
	private static Handler handler = new Handler();

	private static SettingUpdateServerInfo serverinfo;
	private static SettingUpdateLocalInfo myinfo;
	
	public SettingUpdateActivity(Activity act){
		myActivity = act;
	}

	/**
	 * 软件更新入口
	 */
	public void startUpdate(){
		Log.i(TAG, "软件更新入口");
		//显示正在查询中,请稍后
		if(!sdExist()){
			createMessageBox("请检测存储卡!");
			return;
		}
		createProcessDialog();
		new Thread(){
			public void run(){
				  Message msg = new Message();
				//联网检查版本
				if(getServerVerCode()){//获取成功/如果当前版本小于服务器版本,则下载软件
					if(serverinfo.latest.equals("1")){
						//提示用户是否升级
						Log.i(TAG, "提示用户是否升级");
						msg.what = CONNECT_UPDATE;
						h.sendMessage(msg);
					}
					else{
						//提示当前版本已最新
						Log.i(TAG, "提示当前版本已最新");
						msg.what = CONNECT_LASTEST;
						h.sendMessage(msg);
					}
				}
				else{//获取版本失败
					Log.i(TAG,"获取版本失败");
  	              	msg.what = CONNECT_FAIL;
  	              	h.sendMessage(msg);
				}
			}
		}.start();
	}
	/**
	 * handle处理,用于联网后返回数据自动处理
	 */
	Handler h = new Handler(){
		public void handleMessage (Message msg)
        {
            switch(msg.what)
            {
            case CONNECT_FAIL:
            	//更新失败
            	pDialog.dismiss();
            	createMessageBox("产品更新失败请稍后重试");
            	break;
            case CONNECT_LASTEST:
            	//提示已是最新版本
            	notNewVersionShow();
            	break;
            case CONNECT_UPDATE:
            	//用户选择是否是升级
            	pDialog.dismiss();
            	doNewVersionUpdate();
            	break;
            }
        }
    };
    
	/**
	 * 获取服务器软件版本
	 * @return true-获取成功 false-获取失败
	 * 
	 */
	private boolean getServerVerCode(){
		String strDown;
		serverinfo = new SettingUpdateServerInfo();
		
		try {
			strDown = getContent(UPDATE_SERVER + UPDATE_VERJSON);
			Log.i(TAG, "服务器版本信息:"+strDown);
			
			JSONObject json = new JSONObject(strDown);
			serverinfo.changes = json.getString("changes");
			serverinfo.latest = json.getString("latest");
			serverinfo.pversion = json.getString("pversion");
			serverinfo.url = json.getString("url");

			return true;
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return false;
		}
	}
	
	/**
	 * 获取本地版本号
	 * @param context
	 * @return
	 */
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	/**
	 * 获取版本名字
	 * @param context
	 * @return
	 */
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(PACKAGE_NAME, 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}
	/**
	 * 获取应用名称
	 * @param context
	 * @return
	 */
	public static String getAppName(Context context) {
		String verName = context.getResources()
		.getText(R.string.app_name).toString();
		return verName;
	}
	/**
	 * 发现新版本,用户选择是否更新
	 */
	public static void doNewVersionUpdate() {
		final String apkUrl;
		StringBuffer sb = new StringBuffer();
		String changeinfo;
		Log.i(TAG, "更新内容待替换字符："+serverinfo.getchanges());
		//serverinfo.setchanges(StringUtils.str_replace(";",";\n",serverinfo.getchanges()));
		changeinfo = serverinfo.getchanges().replace(";", ";\n");
		changeinfo = serverinfo.getchanges().replace("；", "；\n");
		//serverinfo.setchanges(StringUtils.str_replace("；","；\n",serverinfo.getchanges()));
		Log.i(TAG, "更新内容已替换字符："+changeinfo);
		sb.append("现版本:");
		sb.append(myinfo.getpversion());
		sb.append("\n新版本:");
		sb.append(serverinfo.getpversion());
		sb.append("\n更新内容:\n");
		sb.append(changeinfo);
		apkUrl = serverinfo.geturl();//下载地址字符转换
		apkUrl.replace("\\/", "\\");
		//apkUrl = StringUtils.str_replace("\\/", "\\", serverinfo.geturl());//下载地址字符转换
		Log.i(TAG, "APK地址"+apkUrl);
		Dialog dialog = new AlertDialog.Builder(myActivity)
				.setTitle("产品更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("确定更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(myActivity);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(apkUrl);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								//finish();
							}
						}).create();// 创建
		dialog.show();// 显示对话框
	}
	/**
	 * 外部处理下载
	 * @param strDown
	 */
	public static void doNewVersionUpdate(String strDown) {
		SettingUpdateServerInfo strinfo = null;
		final String apkUrl;
		StringBuffer sb = new StringBuffer();
		String changeinfo;
		try{
			JSONObject json = new JSONObject(strDown);
			strinfo.changes = json.getString("changes");
			strinfo.latest = json.getString("latest");
			strinfo.pversion = json.getString("pversion");
			strinfo.url = json.getString("url");	
		}catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		
		Log.i(TAG, "更新内容待替换字符："+strinfo.getchanges());
		//serverinfo.setchanges(StringUtils.str_replace(";",";\n",serverinfo.getchanges()));
		changeinfo = strinfo.getchanges().replace(";", ";\n");
		changeinfo = strinfo.getchanges().replace("；", "；\n");
		//serverinfo.setchanges(StringUtils.str_replace("；","；\n",serverinfo.getchanges()));
		Log.i(TAG, "更新内容已替换字符："+changeinfo);
		sb.append("现版本:");
		sb.append(myinfo.getpversion());
		sb.append("\n新版本:");
		sb.append(strinfo.getpversion());
		sb.append("\n更新内容:\n");
		sb.append(changeinfo);
		apkUrl = strinfo.geturl();//下载地址字符转换
		apkUrl.replace("\\/", "\\");
		//apkUrl = StringUtils.str_replace("\\/", "\\", serverinfo.geturl());//下载地址字符转换
		Log.i(TAG, "APK地址"+apkUrl);
		Dialog dialog = new AlertDialog.Builder(myActivity)
				.setTitle("产品更新")
				.setMessage(sb.toString())
				// 设置内容
				.setPositiveButton("确定更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								pBar = new ProgressDialog(myActivity);
								pBar.setTitle("正在下载");
								pBar.setMessage("请稍候...");
								pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
								downFile(apkUrl);
							}
						})
				.setNegativeButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// 点击"取消"按钮之后退出程序
								//finish();
							}
						}).create();// 创建
		dialog.show();// 显示对话框
	}
	/**
	 * 弹出已经最新对话框
	 */
	private void notNewVersionShow() {
		int verCode = getVerCode(myActivity);
		String verName = getVerName(myActivity);
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本:");
		sb.append(verName);
		sb.append(" Code:");
		sb.append(verCode);
		sb.append(",\n已是最新版,无需更新!");
		Dialog dialog = new AlertDialog.Builder(myActivity)
				.setTitle("软件更新").setMessage(sb.toString())// 设置内容
				.setPositiveButton("确定",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
								//finish();
							}

						}).create();// 创建
		// 显示对话框
		dialog.show();
	}
	/**
	 * 获取版本信息
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String getContent(String url) throws Exception{
		myinfo = new SettingUpdateLocalInfo();
		byte[] srtbyte = null;
		String strResponse;
		String strUploadData = null;
		
		myinfo.setos("Android"+getPhoneVersion());//设置设备版本
		myinfo.setversion(Integer.toString(getVerCode(myActivity)));//设置软件开发版本号
		myinfo.setpversion(PVERSION);//设置产品版本号
		myinfo.setchannel(CHANNEL);//设置渠道号
		myinfo.setimei(getImei());//设置imei号
		myinfo.setscreen(getScreenPix());//设置当前屏幕大小

		JSONObject json= new JSONObject();

		json.put("os", myinfo.getos());
		json.put("version", myinfo.getversion());
		json.put("pversion", myinfo.getpversion());
		json.put("channel", myinfo.getchannel());
		json.put("imei", myinfo.getimei());
		json.put("screen", myinfo.getscreen());
		
		strUploadData = json.toString();
		
		srtbyte = strUploadData.getBytes("UTF-8");
		Log.i(TAG,"发送地址:"+url+"?json="+strUploadData);
	
		strResponse = WebdataUtil.getWebData(url+"?json="+strUploadData);
		Log.i(TAG,"返回数据:"+strResponse);
		return strResponse;
	} 
	
	/**
	 * 获取手机imei号
	 * @return Imei号
	 */
	public static String getImei(){
		TelephonyManager tm = (TelephonyManager) myActivity.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}
	/**
	 * 获取手机软件版本号
	 * @return手机软件版本号
	 */
	public static String getPhoneVersion(){
		return android.os.Build.VERSION.RELEASE;
	}
	
	/**
	 * 获取手机屏幕大小
	 * @return 屏幕大小-480X800
	 * 
	 */
	private static String getScreenPix(){  
		android.util.DisplayMetrics  dm = new DisplayMetrics(); 
		myActivity.getWindowManager().getDefaultDisplay().getMetrics(dm); 
		String str=dm.widthPixels+"X"+dm.heightPixels; 
		return str;
    }  
	
	/**
	 * 下载文件
	 */
	static //TODO:修改为已封装函数
	void downFile(final String url) {
		
		pBar.show();
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						Log.i(TAG, "下载路径名称"+getCatchPath());
						Log.i(TAG, UPDATE_SAVENAME);
						File file = new File(getCatchPath(), UPDATE_SAVENAME);
						fileOutputStream = new FileOutputStream(file);

						byte[] buf = new byte[1024];
						int ch = -1;
						int count = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							count += ch;
							if (length > 0) {
							}
						}
					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();

	}
	/**
	 * 下载文件?
	 */
	static void down() {
		handler.post(new Runnable() {
			public void run() {
				pBar.cancel();
				update();
			}
		});
	}

	/**
	 * 设置保存APK路径
	 * @return file-文件句柄
	 */
	private static File getCatchPath(){
		  if (sdExist()){//SD卡存在
			  return Environment.getExternalStorageDirectory();
		  }
		  else{//SD卡不存在
			  Log.i(TAG, "设置路径名称"+myActivity.getFilesDir());
			  return myActivity.getFilesDir();
		  }
	}
	/**
	 * 检测SD卡是否可用
	 * @return false-不可用 true-可用
	 */
	private static Boolean sdExist(){
		  if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			  return true;
		  }
		  else{
			  return false;
		  }
	}
	
	/**
	 * 安装软件
	 */
	static void update() {
		
		Log.i(TAG, "开始安装");
		String cmd = "chmod -R +x " +getCatchPath().getPath();
		Log.i(TAG, "cmd"+cmd);
		try {
		    Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		Log.i(TAG, "安装路径名称"+getCatchPath());
		Log.i(TAG, UPDATE_SAVENAME);
		intent.setDataAndType(Uri.fromFile(new File(getCatchPath(), UPDATE_SAVENAME)),
				"application/vnd.android.package-archive");
		myActivity.startActivity(intent);
	}
	/**
	 * 创建等待界面
	 */
	 private void createProcessDialog() {  
		 // 创建ProgressDialog对象  
		 pDialog = new ProgressDialog(myActivity);
         // 设置进度条风格，风格为圆形，旋转的  
         pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
         pDialog.setTitle("提示");// 设置ProgressDialog 标题       
         pDialog.setMessage("正在查询更新信息,请稍后");  // 设置ProgressDialog提示信息  
         //pDialog.setIcon(R.drawable.img1);  // 设置ProgressDialog标题图标  
         pDialog.setIndeterminate(false);   // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
         pDialog.setCancelable(true);    // 设置ProgressDialog 是否可以按退回键取消  
         //pDialog.setButton("取消更新", new Bt1DialogListener());  // 设置ProgressDialog 的一个Button  
         pDialog.show();    // 让ProgressDialog显示  
	    }
	 
	 /**
	  * xhButton01的监听器类
	  */
	    class Bt1DialogListener implements DialogInterface.OnClickListener {  
	        @Override  
	        public void onClick(DialogInterface dialog, int which) {  
	            // 点击“确定”按钮取消对话框  
	            dialog.cancel();  
	        }  
	    }
	    public void createMessageBox(String message){
		    AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
		    builder.setMessage(message)
		           .setCancelable(false)
		           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		            	   myActivity.finish();
		               }
		           });
		    builder.show();
	    }  //TODO:整理下这里的东西
//	public static  void CheckUpdateVersion(){
//		 int mday = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//		 String result = ContactUtil.getSharePerference(myActivity, "data", "softupdate", "");
//		 Log.i(TAG, "系统日期/已存日期"+Integer.toString(mday)+"/"+result);
//		 if(!result.equals(Integer.toString(mday))){
//			    //检查是否有新版本,如果有,则提示出来
//		        SettingUpdateServerInfo serverinfo = new SettingUpdateServerInfo();
//				String strDown = null;
//				serverinfo = new SettingUpdateServerInfo();
//				serverinfo.latest = "0";
//				try {
//					//strDown = WebdataUtil.getWebData(SettingUpdateActivity.UPDATE_SERVER + SettingUpdateActivity.UPDATE_VERJSON+"?json={\"screen\":\"480X854\",\"os\":\"Android2.2\",\"channel\":\"0000\",\"pversion\":\"1.2.1\",\"imei\":\"89014103211118510720\",\"version\":\"1\"}");
//					strDown = getContent(SettingUpdateActivity.UPDATE_SERVER + SettingUpdateActivity.UPDATE_VERJSON);
//					Log.i(TAG, "服务器版本信息:"+strDown);
//					JSONObject json = new JSONObject(strDown);
//					serverinfo.latest = json.getString("latest");
//				}catch (Exception e) {
//					Log.i(TAG, e.getMessage());
//				}				
//				if(serverinfo.latest.equals("1")){
//					Intent myIntent=new Intent(myActivity,AndroidToolActivity.class);
//					//分享机主名片
//					myIntent.putExtra("name","update");
//					
//					//提示用户是否升级
//					Log.i(TAG, "提示用户是否升级");
//					//TODO 自己做shownotification
//					 ContactUtil.showNotification(200,myActivity,"软件更新","软件更新",myIntent);
//				}
//		
//				ContactUtil.writeSharePerference(myActivity, "data", Integer.toString(mday), "softupdate");
//		 }
//	}
}  