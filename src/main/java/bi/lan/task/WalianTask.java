package bi.lan.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bi.lan.service.WalianService;

/**
* @author yuhi
* @date 2018年2月6日 下午12:10:06
*/
@Component
public class WalianTask {
	
	private static final Logger logger = LoggerFactory.getLogger(WalianTask.class);
	
	@Autowired
	private WalianService walianService;
	
	@Scheduled(cron="0 0 0/1 * * ?")
	public void job() {
		try {
			walianService.getWalan();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
