package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import jdbc.C3P0Utils;
import model.PVAnalogQuantityData2;

public class PVAnalogQuantityData2Dao {
	// 添加 （输入类）
	public void addPVAnalogQuantityData2(PVAnalogQuantityData2 pva2) throws SQLException {
		Connection connection = C3P0Utils.getConnection();
		String sql = ""+
		"insert into microgrids_pvanalogquantitydata2" +
		"(pv_num, on_grid_p, on_grid_q, on_grid_s, inv_cabin_temp, day_gen_power," +
		" day_runtime, total_gen_power, total_runtime, co2_reduce,timestamp)" +
		"values(?,?,?,?,?,?,?,?,?,?,NOW())";
		PreparedStatement ptmt = connection.prepareStatement(sql);
		ptmt.setString(1, pva2.getPv_num());
		if (pva2.getOn_grid_p() != null) {
			ptmt.setDouble(2, pva2.getOn_grid_p());
		}else {
			ptmt.setNull(2, Types.DOUBLE);
		}
		if (pva2.getOn_grid_q() != null) {
			ptmt.setDouble(3, pva2.getOn_grid_q());
		}else {
			ptmt.setNull(3, Types.DOUBLE);
		}
		if (pva2.getOn_grid_s() != null) {
			ptmt.setDouble(4, pva2.getOn_grid_s());
		}else {
			ptmt.setNull(4, Types.DOUBLE);
		}
		if (pva2.getInv_cabin_temp() != null) {
			ptmt.setDouble(5, pva2.getInv_cabin_temp());
		}else {
			ptmt.setNull(5, Types.DOUBLE);
		}
		if (pva2.getDay_gen_power() != null) {
			ptmt.setDouble(6, pva2.getDay_gen_power());
		}else {
			ptmt.setNull(6, Types.DOUBLE);
		}
		if (pva2.getDay_runtime() != null) {
			ptmt.setDouble(7, pva2.getDay_runtime());
		}else {
			ptmt.setNull(7, Types.DOUBLE);
		}
		if (pva2.getTotal_gen_power() != null) {
			ptmt.setDouble(8, pva2.getTotal_gen_power());
		}else {
			ptmt.setNull(8, Types.DOUBLE);
		}
		if (pva2.getTotal_runtime() != null) {
			ptmt.setDouble(9, pva2.getTotal_runtime());
		}else {
			ptmt.setNull(9, Types.DOUBLE);
		}
		if (pva2.getCo2_reduce() != null) {
			ptmt.setDouble(10, pva2.getCo2_reduce());
		}else {
			ptmt.setNull(10, Types.DOUBLE);
		}
		ptmt.execute();
		connection.close();
	}
}
