package com.ce.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.ce.dao.UserDao;
import com.ce.model.User;

public class paimingServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        String userId = request.getParameter("login_id").trim();//获得请求的玩家id
		
        ArrayList<User> paimingList = UserDao.queryPaiming(userId);
        
        JSONArray jsonArrayResult = new JSONArray();
        JSONObject jsonObject = new JSONObject();
   
        for(int i=0;i<paimingList.size();i++){
        	jsonObject.put("NO",paimingList.get(i).getNO());
        	jsonObject.put("wanjiaId",paimingList.get(i).getUserId());
        	jsonObject.put("level", paimingList.get(i).getLevel());
            jsonArrayResult.add(jsonObject);
        }
        
        out.write(jsonArrayResult.toString());
        System.out.println(jsonArrayResult.toString());
        
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
