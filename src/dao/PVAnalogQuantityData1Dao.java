package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import jdbc.C3P0Utils;
import model.PVAnalogQuantityData1;

public class PVAnalogQuantityData1Dao {
	// 添加 （输入类）
	public void addPVAnalogQuantityData1(PVAnalogQuantityData1 pva1) throws SQLException {
		Connection connection = C3P0Utils.getConnection();
		String sql = ""+
		"insert into microgrids_pvanalogquantitydata1" +
		"(pv_num, matrix_cur, matrix_volt, grid_volt_ab, grid_volt_bc, grid_volt_ca," +
		" on_grid_cur_a, on_grid_cur_b, on_grid_cur_c, power_factor_a, power_factor_b, power_factor_c,timestamp)" +
		"values(?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
		PreparedStatement ptmt = connection.prepareStatement(sql);
		ptmt.setString(1, pva1.getPv_num());
		if (pva1.getMatrix_cur() != null) {
			ptmt.setDouble(2, pva1.getMatrix_cur());
		}else {
			ptmt.setNull(2, Types.DOUBLE);
		}
		if (pva1.getMatrix_volt() != null) {
			ptmt.setDouble(3, pva1.getMatrix_volt());
		}else {
			ptmt.setNull(3, Types.DOUBLE);
		}
		if (pva1.getGrid_volt_ab() != null) {
			ptmt.setDouble(4, pva1.getGrid_volt_ab());
		}else {
			ptmt.setNull(4, Types.DOUBLE);
		}
		if (pva1.getGrid_volt_bc() != null) {
			ptmt.setDouble(5, pva1.getGrid_volt_bc());
		}else {
			ptmt.setNull(5, Types.DOUBLE);
		}
		if (pva1.getGrid_volt_ca() != null) {
			ptmt.setDouble(6, pva1.getGrid_volt_ca());
		}else {
			ptmt.setNull(6, Types.DOUBLE);
		}
		if (pva1.getOn_grid_cur_a() != null) {
			ptmt.setDouble(7, pva1.getOn_grid_cur_a());
		}else {
			ptmt.setNull(7, Types.DOUBLE);
		}
		if (pva1.getOn_grid_cur_b() != null) {
			ptmt.setDouble(8, pva1.getOn_grid_cur_b());
		}else {
			ptmt.setNull(8, Types.DOUBLE);
		}
		if (pva1.getOn_grid_cur_c() != null) {
			ptmt.setDouble(9, pva1.getOn_grid_cur_c());
		}else {
			ptmt.setNull(9, Types.DOUBLE);
		}
		if (pva1.getPower_factor_a() != null) {
			ptmt.setDouble(10, pva1.getPower_factor_a());
		}else {
			ptmt.setNull(10, Types.DOUBLE);
		}		
		if (pva1.getPower_factor_b() != null) {
			ptmt.setDouble(11, pva1.getPower_factor_b());
		}else {
			ptmt.setNull(11, Types.DOUBLE);
		}
		if (pva1.getPower_factor_c() != null) {
			ptmt.setDouble(12, pva1.getPower_factor_c());
		}else {
			ptmt.setNull(12, Types.DOUBLE);
		}
		ptmt.execute();
		connection.close();
	}
}
