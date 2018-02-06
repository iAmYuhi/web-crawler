package bi.lan.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
* @author yuhi
* @date 2018年2月2日 下午4:17:16
*/
public class StartServer {
	private static final Logger logger = LoggerFactory.getLogger(StartServer.class);
	private static final String PATH = "classpath:spring.xml";
	
	public static void main(String[] args) {
		logger.info("Server starting!");
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                logger.info("Process has been killed!");
            }
        });
		@SuppressWarnings("resource")
		FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext(PATH);
		logger.info("applicationContext info: {}", applicationContext.toString());
		logger.info("Server start finished!");
	}
	
}
