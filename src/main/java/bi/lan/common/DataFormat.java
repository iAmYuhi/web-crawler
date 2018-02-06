package bi.lan.common;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
* @author yuhi
* @date 2018年1月31日 下午2:35:35
*/
public class DataFormat {
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static Timestamp formatData(String time) {
		Date now = new Date();
		String nowFormat = sdf.format(now);
		String date = nowFormat + " %s" + ":00";
		String formatDate = String.format(date, time);
		try {
			Date parse = sdfms.parse(formatDate);
			return new Timestamp(parse.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Timestamp formatData(String ymd, String time) {
		String date = ymd + " %s" + ":00";
		String formatDate = String.format(date, time);
		try {
			Date parse = sdfms.parse(formatDate);
			return new Timestamp(parse.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
