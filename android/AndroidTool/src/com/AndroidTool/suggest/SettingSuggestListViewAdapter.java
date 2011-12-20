package com.AndroidTool.suggest;

import java.util.List;
import java.util.Map;

import com.AndroidTool.R;
import com.AndroidTool.R.id;
import com.AndroidTool.R.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SettingSuggestListViewAdapter extends BaseAdapter {
	private static final String TAG = "SettingSuggestListViewAdapter";
	
	private Context context;						//运行上下文
	private List<Map<String, Object>> questItems;	//意见信息集合
	private LayoutInflater listContainer;			//视图容器
	public final class ListItemView{				//自定义控件集合  
			public ImageView image;  
			public TextView replied;
	        public TextView suggest;
	        public TextView feedback;
	 }
	
	public SettingSuggestListViewAdapter(Context context, List<Map<String, Object>> questItems) {
		this.context = context;			
		listContainer = LayoutInflater.from(context);	//创建视图容器并设置上下文
		this.questItems = questItems;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return questItems.size();
	}

	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * ListView Item设置
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "getView");
		//自定义视图
		ListItemView  listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView(); 
			//获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.setting_suggest_list_item, null);
			//获取控件对象
			listItemView.image = (ImageView)convertView.findViewById(R.id.imageItem);
			listItemView.replied = (TextView)convertView.findViewById(R.id.replied);
			listItemView.suggest = (TextView)convertView.findViewById(R.id.titleItem);
			listItemView.feedback = (TextView)convertView.findViewById(R.id.replied_contant);
			convertView.setTag(listItemView);
		}else {
			listItemView = (ListItemView)convertView.getTag();
		}
		//设置文字和图片
		listItemView.image.setBackgroundResource((Integer) questItems.get(position).get("image"));
		listItemView.suggest.setText((String) questItems.get(position).get("suggest"));
		Log.i(TAG, "suggest:"+questItems.get(position).get("suggest"));
		listItemView.feedback.setText((String)questItems.get(position).get("feedback"));	
		
		
		Log.i(TAG, "反馈字符长度:"+Integer.toString(listItemView.feedback.length()));
		if(listItemView.feedback.length()==0){
			listItemView.feedback.setVisibility(View.GONE);
			listItemView.replied.setVisibility(View.GONE);
		}else{
			listItemView.replied.setVisibility(View.VISIBLE);
		}
			
		
		return convertView;
	}
}
