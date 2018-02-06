package bi.lan.model;

import java.sql.Timestamp;

/**
* @author yuhi
* @date 2018年2月5日 下午9:25:08
*/
public class Walian {
	
	private Integer id;
	private String logo;
	private String title;
	private String intro;
	private String content;
	private Timestamp time;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Timestamp getTime() {
		return time;
	}
	public void setTime(Timestamp time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "{\"id\": \"" + id + "\", \"logo\": \"" + logo + "\", \"title\": \"" + title + "\", \"intro\": \""
				+ intro + "\", \"content\": \"" + content + "\", \"time\": \"" + time + "\"}";
	}
	
}
