package tx.phonebook.util.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 以流的形式拼装POST格式请求报文
 * @author Crane
 *
 */
public class PostJSONTextbyStream {
	protected Socket client;
	protected BufferedOutputStream bos;
	protected BufferedInputStream bis;
	protected ByteArrayInputStream bais;
	protected URL serverUrl;
	private int resCode = -1;
	private String resMsg = "";
	private String serverVersion = "";
	private Properties properties = new Properties();

	public PostJSONTextbyStream() {
	}

	/**
	 * POST方法是向服务器传送数据，以便服务器做出相应的处理。例如网页上常用的
	 * 提交表格。
	 * @param url
	 * @param content
	 * http请求报文格式
	 *
	 * 请求行:请求方法(post/get)+空格+URL+空格+协议版本+回车符+换行符
	 * 请求头:头部字段名+冒号+值+回车符+换行符
	 * 		 头部字段名+冒号+值+回车符+换行符
	 * 			.
	 * 			.
	 * 			.
	 * 		 回车符+换行符
	 * 请求数据：
	 *
	 *
	 *
	 * http响应报文格式
	 *
	 * 状态行:版本+空格+状态码+空格+原因语句+回车符+换行符
	 * 响应头:头部字段名+冒号+值+换行符
	 * 		 头部字段名+冒号+值+回车符+换行符
	 * 			.
	 * 			.
	 * 			.
	 * 		 回车符+换行符
	 * 附属体:
	 */
	public String POST(String url, String json) {
		try {
			//检查网络
			checkHTTP(url);
			//打开通讯通道
			openServer(serverUrl.getHost(), serverUrl.getPort());
			String content = json;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//组装请求行
			write(baos,"POST ");
			write(baos,getURLFormat(serverUrl));
			write(baos," HTTP/1.0\r\n");
			//组装通用请求头部
			write(baos,"User-Agent: myselfHttp/1.0\r\n");
			write(baos,"Accept: www/source; text/html; image/gif; */*\r\n");
			//组装头部其他信息
			write(baos,"Content-type: application/x-www-form-urlencoded\r\n");
			write(baos,"Content-length: ");
			write(baos,""+content.getBytes("utf-8").length);
			write(baos,"\r\n\r\n");
			//组装请求数据
			write(baos,content);
			write(baos,"\r\n");
			sendMessage(baos);
			return receiveMessage();
		} catch (ProtocolException p) {
			p.printStackTrace();
			return "";
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return "";
		} catch (IOException i) {
			i.printStackTrace();
			return "";
		}
	}


