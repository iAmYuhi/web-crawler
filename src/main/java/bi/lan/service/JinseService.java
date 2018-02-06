package bi.lan.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import bi.lan.common.DataFormat;
import bi.lan.common.ZHttpClient;
import bi.lan.mapper.BiLanMapper;
import bi.lan.model.Jinse;

/**
* @author yuhi
* @date 2018年2月5日 下午5:57:29
*/
@Service
public class JinseService {
	
	private static final Logger logger = LoggerFactory.getLogger(JinseService.class);
	private static final String INDEX_ULR = "http://www.jinse.com/lives";
	private static final String DATA_URL = "http://www.jinse.com/ajax/lives/getList?search=&id=%s&flag=down";
	
	@Autowired
	private BiLanMapper bilanMapper;
	
	public void getNewConsult() {
		try {
			Timestamp jinseMaxTime = bilanMapper.getJinseMaxTime();
			String html = ZHttpClient.get(INDEX_ULR);
			parseHtml(html, jinseMaxTime);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void parseHtml(String html, Timestamp jinseMaxTime) {
		List<Integer> ids = new ArrayList<>();
		Map<Integer, Jinse> map = new HashMap<>();
		try {
			Document doc = Jsoup.parse(html);
			Elements lost = doc.select(".lost");
			Elements lis = lost.select("li");
			String indexMinDataId = "";
			boolean flag = true;
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
				if(dto.getTime().getTime() <= jinseMaxTime.getTime()) {
					flag = false;
				}
				ids.add(dto.getId());
				map.put(dto.getId(), dto);
			}
			if(!map.isEmpty()) {
				List<Jinse> dataList = getList(map, ids);
				if(!dataList.isEmpty()) {
					bilanMapper.batchInsertJinse(dataList);
					logger.info("jinse service insert {} data success!", dataList.size());
				}
			}
			if(flag) {
				getUrlData(indexMinDataId, map, jinseMaxTime, ids);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private List<Jinse> getList(Map<Integer, Jinse> map, List<Integer> ids) {
		List<Jinse> list = new ArrayList<>();
		List<Integer> existIds = bilanMapper.getExistIds(ids);
		for (Integer id : existIds) {
			map.remove(id);
		}
		for (Integer id : map.keySet()) {
			list.add(map.get(id));
		}
		return list;
	}

	private void getUrlData(String indexMinDataId, Map<Integer, Jinse> map, Timestamp jinseMaxTime, List<Integer> list) throws InterruptedException {
		String formatUrl = String.format(DATA_URL, indexMinDataId);
		String data = ZHttpClient.get(formatUrl);
		if(StringUtils.isNotEmpty(data)) {
			Integer minId = null;
			JSONObject jsonData = JSONObject.parseObject(data);
			boolean flag = true;
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
						if(dto.getTime().getTime() <= jinseMaxTime.getTime()) {
							flag = false;
							break;
						}
						logger.info(dto.toString());
						list.add(dto.getId());
						map.put(dto.getId(), dto);
					}
					if(flag) {
						if(jsonArray.size() > 0) {
							getUrlData(minId.toString(), map, jinseMaxTime, list);
						}
					} else {
						break;
					}
				}
			}
		}
	}
	
}
