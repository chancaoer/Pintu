package com.ce.dao;

import java.awt.List;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections.map.StaticBucketMap;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.ce.model.User;
import com.ce.util.db.DBUtils;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

public class UserDao {

	// 玩家登录
	public static User queryUser(String userId) {

		
		// 获得数据库的连接对象
		Connection connection = (Connection) DBUtils.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet login_resultSet = null;

		// 生成SQL代码
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT * FROM wanjia WHERE userId=?");

		// 设置数据库的字段值
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, userId);

			login_resultSet = preparedStatement.executeQuery();
			User user = new User();
			if (login_resultSet.next()) {
				user.setUserId(login_resultSet.getString("userId"));
				user.setUserPassword(login_resultSet.getString("userPassword"));
				return user;
			} else {
				return null;
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		} finally {
			DBUtils.closeAll(connection, preparedStatement, login_resultSet);
		}
	}

	// 玩家注册
	public static Boolean insertUser(String userId, String userPassword) {
		// 获得数据库的连接对象
		Connection connection = (Connection) DBUtils.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet vertify_resultSet = null;

		// 生成SQL代码
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT * FROM wanjia WHERE userId=?");
		
		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, userId);
			vertify_resultSet = preparedStatement.executeQuery();
			
			if(vertify_resultSet.next()){
				DBUtils.closeAll(connection, preparedStatement, null);
				return false;
			}else{
				StringBuilder sqlStatement1 = new StringBuilder();
				PreparedStatement preparedStatement1 = null;
				
				sqlStatement1.append("INSERT INTO wanjia(userId,userPassword) VALUES(?,?)");
				preparedStatement1 = (PreparedStatement) connection.prepareStatement(sqlStatement1.toString());
				preparedStatement1.setString(1, userId);
				preparedStatement1.setString(2, userPassword);

				int register_resultSet = preparedStatement1.executeUpdate();
				if (register_resultSet != 0) {
					return true;
				}else {
					return false;
				}
			}
			
		} catch (SQLException ex) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		} finally {
			DBUtils.closeAll(connection, preparedStatement, null);
		}

	}

	// 查看个人战绩
	public static ArrayList<User> queryZhanji(String userId) {
		// 获得数据库的连接对象
		Connection connection = (Connection) DBUtils.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet zhanji_resultSet = null;

		// 生成SQL代码
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("SELECT userId,level,playTime FROM wanjiazhanji WHERE userId=?");

		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, userId);

			zhanji_resultSet = preparedStatement.executeQuery();
//			User currentUser = new User();
			ArrayList<User> currentUserZJ = new ArrayList<User>();
			while (zhanji_resultSet.next()) {
				User currentUser = new User();
				currentUser.setUserId(zhanji_resultSet.getString("userId"));
				currentUser.setLevel(zhanji_resultSet.getInt("level"));
				currentUser.setPlayTime(zhanji_resultSet.getDate("playTime"));
				currentUserZJ.add(currentUser);
			}
			return currentUserZJ;

		} catch (SQLException ex) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		} finally {
			DBUtils.closeAll(connection, preparedStatement, zhanji_resultSet);
		}

	}

	// 查看排名
	public static ArrayList<User> queryPaiming(String userId) {
		// 获得数据库的连接对象
		Connection connection = (Connection) DBUtils.getConnection();
		PreparedStatement preparedStatement = null;
		ResultSet paiming_resultSet = null;

		// 生成SQL代码
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement
				.append("SELECT (@i :=@i+1) AS NO,userId,level FROM (SELECT @i :=0) AS it,wanjiazhanji ORDER BY level DESC");

		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement.toString());
			// preparedStatement.setString(1, userId);

			paiming_resultSet = preparedStatement.executeQuery();
//			User allUser = new User();
			ArrayList<User> allUserZJ = new ArrayList<User>();
			while (paiming_resultSet.next()) {
				User allUser = new User();
				allUser.setNO(paiming_resultSet.getInt("NO"));
				allUser.setUserId(paiming_resultSet.getString("userId"));
				allUser.setLevel(paiming_resultSet.getInt("level"));
				allUserZJ.add(allUser);
			}
			return allUserZJ;

		} catch (SQLException ex) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null,
					ex);
			return null;
		} finally {
			DBUtils.closeAll(connection, preparedStatement, paiming_resultSet);
		}
	}

	// 保存战绩
	public static Boolean saveZhanji(String userId, int slevel, Date splayTime) {
		// 获得数据库的连接对象
		Connection connection = (Connection) DBUtils.getConnection();
		PreparedStatement preparedStatement = null;

		// 生成SQL代码
		StringBuilder sqlStatement = new StringBuilder();
		sqlStatement.append("INSERT INTO wanjiazhanji(userId,level,playTime) VALUES(?,?,?)");

		try {
			preparedStatement = (PreparedStatement) connection.prepareStatement(sqlStatement.toString());
			preparedStatement.setString(1, userId);
			preparedStatement.setInt(2, slevel);
			preparedStatement.setDate(3, splayTime);

			int save_resultSet = preparedStatement.executeUpdate();

			if (save_resultSet != 0) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException ex) {
			Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		} finally {
			DBUtils.closeAll(connection, preparedStatement, null);
		}
	}

}
