package com.ce.model;

import java.sql.Date;
import java.sql.RowId;


public class User {
	
	//�������
	private int NO;

	public int getNO() {
		return NO;
	}

	public void setNO(int NO) {
		this.NO = NO;
	}

	//����˺�
    private String userId;

    //�������
    private String userPassword;
    
    //��Ҵ�����
    private int level;
    
    //��Ϸʱ��
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

