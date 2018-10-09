package com.ce.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ce.dao.UserDao;
import com.ce.model.User;

import net.sf.json.JSONObject;

//测试登录Servlet
public class LoginServlet extends HttpServlet {

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 设置响应内容类型  
        response.setContentType("text/html;charset=utf-8");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        try (PrintWriter out = response.getWriter()) {

            //获得请求中传来的用户名和密码
            String login_id = request.getParameter("login_id").trim();
            String login_psd = request.getParameter("login_psd").trim();

            //密码验证结果
            Boolean verifyResult = verifyLogin(login_id, login_psd);

            Map<String, String> params = new HashMap<>();
            JSONObject jsonObject = new JSONObject();

            if (verifyResult) {
                params.put("Result", "loginsuccess");
            } else {
                params.put("Result", "loginfailed");
            }

            jsonObject.put("params", params);
            out.write(jsonObject.toString());
            System.out.println(jsonObject.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    //验证用户名密码是否正确
    private Boolean verifyLogin(String userId, String userPassword) {
        User user = UserDao.queryUser(userId);
        
//        //账号密码验证
//        if(user != null && userPassword.equals(user.getUserPassword())){
//        	return true;
//        }else{
//        	return false;
//        }
        
        return null != user && userPassword.equals(user.getUserPassword());

    }


}
