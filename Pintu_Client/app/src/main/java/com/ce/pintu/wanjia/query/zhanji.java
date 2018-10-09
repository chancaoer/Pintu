package com.ce.pintu.wanjia.query;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ce.pintu.wanjia.getsetjson.zhanjiAdapter;
import com.ce.pintu.wanjia.getsetjson.zhanjitable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class zhanji extends AppCompatActivity{
    private ImageButton zhanji_fanhui = null;
    private LinearLayout zhanji_biaotou = null;
    private TextView zhanji_wanjiaid = null;
    private TextView zhanji_level = null;
    private TextView zhanji_playtime = null;
    private ListView zhanjilist = null;


    private static final String TAG = "zhanji";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhanji);

        zhanji_fanhui = findViewById(R.id.zhanji_fanhui);
        zhanji_biaotou = findViewById(R.id.zhanjibiaotou);
        zhanji_wanjiaid = findViewById(R.id.zhanji_wanjiaid);
        zhanji_level = findViewById(R.id.zhanji_level);
        zhanji_playtime = findViewById(R.id.zhanji_playtime);
        zhanjilist = findViewById(R.id.zhanjilist);

//        Intent intent = getIntent();
//        String name = intent.getStringExtra("login_id");
        String name = Delivery.getDeli_id();
        zhanji_return(name);

        zhanji_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(zhanji.this,after_login.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void zhanji_return (final String login_id) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/zhanjiServlet";

        String tag = "zj_return";

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
                        List<zhanjitable> zhanjis = gson.fromJson
                                (response, new TypeToken<List<zhanjitable>>() {}.getType());
                        if (zhanjis.size() != 0) {
                            zhanjiAdapter zjAdapter = new zhanjiAdapter(zhanji.this, R.layout.zhanjiitem, zhanjis);
                            ListView listView = findViewById(R.id.zhanjilist);
                            listView.setAdapter(zjAdapter);
                        } else{
                            Toast.makeText(zhanji.this, "还没有战绩哦！快去闯关吧！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(zhanji.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
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
