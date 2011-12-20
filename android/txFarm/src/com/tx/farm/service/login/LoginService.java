package com.tx.farm.service.login;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.gsm.SmsManager;
import android.util.Log;

import com.tx.bean.User;
import com.tx.db.UserDao;
import com.tx.http.util.HttpUtil;
import com.tx.util.UrlConfigUtil;
/**
 * 用户信息校验服务
 * @author Crane
 *
 */
public class LoginService extends Service {
	private static String TAG = "LoginService";
	private String imsi = "";
	private User user;
	// 是否需要再次发送短信
	private boolean needSmsPoll = false;
	// 是否需要再次到服务端验证注册成功情况
	private boolean needCheckPoll = false;
	// 访问服务器注册情况状态码
	private int regStatus;
	SmsManager smsManager;

	public int getRegStatus() {
		return regStatus;
	}

	public User getUser() {
		return user;
	}

	private LoginServiceBinder loginBinder = new LoginServiceBinder();

	public IBinder onBind(Intent arg0) {
		Log.i("LoginService", "========= onBind");
		return loginBinder;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		imsi = intent.getStringExtra("imsi");
		smsManager = SmsManager.getDefault();
		Log.i("LoginService", "imsi=>" + imsi);
		needSmsPoll = true;
		if (!isRegister()) {
			needCheckPoll = true;
			int sendSmsTimes=0;
			while (needSmsPoll && sendSmsTimes < 3) {
				new Thread() {
					public void run() {
						System.out.println("needSmsPoll=======>" + needSmsPoll);
						smsHandler.handleMessage(smsHandler.obtainMessage());
					}

				}.start();
				sendSmsTimes++;
				try {
					Thread.sleep(9000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	boolean isRegister() {
		String url = UrlConfigUtil.REG_URL;
		Map<String, String> queryParams = new HashMap<String, String>();
		queryParams.put("imsi", imsi);
		String result = HttpUtil.postQueryMap(url, queryParams);
		Log.i(TAG,"register result is===>"+result);
		if (result != null && result.contains("#")) {
			String[] arr = result.split("#");
			if (arr.length == 3) {
				user = new User();
				user.setUserId(Integer.parseInt(arr[0]));
				user.setImsi(arr[1]);
				user.setPhoneNum(arr[2]);
				// TODO
				UserDao dao = new UserDao(this);
				dao.saveOrUpdateUser(user);
				regStatus = 200;
				stopSelf();
				return true;
			}
		}
		regStatus = 415;
		return false;
	}


	Handler smsHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (imsi != null) {
				System.out.println("regStatus is:"+regStatus);
				switch (regStatus) {
				case 415:

					// 未注册的场合，进行注册，发送邮件
					String tel = "1065-71090-88877";// 默认移动用户
					if (imsi.startsWith("46000") || imsi.startsWith("46002")) {// 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号
						// 中国移动
						tel = "1065-71090-88877";
					} else if (imsi.startsWith("46001")) {
						// 中国联通
						tel = "1065-5021-80133";
					} else if (imsi.startsWith("46003")) {
						// 中国电信
						tel = "1065-9020-5111-191";
					}
					String sendSms = "99#" + imsi + "#android#" + "jiuyou_1";
					System.out.println("send sms is:" + sendSms);
					/**
					 * sendTextMessage(String destinationAddress, String
					 * scAddress, String text, PendingIntent sentIntent,
					 * PendingIntent deliveryIntent)
					 */
					smsManager.sendTextMessage(tel, null, sendSms, null, null);

					int regCheckTimes = 0;

					while (needCheckPoll && regCheckTimes < 3) {
						new Thread() {
							public void run() {
									regCheckHandler.handleMessage(regCheckHandler.obtainMessage());
									if (regStatus == 200) {
										needSmsPoll = false;
										needCheckPoll = false;
										return;
									}
								}
						}.start();
						regCheckTimes++;
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					break;
				}

			}
		}
	};

	Handler regCheckHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isRegister();
		}
	};


	public class LoginServiceBinder extends Binder {
		public LoginService getService() {
			return LoginService.this;
		}
	}

}
