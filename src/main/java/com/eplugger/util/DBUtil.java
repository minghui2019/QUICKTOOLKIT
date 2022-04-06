package com.eplugger.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBUtil {
	// 利用线程保存conn连接，不用每次关闭
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	public static String getUrl() {
		return url;
	}

	static {
		try {
			Properties properties = new Properties();
			//用的是磁盘符的绝对路径 
			InputStream input = new BufferedInputStream(new FileInputStream("src/main/resource/jdbc.properties"));
			properties.load(input);
			driver = properties.getProperty("jdbc.driver");
			url = properties.getProperty("jdbc.url");
			user = properties.getProperty("jdbc.username");
			pwd = properties.getProperty("jdbc.password", "eplugger");
			// 1.加载驱动程序
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		// 从线程获得数据库的连接
		Connection conn = threadLocal.get();
		if (conn == null) {
			try {
				conn = DriverManager.getConnection(url, user, pwd);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static Map<String, String> getMeaningBySql(String sql){
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, ps, rs);
		}
		return map;
	}
	
	public static Map<String, String> getEntryNameBySql(String sql){
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				map.put(OtherUtils.deleteTextDecoration(data.getColumnName(i)).toUpperCase(), data.getColumnName(i).toUpperCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, ps, rs);
		}
		return map;
	}
	
	public static List<String[]> getCFGFromBySql(String sql){
		List<String[]> list = new ArrayList<>();
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, ps, rs);
		}
		return list;
	}
	
	public static String[] getStrsBySql(String sql){
		String[] strs = new String[2];
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				strs[0] = rs.getString(1);
				strs[1] = rs.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, ps, rs);
		}
		return strs;
	}
	
	public static void closeAll(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (conn != null) {
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int getOrdersFromEntityMeta(String sql) {
		int result = 0;
		try {
			Connection conn = getConnection();
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				while (rs.next()) {
					result = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				closeAll(conn, ps, rs);
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	/**
	 * <p>获取数据库类型，是否是sqlserver数据库</p>
	 * @return <code>true</code>:是sqlserver数据库；<code>false</code>:不是sqlserver数据库
	 */
	public static boolean isSqlServer() {
		return StringUtils.containsIgnoreCase(url, "sqlserver");
	}

	/**
	 * <p>获取数据库类型，是否是MySql数据库</p>
	 * @return <code>true</code>:是MySql数据库；<code>false</code>:不是MySql数据库
	 */
	public static boolean isMySql() {
		return StringUtils.containsIgnoreCase(url, "mysql");
	}

	/**
	 * <p>获取数据库类型，是否是Oracle数据库</p>
	 * @return <code>true</code>:是Oracle数据库；<code>false</code>:不是Oracle数据库
	 */
	public static boolean isOracle() {
		return StringUtils.containsIgnoreCase(url, "oracle");
	}
	
	/**
	 * <p>获取数据库名称</p>
	 * <p>oracle数据库username</p>
	 * @return
	 */
	public static String getDatabaseName() {
		if (isSqlServer()) {
			return url.substring(url.indexOf("=") + 1);
		} else if (isOracle()) {
			return user;
		}
		return "";
	}

	/**
	 * 从数据库名称获取系统版本
	 * @return
	 */
	public static String getEadpDataType() {
		String databaseName = DBUtil.getDatabaseName();
		Pattern pattern = Pattern.compile("[\\d]+[A-Z]?$");
		Matcher matcher = pattern.matcher(databaseName);
		String group = null;
		if (matcher.find()) {
			group = matcher.group();
		}
		if (group != null && group.indexOf("HIS") != -1) {
			return "V3.1.0";
		} else {
			return "V" + group.substring(0, 1) + "." + group.substring(1, 2) + "." + group.substring(2, 3);
		}
	}
}
