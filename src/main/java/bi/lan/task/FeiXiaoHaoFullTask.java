package bi.lan.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import bi.lan.service.FeiXiaoHaoService;

/**
* @author yuhi
* @date 2018年2月6日 下午4:02:15
*/
@Service
public class FeiXiaoHaoFullTask {
	
	private static final Logger logger = LoggerFactory.getLogger(FeiXiaoHaoFullTask.class);
	
	@Autowired
	private FeiXiaoHaoService feixiaohaoService;
	
	@Scheduled(cron="0 0 0 * * ?")
	public void job() {
		try {
			feixiaohaoService.getAllCurreny();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
