package bi.lan.common;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author yuhi
 * @date 2018年1月31日 下午2:28:25
 */
public class ZHttpClient {

	public static String get(String url) {
		String html = null;
		CloseableHttpClient client = HttpClients.custom().build();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpget);
			int resStatus = response.getStatusLine().getStatusCode();
			if (resStatus == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					html = EntityUtils.toString(entity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}

}
