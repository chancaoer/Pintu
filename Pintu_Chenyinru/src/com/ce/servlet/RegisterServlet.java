package com.ce.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import com.ce.dao.UserDao;

//注册servlet
public class RegisterServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// 设置响应内容类型
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		PrintWriter out = response.getWriter();
		String register_id = request.getParameter("register_id");
		String register_psd = request.getParameter("register_psd");

		Boolean insertResult = UserDao.insertUser(register_id, register_psd);

		Map<String, String> params = new HashMap<>();
		JSONObject jsonObject = new JSONObject();

		if (insertResult) {
			params.put("Result", "registersuccess");
		} else {
			params.put("Result", "registerfailed");
		}

		jsonObject.put("params", params);
        out.write(jsonObject.toString());
        System.out.println(jsonObject.toString());

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

}
