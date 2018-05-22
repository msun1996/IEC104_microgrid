package model;

import java.util.Date;

public class PVAnalogQuantityData1 {
	private Integer id;
	private Date timestamp;
	private String pv_num;
	private Double matrix_cur;
	private Double matrix_volt;
	private Double matrix_power_in;
	private Double grid_volt_ab;
	private Double grid_volt_bc;
	private Double grid_volt_ca;
	private Double on_grid_cur_a;
	private Double on_grid_cur_b;
	private Double on_grid_cur_c;
	private Double power_factor_a;
	private Double power_factor_b;
	private Double power_factor_c;
	private Double grid_freq;
	
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
	public Double getMatrix_cur() {
		return matrix_cur;
	}
	public void setMatrix_cur(Double matrix_cur) {
		this.matrix_cur = matrix_cur;
	}
	public Double getMatrix_volt() {
		return matrix_volt;
	}
	public void setMatrix_volt(Double matrix_volt) {
		this.matrix_volt = matrix_volt;
	}
	public Double getMatrix_power_in() {
		return matrix_power_in;
	}
	public void setMatrix_power_in(Double matrix_power_in) {
		this.matrix_power_in = matrix_power_in;
	}
	public Double getGrid_volt_ab() {
		return grid_volt_ab;
	}
	public void setGrid_volt_ab(Double grid_volt_ab) {
		this.grid_volt_ab = grid_volt_ab;
	}
	public Double getGrid_volt_bc() {
		return grid_volt_bc;
	}
	public void setGrid_volt_bc(Double grid_volt_bc) {
		this.grid_volt_bc = grid_volt_bc;
	}
	public Double getGrid_volt_ca() {
		return grid_volt_ca;
	}
	public void setGrid_volt_ca(Double grid_volt_ca) {
		this.grid_volt_ca = grid_volt_ca;
	}
	public Double getOn_grid_cur_a() {
		return on_grid_cur_a;
	}
	public void setOn_grid_cur_a(Double on_grid_cur_a) {
		this.on_grid_cur_a = on_grid_cur_a;
	}
	public Double getOn_grid_cur_b() {
		return on_grid_cur_b;
	}
	public void setOn_grid_cur_b(Double on_grid_cur_b) {
		this.on_grid_cur_b = on_grid_cur_b;
	}
	public Double getOn_grid_cur_c() {
		return on_grid_cur_c;
	}
	public void setOn_grid_cur_c(Double on_grid_cur_c) {
		this.on_grid_cur_c = on_grid_cur_c;
	}
	public Double getPower_factor_a() {
		return power_factor_a;
	}
	public void setPower_factor_a(Double power_factor_a) {
		this.power_factor_a = power_factor_a;
	}
	public Double getPower_factor_b() {
		return power_factor_b;
	}
	public void setPower_factor_b(Double power_factor_b) {
		this.power_factor_b = power_factor_b;
	}
	public Double getPower_factor_c() {
		return power_factor_c;
	}
	public void setPower_factor_c(Double power_factor_c) {
		this.power_factor_c = power_factor_c;
	}
	public Double getGrid_freq() {
		return grid_freq;
	}
	public void setGrid_freq(Double grid_freq) {
		this.grid_freq = grid_freq;
	}
	
	public void Empty() {
		this.matrix_cur = null;
		this.matrix_volt = null;
		this.matrix_power_in = null;
		this.grid_volt_ab = null;
		this.grid_volt_bc = null;
		this.grid_volt_ca = null;
		this.on_grid_cur_a = null;
		this.on_grid_cur_b = null;
		this.on_grid_cur_c = null;
		this.power_factor_a = null;
		this.power_factor_b = null;
		this.power_factor_c = null;
		this.grid_freq = null;
	}
}
