package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jdbc.C3P0Utils;
import model.PVDigitalQuantityData;


// 光伏数字变量
public class PVDigitalQuantityDataDao {
	// 添加（输入为pv_num）
	public void addPVDigitalQuantityData(String pv_num) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" +
		"insert into microgrids_pvdigitalquantitydata" +
		"(pv_num)" +
		"values(?)";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, pv_num);
		ptmt.execute();
		conn.close();
	}
	// 添加（输入为PVD的类）
	public void addPVDigitalQuantityData(PVDigitalQuantityData pv) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		"insert into microgrids_pvdigitalquantitydata" + 
		"(pv_num,status_down,status_standby,status_selftest,status_ongrid,locking_self,emergency_stop,remote_local,reactive_power_compensation,smoke_alarm," + 
		"DC_lightning_protection,AC_lightning_protection,PV_reverse_connection,PV_insulation_resistance,DC_overvoltage," + 
		"power_voltage,grid_frequency,grid_reverse_order,inverter_overload,inverter_overheating,inverter_short_circuit," + 
		"ambient_temperature_overheating,island_protection)" +
		"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, pv.getPv_num());
		if (pv.getStatus_down() != null) {
			ptmt.setInt(2, pv.getStatus_down());
		}else {
			ptmt.setNull(2, Types.INTEGER);
		}
		if (pv.getStatus_standby() != null) {
			ptmt.setInt(3, pv.getStatus_standby());
		}else {
			ptmt.setNull(3, Types.INTEGER);
		}
		if (pv.getStatus_selftest() != null) {
			ptmt.setInt(4, pv.getStatus_selftest());
		}else {
			ptmt.setNull(4, Types.INTEGER);
		}
		if (pv.getStatus_ongrid() != null) {
			ptmt.setInt(5, pv.getStatus_ongrid());
		}else {
			ptmt.setNull(5, Types.INTEGER);
		}
		if (pv.getLocking_self() != null) {
			ptmt.setInt(6, pv.getLocking_self());
		}else {
			ptmt.setNull(6, Types.INTEGER);
		}
		if (pv.getEmergency_stop() != null) {
			ptmt.setInt(7, pv.getEmergency_stop());
		}else {
			ptmt.setNull(7, Types.INTEGER);
		}
		if (pv.getRemote_local() != null) {
			ptmt.setInt(8, pv.getRemote_local());
		}else {
			ptmt.setNull(8, Types.INTEGER);
		}
		
		if (pv.getReactive_power_compensation() != null) {
			ptmt.setInt(9, pv.getReactive_power_compensation());
		}else {
			ptmt.setNull(9, Types.INTEGER);
		}
		if (pv.getSmoke_alarm() != null) {
			ptmt.setInt(10, pv.getSmoke_alarm());
		}else {
			ptmt.setNull(10, Types.INTEGER);
		}
		if (pv.getDC_lightning_protection() != null) {
			ptmt.setInt(11, pv.getDC_lightning_protection());
		}else {
			ptmt.setNull(11, Types.INTEGER);
		}
		if (pv.getAC_lightning_protection() != null) {
			ptmt.setInt(12, pv.getAC_lightning_protection());
		}else {
			ptmt.setNull(12, Types.INTEGER);
		}
		if (pv.getPV_reverse_connection() != null) {
			ptmt.setInt(13, pv.getPV_reverse_connection());
		}else {
			ptmt.setNull(13, Types.INTEGER);
		}
		if (pv.getPV_insulation_resistance() != null) {
			ptmt.setInt(14, pv.getPV_insulation_resistance());
		}else {
			ptmt.setNull(14, Types.INTEGER);
		}
		if (pv.getDC_overvoltage() != null) {
			ptmt.setInt(15, pv.getDC_overvoltage());
		}else {
			ptmt.setNull(15, Types.INTEGER);
		}
		if (pv.getPower_voltage() != null) {
			ptmt.setInt(16, pv.getPower_voltage());
		}else {
			ptmt.setNull(16, Types.INTEGER);
		}
		if (pv.getGrid_frequency() != null) {
			ptmt.setInt(17, pv.getGrid_frequency());
		}else {
			ptmt.setNull(17, Types.INTEGER);
		}
		if (pv.getGrid_reverse_order() != null) {
			ptmt.setInt(18, pv.getGrid_reverse_order());
		}else {
			ptmt.setNull(18, Types.INTEGER);
		}
		if (pv.getInverter_overload() != null) {
			ptmt.setInt(19, pv.getInverter_overload());
		}else {
			ptmt.setNull(19, Types.INTEGER);
		}
		if (pv.getInverter_overheating() != null) {
			ptmt.setInt(20, pv.getInverter_overheating());
		}else {
			ptmt.setNull(20, Types.INTEGER);
		}
		if (pv.getInverter_short_circuit() != null) {
			ptmt.setInt(21, pv.getInverter_short_circuit());
		}else {
			ptmt.setNull(21, Types.INTEGER);
		}
		if (pv.getAmbient_temperature_overheating() != null) {
			ptmt.setInt(22, pv.getAmbient_temperature_overheating());
		}else {
			ptmt.setNull(22, Types.INTEGER);
		}
		if (pv.getIsland_protection() != null) {
			ptmt.setInt(23, pv.getIsland_protection());
		}else {
			ptmt.setNull(23, Types.INTEGER);
		}
		ptmt.execute();
		conn.close();
	}
	// 更新（输入为pv_num,更新字段,更新值）
	public void updatePVDigitalQuantityData(String num,String field, Integer value) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" +
		" update  microgrids_pvdigitalquantitydata " +
		" set " + field + "= ? " +
		" where pv_num = ?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setInt(1, value);
		ptmt.setString(2, num);
		ptmt.execute();
		conn.close();
	}
	// 更新（输入为PVD的类）
	public void updatePVDigitalQuantityData(PVDigitalQuantityData pv) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		" update microgrids_pvdigitalquantitydata " + 
		" set status_down=?,status_standby=?,status_selftest=?,status_ongrid=?,locking_self=?,emergency_stop=?,remote_local=?,reactive_power_compensation=?,smoke_alarm=?," +
		" DC_lightning_protection=?,AC_lightning_protection=?,PV_reverse_connection=?,PV_insulation_resistance=?,DC_overvoltage=?," + 
		" power_voltage=?,grid_frequency=?,grid_reverse_order=?,inverter_overload=?,inverter_overheating=?,inverter_short_circuit=?, "+
		" ambient_temperature_overheating=?,island_protection=? "+
		" where pv_num=?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		if (pv.getStatus_down() != null) {
			ptmt.setInt(1, pv.getStatus_down());
		}else {
			ptmt.setNull(1, Types.INTEGER);
		}
		if (pv.getStatus_standby() != null) {
			ptmt.setInt(2, pv.getStatus_standby());
		}else {
			ptmt.setNull(2, Types.INTEGER);
		}
		if (pv.getStatus_selftest() != null) {
			ptmt.setInt(3, pv.getStatus_selftest());
		}else {
			ptmt.setNull(3, Types.INTEGER);
		}
		if (pv.getStatus_ongrid() != null) {
			ptmt.setInt(4, pv.getStatus_ongrid());
		}else {
			ptmt.setNull(4, Types.INTEGER);
		}
		if (pv.getLocking_self() != null) {
			ptmt.setInt(5, pv.getLocking_self());
		}else {
			ptmt.setNull(5, Types.INTEGER);
		}
		if (pv.getEmergency_stop() != null) {
			ptmt.setInt(6, pv.getEmergency_stop());
		}else {
			ptmt.setNull(6, Types.INTEGER);
		}
		if (pv.getRemote_local() != null) {
			ptmt.setInt(7, pv.getRemote_local());
		}else {
			ptmt.setNull(7, Types.INTEGER);
		}
		if (pv.getReactive_power_compensation() != null) {
			ptmt.setInt(8, pv.getReactive_power_compensation());
		}else {
			ptmt.setNull(8, Types.INTEGER);
		}
		if (pv.getSmoke_alarm() != null) {
			ptmt.setInt(9, pv.getSmoke_alarm());
		}else {
			ptmt.setNull(9, Types.INTEGER);
		}
		if (pv.getDC_lightning_protection() != null) {
			ptmt.setInt(10, pv.getDC_lightning_protection());
		}else {
			ptmt.setNull(10, Types.INTEGER);
		}
		if (pv.getAC_lightning_protection() != null) {
			ptmt.setInt(11, pv.getAC_lightning_protection());
		}else {
			ptmt.setNull(11, Types.INTEGER);
		}
		if (pv.getPV_reverse_connection() != null) {
			ptmt.setInt(12, pv.getPV_reverse_connection());
		}else {
			ptmt.setNull(12, Types.INTEGER);
		}
		if (pv.getPV_insulation_resistance() != null) {
			ptmt.setInt(13, pv.getPV_insulation_resistance());
		}else {
			ptmt.setNull(13, Types.INTEGER);
		}
		if (pv.getDC_overvoltage() != null) {
			ptmt.setInt(14, pv.getDC_overvoltage());
		}else {
			ptmt.setNull(14, Types.INTEGER);
		}
		if (pv.getPower_voltage() != null) {
			ptmt.setInt(15, pv.getPower_voltage());
		}else {
			ptmt.setNull(15, Types.INTEGER);
		}
		if (pv.getGrid_frequency() != null) {
			ptmt.setInt(16, pv.getGrid_frequency());
		}else {
			ptmt.setNull(16, Types.INTEGER);
		}
		if (pv.getGrid_reverse_order() != null) {
			ptmt.setInt(17, pv.getGrid_reverse_order());
		}else {
			ptmt.setNull(17, Types.INTEGER);
		}
		if (pv.getInverter_overload() != null) {
			ptmt.setInt(18, pv.getInverter_overload());
		}else {
			ptmt.setNull(18, Types.INTEGER);
		}
		if (pv.getInverter_overheating() != null) {
			ptmt.setInt(19, pv.getInverter_overheating());
		}else {
			ptmt.setNull(19, Types.INTEGER);
		}
		if (pv.getInverter_short_circuit() != null) {
			ptmt.setInt(20, pv.getInverter_short_circuit());
		}else {
			ptmt.setNull(20, Types.INTEGER);
		}
		if (pv.getAmbient_temperature_overheating() != null) {
			ptmt.setInt(21, pv.getAmbient_temperature_overheating());
		}else {
			ptmt.setNull(21, Types.INTEGER);
		}
		if (pv.getIsland_protection() != null) {
			ptmt.setInt(22, pv.getIsland_protection());
		}else {
			ptmt.setNull(22, Types.INTEGER);
		}
		ptmt.setString(23, pv.getPv_num());
		ptmt.execute();
		conn.close();
	}
	// 删除
	public void delPVDigitalQuantityData(String pv_num) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		" delete from microgrids_pvdigitalquantitydata " + 
		" where pv_num=?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, pv_num);
		ptmt.execute();
		conn.close();
	}
	// 条件获取所有数据
	public List<PVDigitalQuantityData> query(List<Map<String, Object>> params) throws Exception {
		
		List<PVDigitalQuantityData> pvds = new ArrayList<PVDigitalQuantityData>();
		PVDigitalQuantityData pvd = null;

		Connection conn = C3P0Utils.getConnection();
		StringBuilder sql = new StringBuilder();
		sql.append("select * from microgrids_pvdigitalquantitydata where 1=1");
		if (params != null && params.size()>0) {
			for (int i=0; i< params.size(); i++) {
				Map<String, Object> map = params.get(i);
				sql.append(" "+ map.get("ao") +" " + map.get("name") + " " + map.get("rela") + " " + map.get("value"));
			}
		}
		System.out.println(sql);
		PreparedStatement ptmt = conn.prepareStatement(sql.toString());
		ResultSet rs = ptmt.executeQuery();
		
		while(rs.next()) {
			pvd = new PVDigitalQuantityData();
			pvd.setPv_num(rs.getString("pv_num"));
			pvd.setStatus_down(rs.getInt("status_down"));
			pvd.setStatus_standby(rs.getInt("status_standby"));
			pvd.setStatus_selftest(rs.getInt("status_selftest"));
			pvd.setStatus_ongrid(rs.getInt("status_ongrid"));
			pvd.setLocking_self(rs.getInt("locking_self"));
			pvd.setEmergency_stop(rs.getInt("emergency_stop"));
			pvd.setRemote_local(rs.getInt("remote_local"));
			pvd.setReactive_power_compensation(rs.getInt("reactive_power_compensation"));
			pvd.setSmoke_alarm(rs.getInt("smoke_alarm"));
			pvd.setDC_lightning_protection(rs.getInt("dC_lightning_protection"));
			pvd.setAC_lightning_protection(rs.getInt("aC_lightning_protection"));
			pvd.setPV_reverse_connection(rs.getInt("pV_reverse_connection"));
			pvd.setPV_insulation_resistance(rs.getInt("pV_insulation_resistance"));
			pvd.setDC_overvoltage(rs.getInt("dC_overvoltage"));
			pvd.setPower_voltage(rs.getInt("power_voltage"));
			pvd.setGrid_frequency(rs.getInt("grid_frequency"));
			pvd.setGrid_reverse_order(rs.getInt("grid_reverse_order"));
			pvd.setInverter_overload(rs.getInt("inverter_overload"));
			pvd.setInverter_overheating(rs.getInt("inverter_overheating"));
			pvd.setInverter_short_circuit(rs.getInt("inverter_short_circuit"));
			pvd.setAmbient_temperature_overheating(rs.getInt("ambient_temperature_overheating"));
			pvd.setIsland_protection(rs.getInt("island_protection"));
			pvds.add(pvd);
		}
		conn.close();
		return pvds;
		
	}
	// 获取
	public PVDigitalQuantityData get(String pv_num) throws SQLException{
		
		PVDigitalQuantityData pvd = null;
		
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		" select * from microgrids_pvdigitalquantitydata " + 
		" where pv_num=?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, pv_num);
		ResultSet rs = ptmt.executeQuery();
		
		while (rs.next()) {
			pvd = new PVDigitalQuantityData();
			pvd.setPv_num(rs.getString("pv_num"));
			pvd.setStatus_down(rs.getInt("status_down"));
			pvd.setStatus_standby(rs.getInt("status_standby"));
			pvd.setStatus_selftest(rs.getInt("status_selftest"));
			pvd.setStatus_ongrid(rs.getInt("status_ongrid"));
			pvd.setLocking_self(rs.getInt("locking_self"));
			pvd.setEmergency_stop(rs.getInt("emergency_stop"));
			pvd.setRemote_local(rs.getInt("remote_local"));
			pvd.setReactive_power_compensation(rs.getInt("reactive_power_compensation"));
			pvd.setSmoke_alarm(rs.getInt("smoke_alarm"));
			pvd.setDC_lightning_protection(rs.getInt("dC_lightning_protection"));
			pvd.setAC_lightning_protection(rs.getInt("aC_lightning_protection"));
			pvd.setPV_reverse_connection(rs.getInt("pV_reverse_connection"));
			pvd.setPV_insulation_resistance(rs.getInt("pV_insulation_resistance"));
			pvd.setDC_overvoltage(rs.getInt("dC_overvoltage"));
			pvd.setPower_voltage(rs.getInt("power_voltage"));
			pvd.setGrid_frequency(rs.getInt("grid_frequency"));
			pvd.setGrid_reverse_order(rs.getInt("grid_reverse_order"));
			pvd.setInverter_overload(rs.getInt("inverter_overload"));
			pvd.setInverter_overheating(rs.getInt("inverter_overheating"));
			pvd.setInverter_short_circuit(rs.getInt("inverter_short_circuit"));
			pvd.setAmbient_temperature_overheating(rs.getInt("ambient_temperature_overheating"));
			pvd.setIsland_protection(rs.getInt("island_protection"));
		}
		conn.close();
		return pvd;
	}
}
