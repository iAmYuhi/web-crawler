package bi.lan.blockchain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
 * @date 2018年1月29日 下午8:57:26
 */
@Service
public class FeiXiaoHaoMain {

	private static final Logger logger = LoggerFactory.getLogger(FeiXiaoHaoMain.class);
	private static final String INDEX_ULR_ALL = "https://www.feixiaohao.com/all/";
	private static final String DETAIL_URL = "https://www.feixiaohao.com";
	private volatile List<Feixiaohao> list = Collections.synchronizedList(new ArrayList<>());

	@Autowired
	private BiLanMapper lilanMapper;

//	@PostConstruct
	private void init() {
		String html = ZHttpClient.get(INDEX_ULR_ALL);
		this.parseHtml(html);
	}

	private void parseHtml(String html) {
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
						Elements a = td.select("a");
						String href = a.attr("href");
						if(StringUtils.isNotEmpty(href)) {
							getDetail(href, dto);
						}
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
				logger.info(dto.toString());
			}
			lilanMapper.batchInsertFeixiaohao(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			list.clear();
		}
	}

	private void getDetail(String href, Feixiaohao dto) {
		String url = DETAIL_URL + href;
		String html = ZHttpClient.get(url);
		parseDetailHtml(html, dto);
	}

	private void parseDetailHtml(String html, Feixiaohao dto) {
		try {
			Document doc = Jsoup.parse(html);
			Elements height = doc.select(".lowHeight");
			Elements spans = height.select("span");
			for (int i = 0; i < spans.size(); i++) {
				Element span = spans.get(i);
				if(i == 0 && StringUtils.isNotEmpty(span.text())) {
					dto.setHigh24h(span.text());
				}
				if(i == 1 && StringUtils.isNotEmpty(span.text())) {
					dto.setLow24h(span.text());
				}
			}
			Elements des = doc.select(".des");
			String href = des.select("a").attr("href");
			String desUrl = DETAIL_URL + href;
			String desHtml = ZHttpClient.get(desUrl);
			parseHtmlByDes(desHtml, dto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private void parseHtmlByDes(String desHtml, Feixiaohao dto) {
		try {
			Document doc = Jsoup.parse(desHtml);
			Elements artBox = doc.select(".artBox");
			if(StringUtils.isNotEmpty(artBox.text())) {
				dto.setDescribe(artBox.text());
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
