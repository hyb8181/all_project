package com.tx.farm.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tx.bean.TxMenu;
import com.tx.bean.User;
import com.tx.db.UserDao;
import com.tx.farm.R;
import com.tx.util.JsonParseUtil;
import com.tx.util.UrlConfigUtil;

/**
 * 天下客户端主界面
 * 
 * @author Crane
 * 
 */
public class FarmActivity extends Activity {
	private String TAG = "TxActivity";
	private WebView webView;
	private String url = "http://mobile.tx.com.cn:8081/client/ahref.do";
	private StringBuffer sb = new StringBuffer();
	
	private List<TxMenu> listMenu = new ArrayList<TxMenu>();
	private User user;
	ProgressDialog progressDialog;
	boolean showDialog = false;
	String accessType = "local";
	Bundle savedInstanceState;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);//让进度条显示在标题栏上 
		/*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.tx_activity);
		SharedPreferences sp = getSharedPreferences("imsi.txt", 0);
		String imsi = sp.getString("imsi", "");
		UserDao dao = new UserDao(this);
		user = dao.getUser(imsi);
		
		url = "http://mobile.tx.com.cn:8081/client/ahref.do";
		sb.append("?viewerId=");
		sb.append(user.getUserId());
		sb.append("&imsi=");
		sb.append(user.getImsi());
		sb.append("&phonenum=");
		sb.append(user.getPhoneNum());
		/*sb.append("&type=0");
		url += sb.toString();*/
		
		if (user == null) {
			Toast.makeText(FarmActivity.this, getString(R.string.login_sys_err),
					Toast.LENGTH_SHORT).show();
		} else {
			progressDialog = new ProgressDialog(FarmActivity.this);
			showDialog = true;
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			String loading = getString(R.string.loading);
			progressDialog.setMessage(loading);
			progressDialog.show();
			
			accessType = "local";
			webView = (WebView) findViewById(R.id.txactivity);
			webView.setWebViewClient(new MyWebViewClient(){  
	              public void onProgressChanged(WebView view, int progress) {  
		                //Activity和Webview根据加载程度决定进度条的进度大小  
		               //当加载到100%的时候 进度条自动消失  
		            	  context.setProgress(progress * 100);  
		       }  
		    });
			webView.setWebChromeClient(new MyWebChromeClient());
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);
			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);
			
			webView.loadUrl(url + sb.toString() + "&type=16");
			 
			
		}
		InputStream is;
		try {
			is = getAssets().open(UrlConfigUtil.MENU_URL);
			listMenu = JsonParseUtil.listMenu(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		for (TxMenu txMenu : listMenu) {
			int order = txMenu.getOrder();
			String name = txMenu.getName();
			/*String logo = txMenu.getLogo();
			try {
				InputStream imgIs = getAssets().open(logo);
				Drawable icon = Drawable.createFromStream(imgIs, "icon");
				menu.add(0, 0, order, name).setIcon(icon);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			menu.add(0, 0, order, name);
		}
		return true;
	}
	
	/*public boolean onMenuOpened(int featureId, Menu menu){
		LayoutInflater inflater = (LayoutInflater)   
	       this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);   
	    PopupWindow pw = new PopupWindow(   
	       inflater.inflate(R.layout.popup, null, false),    
	       LayoutParams.FILL_PARENT,
			LayoutParams.WRAP_CONTENT);
		if (pw != null) {
			if (pw.isShowing())
				pw.dismiss();
			else {
				 // 下面的代码假定你的根容器 id为 'main' 也就是parent view   
			    pw.showAtLocation(this.findViewById(R.id.txactivity), Gravity.CENTER, 0, 70);
			    pw.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.icon));
			    pw.setFocusable(false);// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
			    pw.update();
			}
		}
		
		   
	   
		return false;
	}*/
	final Activity context = this;  
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int order = item.getOrder();
		switch(item.getOrder()){
		case 100://退出
			showDialog = false;
			final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			am.restartPackage(getPackageName());
			finish();
			break;
		case 1://空间
			webView.loadUrl(url + sb.toString() + "&type=0");
			break;
		case 2://社区
			webView.loadUrl(url + sb.toString() + "&type=21");
			break;
		case 3://游戏
			webView.loadUrl(url + sb.toString() + "&type=24");
			break;
		case 4://首页
			webView.loadUrl(url + sb.toString() + "&type=20");
			break;
		case 5://导航
			webView.loadUrl(url + sb.toString() + "&type=22");
			break;
		case 6://天下秀
			webView.loadUrl(url + sb.toString() + "&type=17");
			break;
		case 7://农场
			webView.loadUrl(url + sb.toString() + "&type=16");
			break;
		case 8://宠物
			webView.loadUrl(url + sb.append("&type=15").toString());
			break;
		case 9://同城
			webView.loadUrl(url + sb.append("&type=6").toString());
			break;
		case 10://论坛
			webView.loadUrl(url + sb.append("&type=23").toString());
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i(TAG,"url is========>"+url);
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
		}
		
		
		
		

	}

	private class MyWebChromeClient extends WebChromeClient {
		public void onProgressChanged(WebView view, int progress) {
			if(showDialog){
				FarmActivity.this.getWindow().setFeatureInt(Window.FEATURE_PROGRESS, progress*100);
				if(progress==100){
					progressDialog.dismiss();
				}
				progressDialog.setProgress(progress);
			}
			super.onProgressChanged(view, progress);
         }
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			AlertDialog.Builder b = new AlertDialog.Builder(FarmActivity.this);
			b.setTitle("Alert");
			b.setMessage(message);
			b.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					});
			b.setCancelable(false);
			b.create();
			b.show();
			return true;
		};

		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				final JsResult result) {
			AlertDialog.Builder b = new AlertDialog.Builder(FarmActivity.this);
			b.setTitle("Confirm");
			b.setMessage(message);
			b.setPositiveButton(android.R.string.ok,
					new AlertDialog.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							result.confirm();
						}
					});
			b.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							result.cancel();
						}
					});
			b.setCancelable(false);
			b.create();
			b.show();
			return true;
		};
		 
		
	}
	
	public void showUpdateDialog() {   
        @SuppressWarnings("unused")   
        AlertDialog alert = new AlertDialog.Builder(this)   
                .setTitle("提示")   
                .setIcon(R.drawable.tx)   
                .setMessage("确定退出天下社区?")   
                .setPositiveButton("确定",   
                        new DialogInterface.OnClickListener() {   
                            public void onClick(DialogInterface dialog,   
                                    int which) {   
                                finish(); 
                            }   
                        })   
                .setNegativeButton("取消",   
                        new DialogInterface.OnClickListener() {   
                            public void onClick(DialogInterface dialog,   
                                    int which) {   
                                dialog.cancel();   
                            }   
                        }).show();   
    }
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		/*if(keyCode == KeyEvent.KEYCODE_MENU){
			Intent intent = new Intent(TxActivity.this, MenuLayout.class);
			startActivity(intent);
			return true;
		}*/
		
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		
		/*Toast.makeText(TxActivity.this, getString(R.string.is_first_page), Toast.LENGTH_LONG).show();
		this.onCreate(savedInstanceState);*/
		showUpdateDialog();
		return true;
	}
	
	
}