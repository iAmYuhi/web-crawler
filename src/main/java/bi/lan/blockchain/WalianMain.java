package bi.lan.blockchain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import bi.lan.common.ZHttpClient;
import bi.lan.mapper.BiLanMapper;
import bi.lan.model.Jinse;
import bi.lan.model.Walian;

/**
* @author yuhi
* @date 2018年2月5日 上午10:27:27
*/
@Service
public class WalianMain {

	private static final Logger logger = LoggerFactory.getLogger(WalianMain.class);
	private static final String INDEX_URL = "https://walian.cn/api/article-lists?channelId=26&rows=20&page=%s";
	private volatile List<Walian> list = Collections.synchronizedList(new ArrayList<>());
	private volatile Map<Integer, String> ids = new HashMap<>();
	private static long insertTime = 0L;
	
	@Autowired
	private BiLanMapper bilanMapper;
	
//	@PostConstruct
	public void init() {
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
								bilanMapper.batchInsertWailian(list);
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
		Integer initPageIndex = 0;
		this.parseInterface(initPageIndex);
	}

	private void parseInterface(Integer pageIndex) {
		try {
			String url = String.format(INDEX_URL, pageIndex);
			String htmlData = ZHttpClient.get(url);
			if(StringUtils.isNotEmpty(htmlData)) {
				JSONObject returnData = JSONObject.parseObject(htmlData);
				if(returnData.containsKey("data")) {
					JSONObject data = returnData.getJSONObject("data");
					if(data.containsKey("page") && data.containsKey("list")) {
						JSONObject page = data.getJSONObject("page");
						Integer pages = page.getInteger("pages");
						Integer pageNum = page.getInteger("pageNum");
						JSONArray arr = data.getJSONArray("list");
						for (int i = 0; i < arr.size(); i++) {
							JSONObject obj = arr.getJSONObject(i);
							Walian dto = new Walian();
							dto.setId(obj.getInteger("id"));
							dto.setLogo(obj.getString("logo"));
							dto.setTitle(obj.getString("title"));
							dto.setIntro(obj.getString("intro"));
							dto.setContent(obj.getString("content"));
							dto.setTime(new Timestamp(obj.getLong("pubTime")));
							if(!ids.containsKey(dto.getId())) {
								ids.put(dto.getId(), "");
								list.add(dto);
							}
							logger.info(dto.toString());
						}
						if(pageIndex <= pages) {
							parseInterface(pageNum + 1);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
