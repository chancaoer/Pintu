package com.ce.pintu.wanjia.model_wanjia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ce.pintu.MainActivity_94113;
import com.ce.pintu.R;
import com.ce.pintu.wanjia.Delivery;
import com.ce.pintu.wanjia.afterlogin.after_login;
import com.ce.pintu.wanjia.login.login;
import com.ce.pintu.wanjia.register.register;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class wanjia extends AppCompatActivity{
    private ImageButton model_wanjia_login = null;
    private ImageButton model_wanjia_register = null;
    private ImageButton model_wanjia_fanhui = null;

    //定义一个填写数据的对象
    private SharedPreferences.Editor loginEditor = null;
    //定义一个存储数据的对象
    private SharedPreferences loginPreference = null;

    private Boolean isAutoLogin = false;
    private Boolean isRememberPsd = false;
    private String wanjiaid;
    private String wanjiapsd;

    private static final String TAG = "wanjia";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.model_wanjia);

        model_wanjia_login = findViewById(R.id.model_wanjia_login);
        model_wanjia_register = findViewById(R.id.model_wanjia_register);
        model_wanjia_fanhui = findViewById(R.id.model_wanjia_fanhui);

        model_wanjia_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wanjia.this, MainActivity_94113.class);
                startActivity(intent);
                finish();
            }
        });

        model_wanjia_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPreference = getSharedPreferences("userlogininfo", MODE_PRIVATE);
                isAutoLogin = loginPreference.getBoolean("isautologin",false);
                isRememberPsd = loginPreference.getBoolean("isrememberpsd",false);
                wanjiaid = loginPreference.getString("loginid",null);
                wanjiapsd = loginPreference.getString("loginpsd",null);
                if (isAutoLogin && isRememberPsd){

                    autoLoginRequest(wanjiaid,wanjiapsd);

                }else {

                Intent intent = new Intent(wanjia.this,login.class);
                startActivity(intent);
                finish();
                }
            }
        });

        model_wanjia_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wanjia.this,register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //为解决空指针异常
    public void autoLoginRequest(final String login_id, final String login_psd) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/LoginServlet";
//        String url = "http://10.167.37.228:8080/Pintu_Chenyinru/LoginServlet";
        String tag = "AutoLogin";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            Log.i(TAG, "onResponse: " + result);
                            if (result.equals("loginsuccess")) {
//                                String name = login_id;
                                String currentname = Delivery.getDeli_id();
                                Log.i(TAG, "name-----------------------"+currentname);
                                Toast.makeText(wanjia.this,"您来拼图啦！"+currentname+"大宝贝",Toast.LENGTH_SHORT).show();
                                //页面跳转到after_login
                                Intent intent = new Intent(wanjia.this, after_login.class);
//                                intent.putExtra("login_id",name);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(wanjia.this, "亲，输错用户名或者密码了哦！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(wanjia.this,login.class);
                                startActivity(intent);
                                finish();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(wanjia.this, "哎呀怎么没有网络呢？", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(wanjia.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        })
            {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", login_id);
                params.put("login_psd", login_psd);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }

}
