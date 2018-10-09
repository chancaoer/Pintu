package com.ce.model;

import java.sql.Date;
import java.sql.RowId;


public class User {
	
	//自增序号
	private int NO;

	public int getNO() {
		return NO;
	}

	public void setNO(int NO) {
		this.NO = NO;
	}

	//玩家账号
    private String userId;

    //玩家密码
    private String userPassword;
    
    //玩家闯关数
    private int level;
    
    //游戏时间
    private Date playTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getPlayTime() {
		return playTime;
	}

	public void setPlayTime(Date playTime) {
		this.playTime = playTime;
	}
    
    
    
}

