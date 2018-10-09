package com.ce.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.omg.CORBA.PUBLIC_MEMBER;

import com.ce.dao.UserDao;
import com.ce.model.User;
import com.mysql.fabric.xmlrpc.base.Params;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class zhanjiServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        String userId = request.getParameter("login_id").trim();//获得请求的玩家id
        
        ArrayList<User> zhanjiList = UserDao.queryZhanji(userId);
        
        JSONArray jsonArrayResult = new JSONArray();
        JSONObject jsonObject = new JSONObject();
   
        for(int i=0;i<zhanjiList.size();i++){
        	jsonObject.put("wanjiaId",zhanjiList.get(i).getUserId() );
        	jsonObject.put("level", zhanjiList.get(i).getLevel());
            jsonObject.put("playTime",zhanjiList.get(i).getPlayTime().toString());
            jsonArrayResult.add(jsonObject);
        }
        
        out.write(jsonArrayResult.toString());
        System.out.println(jsonArrayResult.toString());
        
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
	
}
