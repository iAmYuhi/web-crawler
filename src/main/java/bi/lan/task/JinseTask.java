package bi.lan.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import bi.lan.service.JinseService;

/**
* @author yuhi
* @date 2018年2月5日 下午7:12:36
*/
@Component
public class JinseTask {
	
	private static final Logger logger = LoggerFactory.getLogger(JinseTask.class);
	
	@Autowired
	private JinseService jinseService;
	
	@Scheduled(cron="0 0/1 * * * ?")
	public void job() {
		try {
			jinseService.getNewConsult();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
}
