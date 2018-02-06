package bi.lan.service;

import java.util.ArrayList;
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
* @date 2018年2月5日 下午7:27:29
*/
@Service
public class FeiXiaoHaoService {
	
	private static final Logger logger = LoggerFactory.getLogger(FeiXiaoHaoService.class);
	private static final String INDEX_ULR_ALL = "https://www.feixiaohao.com/all/";
	private static final String DETAIL_URL = "https://www.feixiaohao.com";
	
	@Autowired
	private BiLanMapper bilanMapper;
	
	public void getAllCurreny() {
		try {
			String html = ZHttpClient.get(INDEX_ULR_ALL);
			this.parseFullHtml(html);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void parseFullHtml(String html) {
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
				for (int j = 0; j < tds.size(); j++) {
					Element td = tds.get(j);
					if(j == 1) {
						Elements a = td.select("a");
						String href = a.attr("href");
						dto.setCurrencyUrl(href);
						list.add(dto);
					}
				}
			}
			getDetail(list);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	private void getDetail(List<Feixiaohao> list) {
		for (Feixiaohao feixiaohao : list) {
			String url = DETAIL_URL + feixiaohao.getCurrencyUrl();
			String html = ZHttpClient.get(url);
			if(StringUtils.isNotEmpty(html)) {
				this.parseDetailHtml(html, feixiaohao);
			}
			bilanMapper.updateFeixiaohao(feixiaohao);
		}
		logger.info("success operation full task {} data!", list.size());
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
			if(StringUtils.isNotEmpty(desHtml)) {
				this.parseHtmlByDes(desHtml, dto);
			}
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
						Elements a = td.select("a");
						String href = a.attr("href");
						dto.setCurrencyUrl(href);
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
