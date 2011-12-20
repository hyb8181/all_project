package com.AndroidTool;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.os.Bundle;
/******************************************************************************
 * <p>学习JNI调用</p>
 * 
 * @author  	汪军
 * @version 	1.00 2011/12/15 汪军
 * <p>      		1.01 2011/12/15 汪军 修改类名
 * @see     	
 ******************************************************************************/
public class NdkMainActivity extends Activity {
	private static final String TAG = "NdkMainActivity";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView  tv = new TextView(this);
        tv.setText(ndkStringFromJNI());
        setContentView(tv);
        Log.i(TAG, "开始性能比较");
        /*性能比较*/
        ndkAnalysisPerformance();//调用JNI性能测试
        analysisPerformance();//调用JAVA性能测试
    }
    /******************************************************************************
     * 测试JAVA性能
     * @param  	null
     * @return 	null
     * @throws 	null
     * @see    		null
     ******************************************************************************/
    private static final void analysisPerformance(){
        long start_time; 
        long end_time; 
        start_time=System.currentTimeMillis();
    	for(int j = 0; j < 1000; j++)
    	{
    		for(int k = 0; k < 10000; k++)
    		{
    		}
    	}  
        end_time=System.currentTimeMillis();
        Log.i(TAG, "JAVA性能测试:"+(Long.toString(end_time -  start_time)));
    }
    
    /******************************************************************************
     * NDK函数声明,加载库声明
     ******************************************************************************/    
    public native String  ndkStringFromJNI();
    public native String  ndkAnalysisPerformance();
    static {
        System.loadLibrary("ndk_main");
    }

}