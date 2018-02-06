package bi.lan.model;

import java.sql.Timestamp;

/**
* @author yuhi
* @date 2018年2月5日 上午10:38:24
*/
public class Aicoin {
	
	private Integer id;
	private String title;
	private String describe;
	private String text;
	private Timestamp time;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "{\"id\": \"" + id + "\", \"title\": \"" + title + "\", \"describe\": \"" + describe + "\", \"text\": \""
				+ text + "\", \"time\": \"" + time + "\"}";
	}
	
}
