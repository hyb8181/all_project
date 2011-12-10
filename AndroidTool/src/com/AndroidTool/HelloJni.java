package com.AndroidTool;

import android.app.Activity;
import android.util.Log;
import android.widget.TextView;
import android.os.Bundle;

public class HelloJni extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView  tv = new TextView(this);
        tv.setText( stringFromJNI() );
        setContentView(tv);
        long start_time;
        long end_time; 
        start_time=System.currentTimeMillis();
        stringFromJNI();
        end_time=System.currentTimeMillis();
        Log.i("HelloJni",""+(Long.toString(end_time -  start_time)));
    }
    public native String  stringFromJNI();
    public native String  unimplementedStringFromJNI();
    static {
        System.loadLibrary("hello-jni");
    }
}