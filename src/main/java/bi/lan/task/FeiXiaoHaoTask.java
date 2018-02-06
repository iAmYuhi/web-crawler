package bi.lan.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bi.lan.service.FeiXiaoHaoService;

/**
* @author yuhi
* @date 2018年2月5日 下午7:20:09
*/
@Component
public class FeiXiaoHaoTask {
	
	private static final Logger logger = LoggerFactory.getLogger(FeiXiaoHaoTask.class);
	
	@Autowired
	private FeiXiaoHaoService feixiaohaoService;
	
	@Scheduled(cron="0/30 * * * * ?")
	public void job() {
		try {
			feixiaohaoService.getCurrency();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
