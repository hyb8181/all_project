package com.android.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
/**
 * 对网络数据进行处理
 * @author Administrator
 */
public class WebdataUtil {
	public static final String host="http://211.95.18.70:8083"; //192.168.1.119:8083  211.95.18.70:8083
	//查询是否有其他用户请求自己的机主名片
	public static final String REQUEST="/contacts/namecardRequest/retrieveRequests.do?encryptedUserInfo=%s";
	//查询是否有其他用户分享机主名片给自己（轮询）
	public static final String SHARINGS="/contacts/namecardSharing/retrieveSharings.do";
	//查询是否有其他用户分享联系人给自己（轮询）
	public static final String SHAREDCONTACTS="http://192.168.1.119:8080/contacts/namecardSharing/retrieveSharedContacts.do?encryptedUserInfo=%s";
	//查询是否用户新建或修改联系号码时，请求x->)B关系的机主名片给B
	public static final String UPDATEPHONE="/contacts/namecardSharing/retrieveAwaitPushingNamecard.do";
	//上传电话号码到服务器端进行比对
	public static final String MATCHPHONETOSERVER="/contacts/namecardSharing/retrieveRegisteredContacts.do";
	//向选择的联系人分享机主名片
	public static final String SHARENAMECARD="/contacts/namecardSharing/shareNamecard.do";
	//编辑机主名片
	public static final String EDITNAMECARD="/contacts/namecard/updateNamecard.do";
	//确认或者拒绝机主名片
	public static final String ACCEPTREQUEST="/contacts/namecardSharing/feedbackSharingNamecard.do?encryptedUserInfo=%s&namecardIdParam=%s&feedbackParam=%s";
	//查询是否有机主名片更新
	public static final String CHECKCARDUPDATE="/contacts/namecard/retrieveUpdates.do";
	//请求机主名片
	public static final String REQUESTUSERCARD="/contacts/namecardRequest/requestNamecard.do";
	//同意请求机主名片
	public static final String ACCEPTCARD="/contacts/namecardRequest/feedbackRequestNamecard.do";
	//删除机主名片
	public static final String DELETECARD="/contacts/namecard/deleteNamecard.do";
	public static String getWebData(String path) throws Exception{
		Log.i("PATH", path);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		Log.i("CODE", conn.getResponseCode()+"");
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			byte[] result = readInputStream(inStream);
			String json = new String(result, "UTF-8");
			return json;
		}
		return null;
	}
	/**
	 * 第一次分享联系人,上传所有联系人id,电话号码
	 * @param context 内容上下文
	 * @return 返回名片信息集合
	 */
	/**
	 * 分享联系人
	 * @param context 内容上下文
	 * @param list 已注册本软件的card集合
	 */
	public static void dealRequest(){}
	/**
	 * 以POST方式进行连接,获得返回的字节数组
	 */
	public static String sendPostRequest(String path,byte[] data) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setConnectTimeout(5 * 1000);
		conn.setDoOutput(true);// 能过post方式提交数据，必须要允许输出
		conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
		OutputStream outStream = conn.getOutputStream();
		outStream.write(data);
		outStream.flush();
		outStream.close();
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			byte[] result = readInputStream(inStream);
			return new String(result, "UTF-8");
		}
		return null;
	}
	
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();// 网页的二进制数据
		outStream.close();
		inStream.close();
		return data;
	}
	/**
	 * 访问card信息
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String getCardData(String path) throws Exception{
		URL url = new URL("http","192.168.2.119",8080,path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setConnectTimeout(5 * 1000);
		if (conn.getResponseCode() == 200) {
			InputStream inStream = conn.getInputStream();
			byte[] result = readInputStream(inStream);
			String json = new String(result, "UTF-8");
			return json;
		}
		conn.disconnect();
		return null;
	}
	/**
	 * httpClient post请求
	 * @param url url
	 * @param nameValuePairs 参数列表
	 * @return 返回结果
	 * @throws Exception 
	 */
	public static String doPost(String url,List<NameValuePair> nameValuePairs) throws Exception{
		StringBuffer sb=new StringBuffer();
        HttpPost httppost = new HttpPost(url);
        BasicHttpParams httpParameters = new BasicHttpParams();// Set the timeout in milliseconds until a connection is established.      
        HttpConnectionParams.setConnectionTimeout(httpParameters,5000);// Set the default socket timeout (SO_TIMEOUT) // in milliseconds which is the timeout for waiting for data.      
        HttpConnectionParams.setSoTimeout(httpParameters, 5000);  
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8));
	    //新建HttpClient对象    
        HttpClient httpclient = new DefaultHttpClient(httpParameters);  
	    HttpResponse response = httpclient.execute(httppost);
	    if(response.getStatusLine().getStatusCode()==200){
	    	HttpEntity httpEntity = response.getEntity();
	        //将H中返回实体转化为输入流
	        InputStream is = httpEntity.getContent();
	        //读取输入流，即返回文本内容
	         String line="";
	        BufferedReader br = new BufferedReader(new InputStreamReader(is));
	        while((line=br.readLine())!=null){
	        	sb.append(line);
	        }
	    }
        return sb.toString();
	}
}
