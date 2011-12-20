package com.AndroidTool.suggest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.AndroidTool.R;
import com.android.model.SettingSuggestRequestInfo;
import com.android.model.SettingSuggestResponseInfo;
import com.android.utils.WebdataUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

/**
 * 设置-反馈建议界面
 * @author wj
 * 
 */
public class SettingSuggestActivity extends Activity{

	private static final String TAG = "SettingSuggestActivity";
	//TODO:上线前修改IP地址
	public static final String SUGGEST_SERVER = "http://192.168.1.155:8082/software/suggest/";
	public static final String SUGGEST_VERJSON = "suggest.do?";		

	public static final int SUBMIT_FAIL = 500;
	public static final int SUBMIT_SUCCESS = 200;
	public static final int SUBMIT_INPUT_ERROR = 100;
	public static final String SUBMIT_RESPONSE_SUCCESS  = "{\"code\":200}";
	List<SettingSuggestResponseInfo> listQuestInfo = new ArrayList<SettingSuggestResponseInfo>();
	private ProgressDialog pDialog;
	private Button submit;
	private ListView listView;
	private EditText input;
	private SettingSuggestListViewAdapter listViewAdapter;
	//ArrayList<SettingSuggestResponseInfo> questinfo = new ArrayList<SettingSuggestResponseInfo>();
   	
	private List<Map<String, Object>> listItems;
	private Integer[] imgeIDs = {
			R.drawable.setting_suggest_closed,
			R.drawable.setting_suggest_opened};
	String suggestion = null;
	
    /** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_suggest_activity);
		
		input= (EditText)findViewById(R.id.input);
		submit = (Button)findViewById(R.id.bt1);
        listView = (ListView)findViewById(R.id.list_qa); 
        listItems = getListItems();
        listViewAdapter = new SettingSuggestListViewAdapter(this, listItems); //创建适配器
        listView.setAdapter(listViewAdapter);
        

        suggestion=String.valueOf(this.getIntent().getStringExtra("suggestion"));//获取反馈建议内容
        Log.i(TAG, "反馈建议内容:"+suggestion);
        if((!suggestion.equals("null"))&&(!suggestion.equals(""))){
        	input.setText(suggestion);
        }
        input.setOnFocusChangeListener(new OnFocusChangeListener() {  
            @Override  
            public void onFocusChange(View v, boolean hasFocus) {  
                if (!hasFocus) {  
                    Log.i(TAG,"失去焦点");
                    // 失去焦点  
 //                   input.clearFocus();  
                    	//input.setInputType(InputType.TYPE_CLASS_TEXT);
                   // InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
                  //  imm.hideSoftInputFromWindow(input.getWindowToken(), 0);  

                } 
                else{
       //         	   input.clearFocus();  
                	//   input.setInputType(InputType.TYPE_CLASS_TEXT);
                    Log.i(TAG,"得到焦点");

                }
            }  
        });  
        listView.setOnItemClickListener(new OnItemClickListener() {
        	
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Map<String,Object> map= listItems.get(position);
				TextView feedback = (TextView)arg1.findViewById(R.id.replied_contant);
				ImageView state = (ImageView)arg1.findViewById(R.id.imageItem);
				Log.i(TAG, "是否已选中"+Boolean.toString(listQuestInfo.get(position).state));
				if(listQuestInfo.get(position).state){//如果是展开状态,则隐藏处理
					feedback.setVisibility(View.GONE);
					listQuestInfo.get(position).state = false;
					state.setSelected(false);
					state.setImageResource(imgeIDs[0]);
				}
				else{
					if(map.get("feedback").toString().length()>0){//如果是关闭,则打开列表内容
						//有回复
						feedback.setVisibility(View.VISIBLE);
						feedback.setText(map.get("feedback").toString());
						listQuestInfo.get(position).state = true;//被选中
						state.setImageResource(imgeIDs[1]);
					}
				}
			}
		});
    	//点击提交信息
        submit.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
            	if(input.getText().toString().length() < 10){
            		createMessageBox("输入内容过少,请重新输入!");
            	}
            	else{
             		createProcessDialog();
            		new Thread(){
            			public void run(){
                			sendrequest();
            			}
            		}.start();
            	}
        	}
        });
	}
    /**
	 * handle处理,用于联网后返回数据自动处理
	 */
    Boolean result;
	Handler h = new Handler(){
		public void handleMessage (Message msg)
        {
            switch(msg.what)
            {
            case SUBMIT_FAIL:
            	//提交失败
            	pDialog.dismiss();
            	createMessageBox("提交失败,请稍后重试!");
            	break;
            case SUBMIT_SUCCESS:
            	pDialog.dismiss();
            	Log.i(TAG, "输入框内容:"+input.getText().toString());
            	//保存到数据库中,刷新界面
            	SettingSuggestService service = new SettingSuggestService(SettingSuggestActivity.this);
            	result = service.insert(input.getText().toString(), "");
            	refresh();
            	Log.i(TAG, "true为插入成功,false为失败"+Boolean.toString(result));
            	createMessageBox("提交成功!");
            	break;
            case SUBMIT_INPUT_ERROR:
            	createMessageBox("输入内容过少,请重新输入!");
            	break;
            }
        }
    };
    /**
     * 刷新界面
     */
    private void refresh(){
    	//取出来,再SETADAPTER
    	SettingSuggestService service = new SettingSuggestService(SettingSuggestActivity.this);
    	listQuestInfo = service.getAllSuggestInfo();
        listItems = getListItems();
        listViewAdapter = new SettingSuggestListViewAdapter(this, listItems); //创建适配器
        listView.setAdapter(listViewAdapter);
    }
    /**
     * 初始化问题列表
     */
    private List<Map<String, Object>> getListItems() {

    	//读取数据库内容
    	SettingSuggestService service = new SettingSuggestService(SettingSuggestActivity.this);
    	listQuestInfo = service.getAllSuggestInfo();
    	
       	List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
    	for(int i = 0; i < listQuestInfo.size(); i++) {
    		listQuestInfo.get(i).state = false;
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("image", imgeIDs[0]);	//图片资源
    		map.put("suggest", listQuestInfo.get(i).suggest);//建议
    		map.put("feedback", listQuestInfo.get(i).feedback);//反馈
    		if(listQuestInfo.get(i).feedback.length() >0){//如果有反馈,则显示已解答
    			map.put("answered", "1");	
    		}
    		map.put("state", "0");//表示未显示解答内容
    		listItems.add(map);
    	}
    	return listItems;
    }
    
