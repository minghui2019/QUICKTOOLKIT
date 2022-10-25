package com.eplugger.utils;

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

import cn.hutool.core.lang.Assert;
import com.eplugger.common.lang.StringUtils;
import com.eplugger.onekey.schoolInfo.entity.SchoolInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBUtils {
	// 必须首先设置学校的信息
	public static SchoolInfo schoolInfo;
	// 利用线程保存conn连接，不用每次关闭
	private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
	private static String driver;
	private static String url;
	private static String user;
	private static String pwd;
	
	public static String getUrl() {
		return url;
	}

	private static void initDBParam() {
		if (driver != null) {
			return;
		}
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		log.debug(schoolInfo.toString());
		try {
			Properties properties = new Properties();
			//用的是磁盘符的绝对路径
			InputStream input = new BufferedInputStream(new FileInputStream("src/main/resource/jdbc/" + DBUtils.getDatabaseName() + ".properties"));
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
			DBUtils.initDBParam();
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
			ps = new LoggableStatement(conn, sql);
			log.debug("Executing SQL: " + ((LoggableStatement) ps).getQueryString());
			rs = ps.executeQuery();
			while (rs.next()) {
				map.put(rs.getString(1), rs.getString(2));
			}
		} catch (SQLException e) {
			log.debug("Executing SQL: " + sql + "执行失败！");
		} finally {
			closeAll(conn, ps, rs);
		}
		return map;
	}

	public static Map<String, String> getEntryNameBySql(String sql) {
		Map<String, String> map = new HashMap<String, String>();
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = new LoggableStatement(conn, sql);
			log.debug("Executing SQL: " + ((LoggableStatement) ps).getQueryString());
			rs = ps.executeQuery();
			ResultSetMetaData data = rs.getMetaData();
			for (int i = 1; i <= data.getColumnCount(); i++) {
				map.put(OtherUtils.deleteTextDecoration(data.getColumnName(i)).toUpperCase(), data.getColumnName(i).toUpperCase());
			}
		} catch (SQLException e) {
			log.debug("Executing SQL: " + sql + "执行失败！");
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
			ps = new LoggableStatement(conn, sql);
			log.debug("Executing SQL: " + ((LoggableStatement) ps).getQueryString());
			rs = ps.executeQuery();
			while (rs.next()) {
				list.add(new String[]{rs.getString(1), rs.getString(2), rs.getString(3)});
			}
		} catch (SQLException e) {
			log.debug("Executing SQL: " + sql + "执行失败！");
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
			ps = new LoggableStatement(conn, sql);
			log.debug("Executing SQL: " + ((LoggableStatement) ps).getQueryString());
			rs = ps.executeQuery();
			while (rs.next()) {
				strs[0] = rs.getString(1);
				strs[1] = rs.getString(2);
			}
		} catch (SQLException e) {
			log.debug("Executing SQL: " + sql + "执行失败！");
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
		Connection conn = getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = new LoggableStatement(conn, sql);
			log.debug("Executing SQL: " + ((LoggableStatement) ps).getQueryString());
			rs = ps.executeQuery();
			while (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			log.debug("Executing SQL: " + sql + "执行失败！");
		} finally {
			closeAll(conn, ps, rs);
		}
		return result;
	}
	
	/**
	 * <p>获取数据库类型，是否是sqlserver数据库</p>
	 * @return <code>true</code>:是sqlserver数据库；<code>false</code>:不是sqlserver数据库
	 */
	public static boolean isSqlServer() {
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		return StringUtils.equals(schoolInfo.dbType, "sqlserver");
	}

	/**
	 * <p>获取数据库类型，是否是MySql数据库</p>
	 * @return <code>true</code>:是MySql数据库；<code>false</code>:不是MySql数据库
	 */
	public static boolean isMySql() {
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		return StringUtils.equals(schoolInfo.dbType, "mysql");
	}

	/**
	 * <p>获取数据库类型，是否是Oracle数据库</p>
	 * @return <code>true</code>:是Oracle数据库；<code>false</code>:不是Oracle数据库
	 */
	public static boolean isOracle() {
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		return StringUtils.equals(schoolInfo.dbType, "oracle");
	}
	
	/**
	 * <p>获取数据库名称</p>
	 * <p>oracle数据库username</p>
	 * @return
	 */
	public static String getDatabaseName() {
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		return schoolInfo.dbName;
	}

	/**
	 * 从数据库名称获取系统版本
	 * @return
	 */
	public static String getEadpDataType() {
		Assert.notNull(schoolInfo, "必须首先在@Before设置学校的信息！");
		return schoolInfo.version;
	}
}
