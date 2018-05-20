package model;


public class DevControl {
	private Integer id;
	private String num;
	private Integer dev_type;
	private Integer switch_status;
	private double active_power;
	private double reactive_power;
	private double powerfactor;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Integer getDev_type() {
		return dev_type;
	}
	public void setDev_type(Integer dev_type) {
		this.dev_type = dev_type;
	}
	public Integer getSwitch_status() {
		return switch_status;
	}
	public void setSwitch_status(Integer switch_status) {
		this.switch_status = switch_status;
	}
	public double getActive_power() {
		return active_power;
	}
	public void setActive_power(double active_power) {
		this.active_power = active_power;
	}
	public double getReactive_power() {
		return reactive_power;
	}
	public void setReactive_power(double reactive_power) {
		this.reactive_power = reactive_power;
	}
	public double getPowerfactor() {
		return powerfactor;
	}
	public void setPowerfactor(double powerfactor) {
		this.powerfactor = powerfactor;
	}
	
}
