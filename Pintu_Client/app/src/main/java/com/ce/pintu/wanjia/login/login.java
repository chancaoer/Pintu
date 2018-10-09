package com.ce.pintu.wanjia.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ce.pintu.R;
import com.ce.pintu.wanjia.Delivery;
import com.ce.pintu.wanjia.afterlogin.after_login;
import com.ce.pintu.wanjia.register.register;
import com.ce.pintu.wanjia.model_wanjia.wanjia;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "login";

    private EditText login_id = null;
    private EditText login_psd = null;
    private CheckBox remember_psd = null;
    private CheckBox auto_login = null;
    private ImageButton login_login = null;
    private TextView goto_register = null;
    private ImageButton login_fanhui = null;

    //选择用SharePreferences存储数据是因为只在本手机登录自己的游戏;
    //定义一个填写数据的对象
    private SharedPreferences.Editor loginEditor = null;
    //定义一个存储数据的对象
    private SharedPreferences loginPreference = null;

    private String wanjiaid = null;
    private String wanjiapsd = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_id = findViewById(R.id.login_id);
        login_psd = findViewById(R.id.login_psd);
        remember_psd = findViewById(R.id.remenber_psd);
        auto_login = findViewById(R.id.auto_login);
        login_login = findViewById(R.id.login_login);
        goto_register = findViewById(R.id.goto_register);
        login_fanhui = findViewById(R.id.login_fanhui);

        //设置监听
        login_login.setOnClickListener(this);
        login_fanhui.setOnClickListener(this);
        goto_register.setOnClickListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(remember_psd.isChecked()){
            loginPreference = getSharedPreferences("userlogininfo", MODE_PRIVATE);
            loginEditor = loginPreference.edit();
            loginEditor.putString("loginid",login_id.getText().toString());
            loginEditor.putString("loginpsd",login_psd.getText().toString());
            loginEditor.putBoolean("isrememberpsd",remember_psd.isChecked());
            loginEditor.putBoolean("isautologin",auto_login.isChecked());
            loginEditor.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginPreference = getSharedPreferences("userlogininfo", MODE_PRIVATE);
        wanjiapsd = loginPreference.getString("loginpsd",null);
        wanjiaid = loginPreference.getString("loginid",null);
        if(wanjiaid!=null && wanjiapsd!=null){
            login_id.setText(wanjiaid);
            login_psd.setText(wanjiapsd);
            remember_psd.setChecked(true);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login_login:

                LoginRequest(login_id.getText().toString(), login_psd.getText().toString());

                break;
            case R.id.goto_register:
                Intent intent_register = new Intent(login.this, register.class);
                startActivity(intent_register);
                finish();
                break;
            case R.id.login_fanhui:
                Intent intent_fanhui = new Intent(login.this, wanjia.class);
                startActivity(intent_fanhui);
                finish();
                break;

        }
    }

    public void LoginRequest(final String login_id, final String login_psd) {

        //请求地址
//        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/LoginServlet";
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/LoginServlet";

        String tag = "Login";

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
                                String name = login_id;
                                Toast.makeText(login.this,"您来拼图啦！"+name+"大宝贝",Toast.LENGTH_SHORT).show();
                                //页面跳转到after_login
                                Intent intent = new Intent(login.this, after_login.class);
//                                intent.putExtra("login_id",name);
                                Delivery.setDeli_id(name);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(login.this, "亲，输错用户名或者密码了哦！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(login.this, "哎呀怎么没有网络呢？", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(login.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
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

