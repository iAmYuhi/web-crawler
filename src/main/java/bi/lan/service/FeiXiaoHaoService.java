package bi.lan.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bi.lan.common.ZHttpClient;
import bi.lan.mapper.BiLanMapper;
import bi.lan.model.Feixiaohao;

/**
* @author yuhi
* @date 2018年2月5日 下午7:27:29
*/
@Service
public class FeiXiaoHaoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FeiXiaoHaoService.class);
	private static final String INDEX_ULR_ALL = "https://www.feixiaohao.com/all/";
	
	@Autowired
	private BiLanMapper bilanMapper;
	
	public void getCurrency() {
		try {
			String html = ZHttpClient.get(INDEX_ULR_ALL);
			List<Feixiaohao> list = this.parseHtml(html);
			for (Feixiaohao feixiaohao : list) {
				bilanMapper.updateFeixiaohao(feixiaohao);
			}
			logger.info("update feixiaohao {} data success!", list.size());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private List<Feixiaohao> parseHtml(String html) {
		List<Feixiaohao> list = new ArrayList<>();
		try {
			Document doc = Jsoup.parse(html);
			Elements boxContains = doc.select(".boxContain");
			Element boxContain = boxContains.first();
			Elements trs = boxContain.select("tr");
			for (int i = 0; i < trs.size(); i++) {
				if(i == 0) {
					continue;
				}
				Element tr = trs.get(i);
				Elements tds = tr.select("td");
				Feixiaohao dto = new Feixiaohao();
				Integer id = 0;
				for (int j = 0; j < tds.size(); j++) {
					Element td = tds.get(j);
					if(j == 0) {
						id = Integer.valueOf(td.text());
						dto.setId(id);
					}
					if(j == 1) {
						dto.setCurrencyName(td.text());
						Elements img = td.select("img");
						dto.setCurrencyImg(img.attr("src").replace("//", ""));
					}
					if(j == 2) {
						dto.setMarketPrice(td.text());
					}
					if(j == 3) {
						dto.setPrice(td.text());
					}
					if(j == 4) {
						dto.setMarketNum(td.text().replace("*", ""));
					}
					if(j == 5) {
						dto.setMarkeyRate(td.text());
					}
					if(j == 6) {
						dto.setTurnover24h(td.text());
					}
					if(j == 7) {
						dto.setRose1h(td.text());
					}
					if(j == 8) {
						dto.setRose24h(td.text());
					}
					if(j == 9) {
						dto.setRose7d(td.text());
					}
				}
				list.add(dto);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return list;
	}
	
}
