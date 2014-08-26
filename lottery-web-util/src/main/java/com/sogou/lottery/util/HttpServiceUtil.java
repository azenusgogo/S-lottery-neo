package com.sogou.lottery.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class HttpServiceUtil {
	
	public static Log logger = LogFactory.getLog(HttpServiceUtil.class);
	
	public static final int DEFAULT_CONNECT_TIME_OUT = 600000;
	
	public static final int DEFAULT_READ_TIME_OUT = 600000;
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public static String sendRequest(String url, int connectTimeout, int readTimeout, String charset, boolean returnSingle) {

		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			if (StringUtils.isBlank(charset)) {
				charset = DEFAULT_CHARSET;
			}
			conn = getURLConnection(url, connectTimeout, readTimeout);
			in = new BufferedReader(new InputStreamReader(connect(conn), charset));
			String result = getReturnResult(in, returnSingle);
			return result;
		} catch (Exception e) {
			logger.info("", e);
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		
	}
	
	public static String sendRequest(String url, boolean returnSingle) {

		return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT, DEFAULT_CHARSET, returnSingle);
	}
	
	public static byte[] sendRequest(String url, int connectTimeout, int readTimeout) {

		InputStream is = null;
		HttpURLConnection conn = null;
		try {
			conn = getURLConnection(url, connectTimeout, readTimeout);
			is = connect(conn);
			int size = is.available();
			byte[] result = new byte[size];
			logger.debug("result length:" + size);
			is.read(result, 0, size);
			return result;
		} catch (IOException e) {
			logger.error("", e);
			return null;
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	public static byte[] sendRequest(String url) {

		return sendRequest(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT);
	}
	
	public static String sendPostRequest(String url, String content, String charset) {

		logger.info("url=" + url + ",content=" + content);
		return sendPostRequest(url, content, charset, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT);
	}
	
	public static String sendPostRequest(String url, String content, String charset, int connectTimeout, int readTimeout) {

		return sendPostRequest(url, content, charset, connectTimeout, readTimeout, false);
	}
	
	// public String sendPostRequest(String url, String content, String charset,
	// int connectTimeout, int readTimeout)
	//
	// {
	// return sendPostRequest(url, content, charset, DEFAULT_CONNECT_TIME_OUT,
	// DEFAULT_READ_TIME_OUT, false);
	// }
	
	public static String sendPostRequest(String url, String content, String charset, int connectTimeout, int readTimeout, boolean needCompress, String contentType, boolean returnSingle) {

		BufferedReader in = null;
		HttpURLConnection httpConn = null;
		try {
			httpConn = getURLConnection(url, connectTimeout, readTimeout, contentType);
			if (StringUtils.isBlank(charset)) {
				charset = DEFAULT_CHARSET;
			}
			logger.debug("请求发送地址:" + url);
			logger.debug("参数:" + content);
			InputStream stream = postConnect(httpConn, content, charset, needCompress);
			
			in = new BufferedReader(new InputStreamReader(stream, charset));
			String result = getReturnResult(in, returnSingle);
			return result;
		} catch (IOException e) {
			logger.error("", e);
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				
				if (httpConn != null) {
					httpConn.disconnect();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	public static Map<String,List<String>> getHeaderContentMap(String url) {

		return getHeaderContentMap(url, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT, DEFAULT_CHARSET);
	}
	
	public static Map<String,List<String>> getHeaderContentMap(String url, int connectTimeout, int readTimeout, String charset) {

		BufferedReader in = null;
		HttpURLConnection conn = null;
		try {
			if (StringUtils.isBlank(charset)) {
				charset = DEFAULT_CHARSET;
			}
			conn = getURLConnection(url, connectTimeout, readTimeout);
			String urlStr = conn.getURL().toString();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error(urlStr + "|ResponseCode=" + conn.getResponseCode());
				return null;
			}
			return conn.getHeaderFields();
		} catch (IOException e) {
			logger.error("", e);
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
	}
	
	public static String sendPostRequest(String url, String content, String charset, boolean needCompress) {

		return sendPostRequest(url, content, charset, DEFAULT_CONNECT_TIME_OUT, DEFAULT_READ_TIME_OUT, needCompress);
	}
	
	private static InputStream postConnect(HttpURLConnection httpConn, String content, String charset, boolean needCompress) {

		try {
			if (StringUtils.isBlank(charset)) {
				charset = DEFAULT_CHARSET;
			}
			// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true,
			// 默认情况下是false;
			httpConn.setDoOutput(true);
			// Post 请求不能使用缓存
			httpConn.setUseCaches(false);
			// 设定请求的方法为"POST"，默认是GET
			httpConn.setRequestMethod("POST");
			if (needCompress) {
				sendCompressRequest(content, charset, httpConn);
			} else {
				sendNoCompressRequest(content, charset, httpConn);
			}
			// 接收数据
			if (needCompress) {
				return new GZIPInputStream(httpConn.getInputStream());
			}
			return httpConn.getInputStream();
		} catch (MalformedURLException e) {
			logger.error("", e);
			return null;
		} catch (IOException e) {
			logger.error("", e);
			return null;
		}
	}
	
	private static void sendCompressRequest(String content, String charset, HttpURLConnection httpConn) {

		GZIPOutputStream out = null;
		try {
			httpConn.setRequestProperty("Content-Type", "application/x-gzip");
			httpConn.setRequestProperty("Accept", "application/x-gzip");
			out = new GZIPOutputStream(httpConn.getOutputStream());
			out.write(content.getBytes("GBK"));
			out.flush();
		} catch (IOException e) {
			logger.error("", e);
			return;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 发送原始消息
	 * 
	 * @param content
	 * @param charset
	 * @param httpConn
	 */
	private static void sendNoCompressRequest(String content, String charset, HttpURLConnection httpConn) {

		PrintWriter out = null;
		try {
			out = new PrintWriter(new OutputStreamWriter(httpConn.getOutputStream(), charset));
			out.write(content);
			out.flush();
		} catch (IOException e) {
			logger.error("", e);
			return;
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 建立远程连接
	 * 
	 * @param urlStr
	 * @param connectTimeout
	 * @param readTimeout
	 * @return
	 */
	private static InputStream connect(HttpURLConnection httpConn) {

		String urlStr = httpConn.getURL().toString();
		try {
			if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				logger.error(urlStr + "|ResponseCode=" + httpConn.getResponseCode());
				return null;
			}
			return httpConn.getInputStream();
		} catch (IOException e) {
			logger.error("", e);
			return null;
		}
	}
	
	/**
	 * 构造URLConnnection
	 * 
	 * @param urlStr
	 * @param connectTimeout
	 * @param readTimeout
	 * @param contentType
	 *            content-type类型
	 * @return @
	 */
	private static HttpURLConnection getURLConnection(String urlStr, int connectTimeout, int readTimeout, String contentType) {

		// logger.debug("请求URL:" + urlStr);
		try {
			URL remoteUrl = new URL(urlStr);
			HttpURLConnection httpConn = (HttpURLConnection) remoteUrl.openConnection();
			httpConn.setConnectTimeout(connectTimeout);
			httpConn.setReadTimeout(readTimeout);
			if (contentType != null) {
				httpConn.setRequestProperty("content-type", contentType);// 
				// 需要确认一下
			}
			return httpConn;
		} catch (MalformedURLException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		}
		return null;
	}
	
	private static HttpURLConnection getURLConnection(String urlStr, int connectTimeout, int readTimeout) {

		return getURLConnection(urlStr, connectTimeout, readTimeout, null);
	}
	
	private static String getReturnResult(BufferedReader in, boolean returnSingleLine) throws IOException {

		if (returnSingleLine) {
			return in.readLine();
		} else {
			StringBuffer sb = new StringBuffer();
			String result = "";
			while ((result = in.readLine()) != null) {
				logger.debug("从中心返回：" + result);
				sb.append(StringUtils.trimToEmpty(result));
			}
			return sb.toString();
		}
	}
	
	/**
	 * 用BufferedReader 读取并 排列格式
	 */
	public static String sendRequestReturnString(String url, int connectTimeout, int readTimeout) {

		InputStream is = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			conn = getURLConnection(url, connectTimeout, readTimeout);
			is = connect(conn);
			br = new BufferedReader(new InputStreamReader(is));
			StringBuffer sb = new StringBuffer();
			String tempStr = "";
			while ((tempStr = br.readLine()) != null) {
				sb.append(tempStr + "\n");// 请不要删除这里的\n \n在很多程序中会根据此标识进行splict
				// 因为通过HttpConnection去取出来一段字符串是没有\n标识的
			}
			return sb.toString();
		} catch (Exception e) {
			logger.error("", e);
			return null;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (conn != null) {
					conn.disconnect();
				}
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		
	}
	
	public static String sendPostRequest(String url, String content, String charset, int connectTimeout, int readTimeout, boolean needCompress) {

		return sendPostRequest(url, content, charset, connectTimeout, readTimeout, needCompress, null, false);
	}
	
}
