package Entity;

public class objectLog {
	private String time;
	private Long imei;
	private String ip;
	
	public objectLog() {
		// TODO Auto-generated constructor stub
	}
	public objectLog(String time, Long imei, String ip) {
		// TODO Auto-generated constructor stub
		this.time = time;
		this.imei = imei;
		this.ip = ip;
	}
	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public Long getImei() {
		return imei;
	}
	public void setImei(Long imei) {
		this.imei = imei;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Time : "+time + "\timei: "+imei + "\tip : "+ip;
	}
	
	
	
}
