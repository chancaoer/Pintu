package com.ce.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.ce.dao.UserDao;

public class saveServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        PrintWriter out = response.getWriter();
        String userId = request.getParameter("login_id").trim();//获得请求的玩家id
        String level = request.getParameter("level").trim();
        int slevel = Integer.valueOf(level);
        String playTime = request.getParameter("playTime").trim();
        Date splayTime = Date.valueOf(playTime);
        
        Boolean saveResult = UserDao.saveZhanji(userId, slevel, splayTime);

		Map<String, String> params = new HashMap<>();
		JSONObject jsonObject = new JSONObject();

		if (saveResult) {
			params.put("Result", "savesuccess");
		} else {
			params.put("Result", "savefailed");
		}

		jsonObject.put("params", params);
        out.write(jsonObject.toString());
        System.out.println(jsonObject.toString());
        
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		doGet(request, response);
	}

}
