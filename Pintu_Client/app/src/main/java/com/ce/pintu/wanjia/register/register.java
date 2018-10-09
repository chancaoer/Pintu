package com.ce.pintu.wanjia.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.ce.pintu.wanjia.login.login;
import com.ce.pintu.wanjia.model_wanjia.wanjia;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity implements View.OnClickListener{

    private EditText register_id = null;
    private EditText register_psd = null;
    private EditText confirm_psd = null;
    private ImageButton register_register = null;
    private TextView goto_login = null;
    private ImageButton register_fanhui = null;

    private String newpsd = null;
    private String repsd = null;

    private static final String TAG = "register";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        register_id = findViewById(R.id.register_id);
        register_psd = findViewById(R.id.register_psd);
        confirm_psd = findViewById(R.id.confirm_psd);
        register_register = findViewById(R.id.register_register);
        goto_login = findViewById(R.id.goto_login);
        register_fanhui = findViewById(R.id.register_fanhui);

        register_fanhui.setOnClickListener(this);
        goto_login.setOnClickListener(this);
        register_register.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_register:

                newpsd = register_psd.getText().toString();
                repsd = confirm_psd.getText().toString();

                if(newpsd.equals(repsd)){
                    RegisterRequest(register_id.getText().toString(),register_psd.getText().toString());

                }else{
                    Toast.makeText(register.this,"两次输入的密码不一致，重新输一下呗！",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.goto_login:
                Intent intent_register = new Intent(register.this, login.class);
                startActivity(intent_register);
                break;
            case R.id.login_fanhui:
                Intent intent_fanhui = new Intent(register.this, wanjia.class);
                startActivity(intent_fanhui);
                break;

        }
    }

    public void RegisterRequest (final String register_id, final String register_psd) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/RegisterServlet";

        String tag = "Register";

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
                            if (result.equals("registersuccess")) {
                                //页面跳转到after_login
                                String name = register_id;
                                Toast.makeText(register.this,"您这么好看当然注册成功啦！"+name+"大宝贝",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(register.this, login.class);
                                Delivery.setDeli_id(name);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(register.this, "注册失败！该用户账号已经存在啦！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(register.this, "哎呀怎么没有网络呢？", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(register.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("register_id", register_id);
                params.put("register_psd", register_psd);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }

}
