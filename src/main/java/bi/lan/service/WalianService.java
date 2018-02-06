package bi.lan.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import bi.lan.common.ZHttpClient;
import bi.lan.mapper.BiLanMapper;
import bi.lan.model.Walian;

/**
* @author yuhi
* @date 2018年2月6日 上午11:49:18
*/
@Service
public class WalianService {
	
	private static final Logger logger = LoggerFactory.getLogger(WalianService.class);
	private static final String INDEX_URL = "https://walian.cn/api/article-lists?channelId=26&rows=20&page=%s";
	
	@Autowired
	private BiLanMapper bilanMapper;
	
	public void getWalan() {
		try {
			Timestamp walanMaxTime = bilanMapper.getWalanMaxTime();
			Integer initPageIndex = 0;
			this.parseInterface(initPageIndex, walanMaxTime);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void parseInterface(Integer pageIndex, Timestamp walanMaxTime) {
		List<Integer> ids = new ArrayList<>();
		Map<Integer, Walian> map = new HashMap<>();
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
						boolean flag = true;
						for (int i = 0; i < arr.size(); i++) {
							JSONObject obj = arr.getJSONObject(i);
							Walian dto = new Walian();
							dto.setId(obj.getInteger("id"));
							dto.setLogo(obj.getString("logo"));
							dto.setTitle(obj.getString("title"));
							dto.setIntro(obj.getString("intro"));
							dto.setContent(obj.getString("content"));
							dto.setTime(new Timestamp(obj.getLong("pubTime")));
							if(dto.getTime().getTime() > walanMaxTime.getTime()) {
								ids.add(dto.getId());
								map.put(dto.getId(), dto);
							} else {
								flag = false; 
								break;
							}
						}
						if(!map.isEmpty()) {
							List<Walian> list = this.getList(map, ids);
							if(!list.isEmpty()) {
								bilanMapper.batchInsertWailian(list);
								logger.info("batch insert walian {} data success!", list.size());
							}
						}
						if(flag && pageIndex <= pages) {
							parseInterface(pageNum + 1, walanMaxTime);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private List<Walian> getList(Map<Integer, Walian> map, List<Integer> ids) {
		List<Walian> list = new ArrayList<>();
		List<Integer> existIds = bilanMapper.getWalianExistIds(ids);
		for (Integer id : existIds) {
			map.remove(id);
		}
		for (Integer id : map.keySet()) {
			list.add(map.get(id));
		}
		return list;
	}

	
}
