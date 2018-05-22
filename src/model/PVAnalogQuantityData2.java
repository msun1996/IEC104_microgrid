package model;

import java.util.Date;

public class PVAnalogQuantityData2 {
	
	private Integer id;
	private Date timestamp;
	private String pv_num;
	private Double on_grid_p;
	private Double on_grid_q;
	private Double on_grid_s;
	private Double inv_cabin_temp;
	private Double day_gen_power;
	private Double day_runtime;
	private Double total_gen_power;
	private Double total_runtime;
	private Double co2_reduce;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getPv_num() {
		return pv_num;
	}
	public void setPv_num(String pv_num) {
		this.pv_num = pv_num;
	}
	public Double getOn_grid_p() {
		return on_grid_p;
	}
	public void setOn_grid_p(Double on_grid_p) {
		this.on_grid_p = on_grid_p;
	}
	public Double getOn_grid_q() {
		return on_grid_q;
	}
	public void setOn_grid_q(Double on_grid_q) {
		this.on_grid_q = on_grid_q;
	}
	public Double getOn_grid_s() {
		return on_grid_s;
	}
	public void setOn_grid_s(Double on_grid_s) {
		this.on_grid_s = on_grid_s;
	}
	public Double getInv_cabin_temp() {
		return inv_cabin_temp;
	}
	public void setInv_cabin_temp(Double inv_cabin_temp) {
		this.inv_cabin_temp = inv_cabin_temp;
	}
	public Double getDay_gen_power() {
		return day_gen_power;
	}
	public void setDay_gen_power(Double day_gen_power) {
		this.day_gen_power = day_gen_power;
	}
	public Double getDay_runtime() {
		return day_runtime;
	}
	public void setDay_runtime(Double day_runtime) {
		this.day_runtime = day_runtime;
	}
	public Double getTotal_gen_power() {
		return total_gen_power;
	}
	public void setTotal_gen_power(Double total_gen_power) {
		this.total_gen_power = total_gen_power;
	}
	public Double getTotal_runtime() {
		return total_runtime;
	}
	public void setTotal_runtime(Double total_runtime) {
		this.total_runtime = total_runtime;
	}
	public Double getCo2_reduce() {
		return co2_reduce;
	}
	public void setCo2_reduce(Double co2_reduce) {
		this.co2_reduce = co2_reduce;
	}
	
	public void Empty() {
		this.on_grid_p = null;
		this.on_grid_q = null;
		this.on_grid_s = null;
		this.inv_cabin_temp = null;
		this.day_gen_power = null;
		this.day_runtime = null;
		this.total_gen_power = null;
		this.total_runtime = null;
		this.co2_reduce = null;
	}
}
