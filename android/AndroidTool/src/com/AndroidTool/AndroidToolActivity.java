package com.AndroidTool;


import com.AndroidTool.R;
import com.AndroidTool.suggest.SettingSuggestActivity;
import com.AndroidTool.update.SettingUpdateTestActivity;
import com.AndroidTool.PopupActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * androidtool 主Activity
 * @version 1.0 wj create
 * @author wj
 *
 */
public class AndroidToolActivity extends Activity {

	Button bt1;
	Button bt2;
	Button bt3;
	Button bt4;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidtoolactivity);
        //测试功能模块
        test.main(null);
        //设置按钮,点击进入弹出框界面
        bt1 = (Button)findViewById(R.id.bt1);
        bt2 = (Button)findViewById(R.id.bt2);
        bt3 = (Button)findViewById(R.id.bt3);
        bt4 = (Button)findViewById(R.id.bt4);
        bt1.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
        		//进入弹出框演示界面
        		startActivity(new Intent(AndroidToolActivity.this,PopupActivity.class));
        	}
        });
        bt2.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
        		//进入提交建议界面
        		startActivity(new Intent(AndroidToolActivity.this,SettingSuggestActivity.class));
        	}
        });
        bt3.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
        		//进入自动更新界面
        		startActivity(new Intent(AndroidToolActivity.this,SettingUpdateTestActivity.class));
        	}
        });
        bt4.setOnClickListener(new OnClickListener(){
        	public void onClick(View arg0){
        		//进入JNI界面
        		startActivity(new Intent(AndroidToolActivity.this,NdkMainActivity.class));
        	}
        });
    }
}