package bi.lan.model;

/**
* @author yuhi
* @date 2018年2月2日 下午10:44:46
*/
public class Feixiaohao {
	
	private Integer id;
	private String currencyName;
	private String currencyImg;
	private String marketPrice;
	private String price;
	private String marketNum;
	private String markeyRate;
	private String turnover24h;
	private String rose1h;
	private String rose24h;
	private String rose7d;
	private String high24h;
	private String low24h;
	private String describe;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getCurrencyImg() {
		return currencyImg;
	}
	public void setCurrencyImg(String currencyImg) {
		this.currencyImg = currencyImg;
	}
	public String getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(String marketPrice) {
		this.marketPrice = marketPrice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMarketNum() {
		return marketNum;
	}
	public void setMarketNum(String marketNum) {
		this.marketNum = marketNum;
	}
	public String getMarkeyRate() {
		return markeyRate;
	}
	public void setMarkeyRate(String markeyRate) {
		this.markeyRate = markeyRate;
	}
	public String getTurnover24h() {
		return turnover24h;
	}
	public void setTurnover24h(String turnover24h) {
		this.turnover24h = turnover24h;
	}
	public String getRose1h() {
		return rose1h;
	}
	public void setRose1h(String rose1h) {
		this.rose1h = rose1h;
	}
	public String getRose24h() {
		return rose24h;
	}
	public void setRose24h(String rose24h) {
		this.rose24h = rose24h;
	}
	public String getRose7d() {
		return rose7d;
	}
	public void setRose7d(String rose7d) {
		this.rose7d = rose7d;
	}
	public String getHigh24h() {
		return high24h;
	}
	public void setHigh24h(String high24h) {
		this.high24h = high24h;
	}
	public String getLow24h() {
		return low24h;
	}
	public void setLow24h(String low24h) {
		this.low24h = low24h;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	@Override
	public String toString() {
		return "{\"id\": \"" + id + "\", \"currencyName\": \"" + currencyName + "\", \"currencyImg\": \"" + currencyImg
				+ "\", \"marketPrice\": \"" + marketPrice + "\", \"price\": \"" + price + "\", \"marketNum\": \""
				+ marketNum + "\", \"markeyRate\": \"" + markeyRate + "\", \"turnover24h\": \"" + turnover24h
				+ "\", \"rose1h\": \"" + rose1h + "\", \"rose24h\": \"" + rose24h + "\", \"rose7d\": \"" + rose7d
				+ "\", \"high24h\": \"" + high24h + "\", \"low24h\": \"" + low24h + "\", \"describe\": \"" + describe
				+ "\"}";
	}

}
