package bi.lan.model;

import java.sql.Timestamp;

/**
* @author yuhi
* @date 2018年1月31日 下午1:54:08
*/
public class Jinse {
	
	private Integer id;
	private Timestamp time;
	private String text;
	private String url;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	@Override
	public String toString() {
		return "{\"id\": \"" + id + "\", \"time\": \"" + time + "\", \"text\": \"" + text + "\", \"url\": \"" + url
				+ "\"}";
	}
	
}
