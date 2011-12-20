package com.tx.farm.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.CookieSyncManager;
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
 * 天下游戏厅首页
 * @author Crane
 *
 */
public class TxHallWebActivity extends Activity {
	private WebView webView;

	private List<TxMenu> listMenu = new ArrayList<TxMenu>();
	private User user;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tx_hall_web_activity);
		SharedPreferences sp = getSharedPreferences("imsi.txt", 0);
		String imsi = sp.getString("imsi", "");
		UserDao dao = new UserDao(this);
		user = dao.getUser(imsi);
		if (user == null) {
			Toast.makeText(TxHallWebActivity.this, getString(R.string.login_sys_err),
					Toast.LENGTH_SHORT).show();
		} else {
			webView = (WebView) findViewById(R.id.txhallwebactivity);
			webView.setWebViewClient(new MyWebViewClient());
			webView.setWebChromeClient(new MyWebChromeClient());
			WebSettings settings = webView.getSettings();
			settings.setJavaScriptEnabled(true);
			//settings.setSupportZoom(true);
			//settings.setBuiltInZoomControls(true);
			// 设置滚动条状态
			webView.setHorizontalScrollBarEnabled(false);
			webView.setVerticalScrollBarEnabled(false);
			
			String url = "http://mobile.tx.com.cn:8084/txanhall.jsp";
			StringBuffer sb = new StringBuffer();
			sb.append("?viewerId=");
			sb.append(user.getUserId());
			sb.append("&imsi=");
			sb.append(user.getImsi());
			sb.append("&phonenum=");
			sb.append(user.getPhoneNum());
			url += sb.toString();
			webView.loadUrl(url);
			InputStream is;
			try {
				is = getAssets().open(UrlConfigUtil.MENU_URL);
				listMenu = JsonParseUtil.listMenu(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
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
			String logo = txMenu.getLogo();
			try {
				InputStream imgIs = getAssets().open(logo);
				Drawable icon = Drawable.createFromStream(imgIs, "icon");
				menu.add(0, 0, order, name).setIcon(icon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		int groupId = item.getGroupId();
		int itemId = item.getItemId();
		if (groupId == 0 && itemId == 0) {
			int order = item.getOrder();
			for (TxMenu txMenu : listMenu) {
				int orderTmp = txMenu.getOrder();
				if (order == orderTmp) {
					if (order == 100) {
						final ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
						am.restartPackage(getPackageName());
						finish();
						break;
					}
					if (order == 1) {
						SharedPreferences sp = getSharedPreferences("imsi.txt",0);
						String imsi = sp.getString("imsi", "");
						UserDao dao = new UserDao(this);
						user = dao.getUser(imsi);
						String url = "http://mobile.tx.com.cn:8084/txanhall.jsp";
						StringBuffer sb = new StringBuffer();
						sb.append("?viewerId=");
						sb.append(user.getUserId());
						sb.append("&imsi=");
						sb.append(user.getImsi());
						sb.append("&phonenum=");
						sb.append(user.getPhoneNum());
						url += sb.toString();
						webView.loadUrl(url);
					}

				} else {
					continue;
				}
			}
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			CookieSyncManager.createInstance(TxHallWebActivity.this);
			CookieSyncManager manager = CookieSyncManager.getInstance();
			manager.startSync();
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

		@Override
		public void onLoadResource(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onLoadResource(view, url);
		}

		
		
		
	}

	private class MyWebChromeClient extends WebChromeClient {
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				final JsResult result) {
			AlertDialog.Builder b = new AlertDialog.Builder(TxHallWebActivity.this);
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
			AlertDialog.Builder b = new AlertDialog.Builder(TxHallWebActivity.this);
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		if (keyCode != KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		Intent intent = new Intent();
		intent.setClass(this, TxActivity.class);
		startActivity(intent);
		finish();
		return true;
	}
}