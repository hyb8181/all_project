package com.tx.farm.activity;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.ImageView;

import com.tx.db.AppDao;
import com.tx.farm.service.login.LoginService;
import com.tx.farm.R;
import com.tx.util.JsonParseUtil;
import com.tx.util.UrlConfigUtil;
/**
 * 填写客户端闪屏界面
 * @author Crane
 *
 */
public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";
	// stayTime：闪屏界面停留的时间(毫秒)
	private long stayTime = 2000;
	// isStop: 程序暂停在闪屏界面
	private boolean isStop = false;
	// isOver：跳过闪屏，快速进入主程序界面
	private boolean isOver = true;

	private Intent loginServiceIntent;
	//private Intent installAppServiceIntent;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		ImageView imgView = (ImageView) findViewById(R.id.splash);
		try {
			InputStream imgConfigIs = getAssets().open(
					UrlConfigUtil.SPLASH_URL);
			String imageUrl = JsonParseUtil.getSplashAddr(imgConfigIs);

			InputStream imgIs = getAssets().open(imageUrl);
			Bitmap bitMap = BitmapFactory.decodeStream(imgIs);
			Matrix matrix = new Matrix();
			Bitmap bm = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(),
					bitMap.getHeight(), matrix, true);
			imgView.setImageBitmap(bm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSubscriberId();
		Log.i(TAG,"============IMSI IS:"+imsi);
		if(imsi==null||imsi.equals("")){
			//TODO此处进行判断
		}
		checkUser(imsi);
		
		SharedPreferences sp = getSharedPreferences("imsi.txt", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("imsi", imsi);
		editor.commit();
		
		
		
		boolean isExist = new AppDao(this).isExist();
		Log.i(TAG,"app is exist:"+isExist);
		//TODO 若需要预装，此处取消注释
		/*if(!isExist){
			installApp();
		}else{
			//TODO，到服务器进行判断是否有更新操作
		}*/
		
		Splash splash = new Splash();
		Thread th = new Thread(splash);
		th.start();
	}
	void checkUser(String imsi){
		loginServiceIntent = new Intent(this,LoginService.class);
		loginServiceIntent.putExtra("imsi", imsi);
		startService(loginServiceIntent);
	}
	
	/*void installApp(){
		installAppServiceIntent = new Intent(this,InstallAppService.class);
		installAppServiceIntent.putExtra("zipNameId", R.raw.txmobile);
		startService(installAppServiceIntent);
	}*/
	
	class Splash extends Thread {
		public void run() {
			try {
				long ms = 0;
				while (isOver && ms < stayTime) {
					sleep(100);
					if (!isStop) {
						ms += 50;
					}
				}
				Intent intent = new Intent(SplashActivity.this,
						TxActivity.class);
				startActivity(intent);
				Log.i(TAG,"********************");
			} catch (Exception e) {
				Log.e("Splash", e.getMessage());
			} finally {
				finish();
			}
		}
	}

	

	
	protected void onPause() {
		super.onPause();
		isStop = true;
	}

	protected void onResume() {
		super.onResume();
		isStop = false;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		super.onKeyDown(keyCode, event);
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			isOver = false;
			break;
		case KeyEvent.KEYCODE_BACK:
			android.os.Process.killProcess(android.os.Process.myPid());
			break;
		default:
			break;
		}
		return true;
	}

}
