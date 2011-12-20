package com.tx.farm.activity;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;

import com.tx.farm.R;

public class MenuLayout extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		TabHost tabHost = getTabHost();
		
		//绑定标签模板，进而绑定其每个标签的内容关联
		LayoutInflater lif = LayoutInflater.from(this);
		lif.inflate(R.layout.menulayout, tabHost.getTabContentView(), true);
		
		tabHost.addTab(tabHost.newTabSpec("spec1").setIndicator("tab1").setContent(R.id.TextView01));
		tabHost.addTab(tabHost.newTabSpec("spec2").setIndicator("tab2").setContent(R.id.TextView02));
		tabHost.addTab(tabHost.newTabSpec("spec3").setIndicator("tab3").setContent(R.id.TextView03));
		
	}
	
}
