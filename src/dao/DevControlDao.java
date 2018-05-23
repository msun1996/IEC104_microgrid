package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc.C3P0Utils;

public class DevControlDao {
	// 添加设备 输入参数为设备编号和设备的类型号码
	public void addDev(String num , Integer dev_type) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		"insert into microgrids_devcontrol " +
		"(num,dev_type)" +
		"values(?,?)";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, num);
		ptmt.setInt(2, dev_type);
		ptmt.execute();
		conn.close();
	}
	// 更新设备类型号码
	public void updateDev(String num , Integer dev_type) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		" update microgrids_devcontrol" +
		" set dev_type=? " +
		" where num=? ";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setInt(1, dev_type);
		ptmt.setString(2, num);
		ptmt.execute();
		conn.close();
	}
	// 删除设备 输入设备编号
	public void delDev(String num) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = "" + 
		" delete from microgrids_devcontrol" + 
		" where num = ?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, num);
		ptmt.execute();
		conn.close();
	}
	// 查询返回设备是否存在
	public Boolean exit(String num) throws SQLException {
		Connection conn = C3P0Utils.getConnection();
		String sql = " " +
		"select num from microgrids_devcontrol" + 
		"where num = ?";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ptmt.setString(1, num);
		ResultSet rs = ptmt.executeQuery();
		if (rs.next()) {
			conn.close();
			return true;
		}else {
			conn.close();
			return false; 
		}	
	}
	// 查询所有存在设备编号,返回编号集合
	public List<String> query() throws SQLException {
		List<String> dev_nums = new ArrayList<String>();
		
		Connection conn = C3P0Utils.getConnection();
		String sql = "" +
		"select num from microgrids_devcontrol";
		PreparedStatement ptmt = conn.prepareStatement(sql);
		ResultSet rs = ptmt.executeQuery();
		while (rs.next()) {
			String dev_num = rs.getString("num");
			dev_nums.add(dev_num);
		}
		conn.close();
		return dev_nums;
	} 
	// 查询返回字段整型值(遥控)
	public Integer getint(String num, String field) throws SQLException {
		Integer value = 0;
		Connection conn = C3P0Utils.getConnection();
		String sql = "" +
		"select " + field + " from microgrids_devcontrol" +
		" where num = ?";
		PreparedStatement ptmt =conn.prepareStatement(sql);
		ptmt.setString(1, num);
		ResultSet rs = ptmt.executeQuery();
		while (rs.next()) {
			value = rs.getInt(field);
		}
		conn.close();
		return value;
	}
	// 查询返回字段浮点值(遥调)
	public Double getdouble(String num, String field) throws SQLException {
		Double value = 0.0;
		Connection conn = C3P0Utils.getConnection();
		String sql = "" +
		"select " + field + " from microgrids_devcontrol" +
		" where num = ?";
		PreparedStatement ptmt =conn.prepareStatement(sql);
		ptmt.setString(1, num);
		ResultSet rs = ptmt.executeQuery();
		while (rs.next()) {
			value = rs.getDouble(field);
		}
		conn.close();
		return value;
	}
}
