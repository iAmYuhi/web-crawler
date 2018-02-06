package bi.lan.blockchain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import bi.lan.common.DataFormat;
import bi.lan.common.ZHttpClient;
import bi.lan.mapper.BiLanMapper;
import bi.lan.model.Jinse;

/**
 * @author yuhi
 * @date 2018年1月31日 上午11:36:50
 */
@Service
public class JinseMain {

	private static final Logger logger = LoggerFactory.getLogger(JinseMain.class);
	private static final String INDEX_ULR = "http://www.jinse.com/lives";
	private static final String DATA_URL = "http://www.jinse.com/ajax/lives/getList?search=&id=%s&flag=down";
	private volatile List<Jinse> list = Collections.synchronizedList(new ArrayList<>());
	private static long insertTime = 0L;
	
	@Autowired
	private BiLanMapper bilanMapper;

//	@PostConstruct
	private void init() {
		ThreadFactory geoHashThreadFactory = new ThreadFactoryBuilder()
				.setNameFormat("geoHashQueue-pool-%d").build();
		ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS,
				new LinkedBlockingQueue<Runnable>(1024), geoHashThreadFactory, new ThreadPoolExecutor.AbortPolicy());
		singleThreadPool.execute(() -> {
			try {
				while(true) {
					long now = System.currentTimeMillis();
					if((now - insertTime) >= 5000) {
						synchronized (list) {
							if(list.size() > 0) {
								bilanMapper.batchInsertJinse(list);
								list.clear();
							}
						}
						insertTime = now;
					}
				}
			} catch (Exception e) {
				logger.error("get list data insert db error:", e);
			}
		});
		String html = ZHttpClient.get(INDEX_ULR);
		parseHtml(html);
	}

	private void parseHtml(String html) {
		try {
			Document doc = Jsoup.parse(html);
			Elements lost = doc.select(".lost");
			Elements lis = lost.select("li");
			String indexMinDataId = "";
			for (Element li : lis) {
				Jinse dto = new Jinse();
				indexMinDataId = li.attr("data-id");
				if(StringUtils.isNotEmpty(indexMinDataId)) {
					dto.setId(Integer.valueOf(indexMinDataId));
				}
				Elements time = li.select(".live-time");
				if(StringUtils.isNotEmpty(time.text().trim())) {
					Timestamp formatData = DataFormat.formatData(time.text());
					if(formatData != null) {
						dto.setTime(formatData);
					}
				}
				Elements liveData = li.select(".live-info");
				dto.setText(liveData.text());
				Elements a = liveData.select("a");
				String href = a.attr("href");
				if(StringUtils.isNotEmpty(href.trim())) {
					dto.setUrl(href);
				}
				logger.info(dto.toString());
				list.add(dto);
			}
			getUrlData(indexMinDataId);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void getUrlData(String indexMinDataId) throws InterruptedException {
		String formatUrl = String.format(DATA_URL, indexMinDataId);
		String data = ZHttpClient.get(formatUrl);
		if(StringUtils.isNotEmpty(data)) {
			Integer minId = null;
			JSONObject jsonData = JSONObject.parseObject(data);
			if(jsonData.containsKey("data")) {
				JSONObject jsonObject = jsonData.getJSONObject("data");
				for (String key : jsonObject.keySet()) {
					JSONArray jsonArray = jsonObject.getJSONArray(key);
					for (int i = 0; i < jsonArray.size(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						minId = obj.getInteger("id");
						Jinse dto = new Jinse();
						if(minId != null) {
							dto.setId(minId);
						}
						dto.setText(obj.getString("content"));
						dto.setUrl(obj.getString("source_url"));
						String time = obj.getString("updated_at");
						if(StringUtils.isNotEmpty(time)) {
							Timestamp formatData = DataFormat.formatData(key, time);
							if(formatData != null) {
								dto.setTime(formatData);
							}
						}
						logger.info(dto.toString());
						list.add(dto);
					}
					if(jsonArray.size() > 0) {
						getUrlData(minId.toString());
					}
				}
			}
		}
	}

}
