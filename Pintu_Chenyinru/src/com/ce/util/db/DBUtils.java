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

//�ṩ�������ݿ�Ͳ�����ӹ���
public class DBUtils extends HttpServlet {

	ServletConfig config;
	private static String username; // ��������ݿ��û���
	private static String password; // ��������ݿ���������
	private static String url; // �������ݿ�����URL
	private static Connection connection; // ��������

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config); // �̳и����init()����
		this.config = config; // ��ȡ������Ϣ
		
		//��web.xml�ж�ȡ��ʼ������
		username = config.getInitParameter("DBUsername"); // ��ȡ���ݿ��û���
		password = config.getInitParameter("DBPassword"); // ��ȡ���ݿ���������
		url = config.getInitParameter("ConnectionURL"); // ��ȡ���ݿ�����URL
	}

	//������ݿ����Ӷ���
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

	//�ر����е����ݿ�������Դ
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
