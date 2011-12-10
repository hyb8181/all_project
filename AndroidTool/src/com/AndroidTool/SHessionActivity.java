package com.AndroidTool;

import java.net.MalformedURLException;

import com.android.model.BasicAPI;
import com.caucho.hessian.client.HessianProxyFactory;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

public class SHessionActivity extends Activity {

	Button btn;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.hessionactivity);
		btn = (Button)findViewById(R.id.btuhession);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				// TODO Auto-generated method stub 
				 String url = "http://192.168.1.1:8080/HessianServer/hello.do";
			        HessianProxyFactory factory = new HessianProxyFactory();
			        try {
			            factory.setDebug(true);
			            factory.setReadTimeout(5000);
			            BasicAPI basic = (BasicAPI)factory.create(BasicAPI.class, url,getClassLoader());
//			            Toast.makeText(this, "调用结果:"+basic.hello(), Toast.LENGTH_LONG).show();
			        } catch (MalformedURLException e) {
			            e.printStackTrace();
			        }
			}
		});
		
    }
}