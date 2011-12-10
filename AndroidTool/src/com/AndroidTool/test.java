package com.AndroidTool;

import java.io.IOException;

import com.android.utils.FileUtils;
import com.android.utils.StringUtils;
/**
 * 测试类,用来做测试接口
 * @version 1.00 2011/10/18 wj 创建
 * @author wj
 *
 */
public class test {

	public static void main(String args[]){
		//测试字符串处理类
        StringUtils.main(null);
        
       //测试文件操作处理类
        try {
			FileUtils.main(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
