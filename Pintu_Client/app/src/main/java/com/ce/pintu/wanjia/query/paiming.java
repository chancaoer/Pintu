package com.ce.pintu.wanjia.query;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.ce.pintu.wanjia.getsetjson.paimingAdapter;
import com.ce.pintu.wanjia.getsetjson.zhanjitable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class paiming extends AppCompatActivity {
    private ImageButton paiming_fanhui;

    private static final String TAG = "paiming";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paiming);

        paiming_fanhui = findViewById(R.id.paiming_fanhui);

        String name = Delivery.getDeli_id();
        paiming_return(name);

        paiming_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(paiming.this, after_login.class);
                startActivity(intent);
                finish();

            }
        });
    }

    public void paiming_return (final String login_id) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/paimingServlet";

        String tag = "pm_return";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //防止重复请求
        requestQueue.cancelAll(tag);

        //创建StringRequest，定义字符串请求的请求方式为POST
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse:--------------------------------------------------- " + response);

                        Gson gson = new Gson();
                        List<zhanjitable> paiming = gson.fromJson
                                (response, new TypeToken<List<zhanjitable>>() {}.getType());
                        if (paiming.size() != 0) {
                            paimingAdapter pmAdaprter = new paimingAdapter(paiming.this, R.layout.paimingitem, paiming);
                            ListView listView = findViewById(R.id.paiminglist);
                            listView.setAdapter(pmAdaprter);
                        }else{
                            Toast.makeText(paiming.this, "还没有人玩过这一款游戏哦！快去当吃螃蟹的第一人吧！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(paiming.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", login_id);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }
}