	private void write(OutputStream out,String msg){
		try {
			out.write(msg.getBytes("utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查请求地址是否符合http协议规范
	 * @param url
	 * @throws ProtocolException
	 */
	protected void checkHTTP(String url) throws ProtocolException {
		try {
			URL target = new URL(url);
			if (target == null
					|| !target.getProtocol().toUpperCase().equals("HTTP"))
				throw new ProtocolException("这不是HTTP协议");
			this.serverUrl = target;
		} catch (MalformedURLException m) {
			throw new ProtocolException("协议格式错误");
		}

	}

	/**
	 * 与Web服务器连接。若找不到Web服务器，InetAddress会引发UnknownHostException
	 * 异常。若Socket连接失败，会引发IOException异常。
	 * @param host
	 * @param port
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	protected void openServer(String host, int port)
			throws UnknownHostException, IOException {
		properties.clear();
		resMsg = "";
		resCode = -1;
		try {
			if (client != null)
				closeServer();
			if (bais != null) {
				bais.close();
				bais = null;
			}
			InetAddress address = InetAddress.getByName(host);
			client = new Socket(address, port == -1 ? 80 : port);
			bos = new BufferedOutputStream(client.getOutputStream());
			bis = new BufferedInputStream(client.getInputStream());
		} catch (UnknownHostException u) {
			throw u;
		} catch (IOException i) {
			throw i;
		}
	}

	/* 关闭与Web服务器的连接 */

	protected void closeServer() throws IOException {
		if (client == null)
			return;
		try {
			client.close();
			bos.close();
			bis.close();
		} catch (IOException i) {
			throw i;
		}
		client = null;
		bos = null;
		bis = null;
	}

	/**
	 * 组装请求地址
	 * @param target
	 * @return
	 */
	protected String getURLFormat(URL target) {
		String spec = "http://" + target.getHost();
		if (target.getPort() != -1)
			spec += ":" + target.getPort();
		return spec += target.getFile();
	}

	/**
	 * 向Web服务器传送数据
	 * @param data
	 * @throws IOException
	 */
	protected void sendMessage(ByteArrayOutputStream os) throws IOException {
		bos.write(os.toByteArray(), 0, os.size());
		bos.flush();

	}

	/**
	 * 从WEB服务端接收数据
	 * @throws IOException
	 */
	protected String receiveMessage() throws IOException {
		byte data[] = new byte[1024];
		int count = 0;
		int word = -1;
		// 解析第一行（状态行）
		while ((word = bis.read()) != -1) {
			if (word == '\r' || word == '\n') {
				word = bis.read();
				if (word == '\n')
					word = bis.read();
				break;
			}
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
		}
		//对状态行进行处理
		String message = new String(data, 0, count);
		int mark = message.indexOf(32);//空格的ascii码为32
		serverVersion = message.substring(0, mark);
		while (mark < message.length() && message.charAt(mark + 1) == 32)
			mark++;
		resCode = Integer.parseInt(message.substring(mark + 1, mark += 4));
		resMsg = message.substring(mark, message.length()).trim();
		// 应答状态码和处理请读者添加

		switch (resCode) {
		case 400:
			throw new IOException("错误请求");
		case 404:
			throw new FileNotFoundException(getURLFormat(serverUrl));
		case 503:
			throw new IOException("服务器不可用");
		}
		if (word == -1)
			throw new ProtocolException("信息接收异常终止");
		int symbol = -1;
		count = 0;
		// 解析元信息（响应头信息）
		while (word != '\r' && word != '\n' && word > -1) {
			if (word == '\t')
				word = 32;
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
			parseLine: {
				while ((symbol = bis.read()) > -1) {
					switch (symbol) {
					case '\t':
						symbol = 32;
						break;
					case '\r':
					case '\n':
						word = bis.read();
						if (symbol == '\r' && word == '\n') {
							word = bis.read();
							if (word == '\r')
								word = bis.read();
						}
						if (word == '\r' || word == '\n' || word > 32)
							break parseLine;
						symbol = 32;
						break;
					}
					if (count == data.length)
						data = addCapacity(data);
					data[count++] = (byte) symbol;
				}
				word = -1;
			}
			//解析响应报文头
			message = new String(data, 0, count);
			mark = message.indexOf(':');
			String key = null;
			if (mark > 0)
				key = message.substring(0, mark);
			mark++;
			while (mark < message.length() && message.charAt(mark) <= 32)
				mark++;
			String value = message.substring(mark, message.length());
			properties.put(key, value);
			count = 0;
		}

		// 获得正文数据（响应数据体）
		while ((word = bis.read()) != -1) {
			if (count == data.length)
				data = addCapacity(data);
			data[count++] = (byte) word;
		}
		if (count > 0) {
			/*bais = new ByteArrayInputStream(data, 0, count);
			data = null;*/
			String retMsg = new String(data, 0, count);
			System.out.println(retMsg);
			return retMsg;
		}
		closeServer();
		return "";
	}

	public String getResponseMessage() {
		return resMsg;
	}

	public int getResponseCode() {
		return resCode;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public InputStream getInputStream() {
		return bais;
	}

	public synchronized String getHeaderKey(int i) {
		if (i >= properties.size())
			return null;
		Enumeration enu = properties.propertyNames();
		String key = null;
		for (int j = 0; j <= i; j++)
			key = (String) enu.nextElement();
		return key;
	}

	public synchronized String getHeaderValue(int i) {
		if (i >= properties.size())
			return null;
		return properties.getProperty(getHeaderKey(i));
	}

	public synchronized String getHeaderValue(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 向数组中增加数据
	 * @param rece
	 * @return
	 */
	private byte[] addCapacity(byte rece[]) {
		byte temp[] = new byte[rece.length + 1024];
		System.arraycopy(rece, 0, temp, 0, rece.length);
		return temp;

	}
}