	/**
	 * 获取手机imei号
	 * @return Imei号
	 */
	public String getImei(){
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getSimSerialNumber();
	}
	
    /**
     * 发送消息至服务器
     */
    public void sendrequest(){
    	String strRequest;
    	SettingSuggestRequestInfo sInfo = new SettingSuggestRequestInfo();
    	//TODO:入口处初始化完成后删除此处内容
    	Message msg = new Message();
    	sInfo.imei = getImei();
    	sInfo.suggest = input.getText().toString();
    	strRequest = "imei="+sInfo.imei+"&"+"title="+sInfo.suggest;
	
    	try {
    		Log.i(TAG, "发送数据:"+SUGGEST_SERVER+SUGGEST_VERJSON+""+strRequest);
    		String strResponse = WebdataUtil.getWebData(SUGGEST_SERVER+SUGGEST_VERJSON+""+strRequest);
    		Log.i(TAG, "接收数据:"+strResponse);
    	
    		if(strResponse.equals(SUBMIT_RESPONSE_SUCCESS))
    		{
    			Log.i(TAG,"发送成功");
    			msg.what = SUBMIT_SUCCESS;
              	h.sendMessage(msg);
    		}
    		else{
    			Log.i(TAG,"发送失败");
    		   	msg.what = SUBMIT_FAIL;
    		   	h.sendMessage(msg);
    		}
		} catch (Exception e) {
				e.printStackTrace();
    		   	msg.what = SUBMIT_FAIL;
    		   	h.sendMessage(msg);
		}
    }
    /**
     * 创建进度条提示框
     * 
     */
	 private void createProcessDialog() {
		 pDialog = new ProgressDialog(this); // 创建ProgressDialog对象
         pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // 设置进度条风格，风格为圆形，旋转的  
         pDialog.setTitle("提示");// 设置ProgressDialog 标题       
         pDialog.setMessage("正在提交,请稍后");  // 设置ProgressDialog提示信息  
         //pDialog.setIcon(R.drawable.img1);  // 设置ProgressDialog标题图标  
         pDialog.setIndeterminate(false);   // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确
         pDialog.setCancelable(true);    // 设置ProgressDialog 是否可以按退回键取消  
         //pDialog.setButton("取消", new Bt1DialogListener());  // 设置ProgressDialog 的一个Button  
         pDialog.show();    // 让ProgressDialog显示  
	    } 
	 
	 	/**
	 	 * 创建提示框
	 	 * @param message
	 	 */
	    private void createMessageBox(String message){
		    AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    builder.setMessage(message)
		           .setCancelable(false)
		           .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		               public void onClick(DialogInterface dialog, int id) {
		               }
		           });
		    builder.show(); 
	    }
}
