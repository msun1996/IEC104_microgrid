package model;


/* 光伏数字量告警信息对应实体类 */
public class PVDigitalQuantityData {
	private Integer id;
	private String pv_num;
	private Integer status_down;
	private Integer status_standby;
	private Integer status_selftest;
	private Integer status_ongrid;
	private Integer locking_self;
	private Integer emergency_stop;
	private Integer remote_local;
	private Integer reactive_power_compensation;
	private Integer smoke_alarm;
	private Integer DC_lightning_protection;
	private Integer AC_lightning_protection;
	private Integer PV_reverse_connection;
	private Integer PV_insulation_resistance;
	private Integer DC_overvoltage;
	private Integer power_voltage;
	private Integer grid_frequency;
	private Integer grid_reverse_order;
	private Integer inverter_overload;
	private Integer inverter_overheating;
	private Integer ambient_temperature_overheating;
	private Integer inverter_short_circuit;
	private Integer island_protection;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPv_num() {
		return pv_num;
	}
	public void setPv_num(String pv_num) {
		this.pv_num = pv_num;
	}
	public Integer getStatus_down() {
		return status_down;
	}
	public void setStatus_down(Integer status_down) {
		this.status_down = status_down;
	}
	public Integer getStatus_standby() {
		return status_standby;
	}
	public void setStatus_standby(Integer status_standby) {
		this.status_standby = status_standby;
	}
	public Integer getStatus_selftest() {
		return status_selftest;
	}
	public void setStatus_selftest(Integer status_selftest) {
		this.status_selftest = status_selftest;
	}
	public Integer getStatus_ongrid() {
		return status_ongrid;
	}
	public void setStatus_ongrid(Integer status_ongrid) {
		this.status_ongrid = status_ongrid;
	}
	public Integer getLocking_self() {
		return locking_self;
	}
	public void setLocking_self(Integer locking_self) {
		this.locking_self = locking_self;
	}
	public Integer getEmergency_stop() {
		return emergency_stop;
	}
	public void setEmergency_stop(Integer emergency_stop) {
		this.emergency_stop = emergency_stop;
	}
	public Integer getRemote_local() {
		return remote_local;
	}
	public void setRemote_local(Integer remote_local) {
		this.remote_local = remote_local;
	}
	public Integer getReactive_power_compensation() {
		return reactive_power_compensation;
	}
	public void setReactive_power_compensation(Integer reactive_power_compensation) {
		this.reactive_power_compensation = reactive_power_compensation;
	}
	public Integer getSmoke_alarm() {
		return smoke_alarm;
	}
	public void setSmoke_alarm(Integer smoke_alarm) {
		this.smoke_alarm = smoke_alarm;
	}
	public Integer getDC_lightning_protection() {
		return DC_lightning_protection;
	}
	public void setDC_lightning_protection(Integer dC_lightning_protection) {
		DC_lightning_protection = dC_lightning_protection;
	}
	public Integer getAC_lightning_protection() {
		return AC_lightning_protection;
	}
	public void setAC_lightning_protection(Integer aC_lightning_protection) {
		AC_lightning_protection = aC_lightning_protection;
	}
	public Integer getPV_reverse_connection() {
		return PV_reverse_connection;
	}
	public void setPV_reverse_connection(Integer pV_reverse_connection) {
		PV_reverse_connection = pV_reverse_connection;
	}
	public Integer getPV_insulation_resistance() {
		return PV_insulation_resistance;
	}
	public void setPV_insulation_resistance(Integer pV_insulation_resistance) {
		PV_insulation_resistance = pV_insulation_resistance;
	}
	public Integer getDC_overvoltage() {
		return DC_overvoltage;
	}
	public void setDC_overvoltage(Integer dC_overvoltage) {
		DC_overvoltage = dC_overvoltage;
	}
	public Integer getPower_voltage() {
		return power_voltage;
	}
	public void setPower_voltage(Integer power_voltage) {
		this.power_voltage = power_voltage;
	}
	public Integer getGrid_frequency() {
		return grid_frequency;
	}
	public void setGrid_frequency(Integer grid_frequency) {
		this.grid_frequency = grid_frequency;
	}
	public Integer getGrid_reverse_order() {
		return grid_reverse_order;
	}
	public void setGrid_reverse_order(Integer grid_reverse_order) {
		this.grid_reverse_order = grid_reverse_order;
	}
	public Integer getInverter_overload() {
		return inverter_overload;
	}
	public void setInverter_overload(Integer inverter_overload) {
		this.inverter_overload = inverter_overload;
	}
	public Integer getInverter_overheating() {
		return inverter_overheating;
	}
	public void setInverter_overheating(Integer inverter_overheating) {
		this.inverter_overheating = inverter_overheating;
	}
	public Integer getInverter_short_circuit() {
		return inverter_short_circuit;
	}
	public void setInverter_short_circuit(Integer inverter_short_circuit) {
		this.inverter_short_circuit = inverter_short_circuit;
	}
	public Integer getAmbient_temperature_overheating() {
		return ambient_temperature_overheating;
	}
	public void setAmbient_temperature_overheating(Integer ambient_temperature_overheating) {
		this.ambient_temperature_overheating = ambient_temperature_overheating;
	}
	public Integer getIsland_protection() {
		return island_protection;
	}
	public void setIsland_protection(Integer island_protection) {
		this.island_protection = island_protection;
	}
	
}
