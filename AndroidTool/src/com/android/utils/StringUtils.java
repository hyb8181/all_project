package com.android.utils;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;


/**
 * <p>字符串类相关优化处理</p>
 * 
 * @author  汪军
 * @version 1.0.0 2011/10/18 汪军 创建
 * <p>      1.0.0 2011/10/18 汪军 简化输出字符串</p>
 * <p> 		1.0.1 2011年10月30日 汪军 加入替换字符串</p>
 * <p>		1.0.2 2011年11月18日 汪军 加入字符串转byte方法</p>
 * @see     ?
 */
public class StringUtils{
	
	private static final String TAG = "StringUtils";
	
	/** 
	 * <P>用于测试此类方法<P>
	 */
	public static void main(String args[]){
		LogUtils.l(TAG, "开始测试字符串类");
        StringUtils.pln("测试输出字符串并换行");
        StringUtils.p("测试输出字符串");
        System.out.println();
		StringUtils.pnull();
		LogUtils.l(TAG, "测试字符串类结束");
		String str = "abcdefg";
		//截取第二位到第四位
		String str_part = str.substring(1,4);
		LogUtils.l(TAG, "截取后的字串:"+str_part);
	}

	/** 
	 * <P>打印字符串并换行<P>
	 *
	 * @param string  输入字符串
	 * @return null
	 */
	public static void pln(String s){
		System.out.println(s);
	}
	
	/** 
	 *<P>打印字符串<P>
	 *
	 * @param string  输入字符串
	 * @return null
	 */
	public static void p(String s){
		System.out.print(s);
	}
	
	/** 
	 *<P>打印空字符串<P>
	 *
	 * @param null
	 * @return null
	 */	
	public static void pnull(){
		System.out.println("输出空字符串");
	}
	
	/**
	 * 字符串替换
	 * @param from 替换前字符串
	 * @param to 替换后字符串
	 * @param source 需处理字符串
	 */
	public static String replace(String from,String to,String source) {

	    StringBuffer bf= new StringBuffer("");
	    StringTokenizer st = new StringTokenizer(source,from,true);

	    while (st.hasMoreTokens()) {
	      String tmp = st.nextToken();
	      if(tmp.equals(from)) {
	        bf.append(to);
	      } else {
	        bf.append(tmp);
	      }
	    }
	    return bf.toString();

	  }
	/**
	 * 字符串转整型
	 */
	public static long stringTolong(String str){
		long result;
		result = Long.parseLong(str);
		return result;
	}
	/**
	 * String 转byte[]
	 */
	public static byte[] stringtobyte(String str){
		try {
			return str.getBytes("UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
