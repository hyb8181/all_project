package com.AndroidTool.update;

import com.AndroidTool.R;
import com.AndroidTool.R.id;
import com.AndroidTool.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
/**
 * 软件升级测试界面
 * @author wj
 * @version 1.0 wj create
 */

public class SettingUpdateTestActivity extends Activity{
	Button bt1;
	
	public void onCreate(Bundle saveInstanceState ){
		super.onCreate(saveInstanceState);
		setContentView(R.layout.versionupdateactivity);
		bt1 = (Button)findViewById(R.id.btupdate);
	    bt1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				SettingUpdateActivity update = new SettingUpdateActivity(SettingUpdateTestActivity.this);
				update.startUpdate();
			}
			
		});
	}
	
}
