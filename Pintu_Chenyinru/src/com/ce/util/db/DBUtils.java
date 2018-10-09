package com.ce.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

//提供连接数据库和拆解链接功能
public class DBUtils extends HttpServlet {

	ServletConfig config;
	private static String username; // 定义的数据库用户名
	private static String password; // 定义的数据库连接密码
	private static String url; // 定义数据库连接URL
	private static Connection connection; // 定义连接

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config); // 继承父类的init()方法
		this.config = config; // 获取配置信息
		
		//从web.xml中读取初始化参数
		username = config.getInitParameter("DBUsername"); // 获取数据库用户名
		password = config.getInitParameter("DBPassword"); // 获取数据库连接密码
		url = config.getInitParameter("ConnectionURL"); // 获取数据库连接URL
	}

	//获得数据库连接对象
	public static Connection getConnection() {
	        try {
	            Class.forName("com.mysql.jdbc.Driver").newInstance();
	            connection = DriverManager.getConnection(url, username, password);
	        } catch (ClassNotFoundException | InstantiationException
	                | IllegalAccessException | SQLException ex) {
	            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        return connection;
	    }

	//关闭所有的数据库连接资源
	public static void closeAll(Connection connection, Statement statement,
			ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (SQLException ex) {
			Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null,ex);
		}
	}

}
