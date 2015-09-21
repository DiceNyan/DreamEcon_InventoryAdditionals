package skymine.redenergy.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import scala.actors.threadpool.Arrays;

/**
 * 
 * @author RedEnergy
 */
public class HttpUtils {
	
	/**
	 * User agent from Google Chrome web browser
	 */
	private static final String BASIC_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36";
	
	/**
	 * @param url of web site to request
	 * @return Content of requested document
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getRequest(String url) throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);
		request.addHeader("User-Agent", BASIC_USER_AGENT);
		HttpResponse response = client.execute(request);
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		reader.lines().forEach(result::append);
		return result.toString();
	}
	
	/**
	 * @param site Url of web site to request
	 * @param args Arguments of request
	 * @return Content of requested document
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String postRequest(String site, BasicNameValuePair ... args) throws ClientProtocolException, IOException{
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(site);
		post.setHeader("User-Agent", BASIC_USER_AGENT);
		post.setEntity(new UrlEncodedFormEntity(Arrays.asList(args)));
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		rd.lines().forEach(result::append);
		return result.toString();
	}
}
