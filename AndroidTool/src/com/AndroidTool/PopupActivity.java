package com.AndroidTool;

import com.AndroidTool.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 弹出框演示界面
 * @author wj
 * @version 1.0 wj create
 * <p>PS:弹出框只能在主线程中跑</p>
 */

public class PopupActivity extends Activity{
	Button bt1;
	Button bt2;
	private ProgressDialog pDialog;
	private AlertDialog alert;
	
	public void onCreate(Bundle saveInstanceState ){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.popupactivity);
		bt1 = (Button)findViewById(R.id.bt1);
		bt2 = (Button)findViewById(R.id.bt2);
		//设置点击显示弹出框
		bt1.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				showPrompt();
			}
		});
		//设置点击显示进度框,默认显示3秒
		//TODO:未完成3秒设置
		bt2.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0){
				CreateProcessDialog();
			}
		});
	}
	
	/**
	 * 创建等待界面
	 */
	 private void CreateProcessDialog() {  
		 
		 // 创建ProgressDialog对象  
		 pDialog = new ProgressDialog(this);
         // 设置进度条风格，风格为圆形，旋转的  
         pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);  

         // 设置ProgressDialog 标题  
         pDialog.setTitle(getString(R.string.prompt));  

         // 设置ProgressDialog提示信息  
         pDialog.setMessage(getString(R.string.busy));  

         // 设置ProgressDialog标题图标  
         //pDialog.setIcon(R.drawable.img1); 

         // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确  
         pDialog.setIndeterminate(false);  

         // 设置ProgressDialog 是否可以按退回键取消  
         pDialog.setCancelable(true);  

         // 设置ProgressDialog 的一个Button  
         pDialog.setButton(getString(R.string.cancle), new Bt1DialogListener());  

         // 让ProgressDialog显示  
         pDialog.show();  
	    }  
	 class Bt1DialogListener implements DialogInterface.OnClickListener{
		 @Override
		 public void onClick(DialogInterface dialog,int which){
			 dialog.cancel();
		 } 
	 }
	    public void showPrompt(){
	        alert = new AlertDialog.Builder(PopupActivity.this).setTitle(getString(R.string.thisissimpleprompt))   
            .create();  
	        alert.show();
	    }
}
