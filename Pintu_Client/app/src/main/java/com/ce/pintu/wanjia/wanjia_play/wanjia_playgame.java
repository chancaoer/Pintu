package com.ce.pintu.wanjia.wanjia_play;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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
import com.ce.pintu.pintu_work.gameLayout;
import com.ce.pintu.wanjia.Delivery;
import com.ce.pintu.wanjia.afterlogin.after_login;
import com.ce.pintu.wanjia.login.login;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class wanjia_playgame extends AppCompatActivity {

    private ImageButton playgame_fanhui = null;
    private gameLayout gamepintu = null;
    private TextView level = null;
    private TextView timeleft = null;

    private static final String TAG = "wanjia_playgame";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        gamepintu = findViewById(R.id.gamepintu);
        level = findViewById(R.id.level);
        timeleft = findViewById(R.id.timeleft);
        playgame_fanhui = findViewById(R.id.playgame_fanhui);

        gamepintu.setTimeEnabled(true);
        gamepintu.setOnGamepintuListener(new gameLayout.GamepintuListener() {
            @Override
            public void nextLevel(final int nextlevel) {
                new AlertDialog.Builder(wanjia_playgame.this)
                        .setTitle("拼一拼就okay")
                        .setMessage("恭喜您！挑战成功！")
                        .setPositiveButton("继续挑战", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(gamepintu.nextLevel()) {
                                    level.setText(""+nextlevel);
//                                    level.setText(nextlevel);
                                }
                            }
                        }).setNegativeButton("放弃并保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = getIntent();
//                        String currentname = intent.getStringExtra("login_id");
                        String currentname = Delivery.getDeli_id();
                        Log.i(TAG, "login_id--------------------------------"+currentname);
                        String currentlevel = level.getText().toString();
                        Log.i(TAG, "currentlevel---------------------------------"+currentlevel);
                        int endLevel = Integer.parseInt(currentlevel);
                        Log.i(TAG, "endlevel------------------------------------"+endLevel);
                        String endTime = date();
                        Log.i(TAG, "endtime----------------------------------------"+endTime);
                        baocunZhanji(currentname,endLevel,endTime);

                    }
                }).show();
            }

            @Override
            public void timechanged(int currentTime) {
                timeleft.setText(""+currentTime);
            }

            @Override
            public void gameover() {
                new AlertDialog.Builder(wanjia_playgame.this)
                        .setTitle("拼一拼就okay")
                        .setMessage("哎呀，差一点就挑战成功了呢！")
                        .setPositiveButton("重新挑战", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                gamepintu.restart();
                            }
                        }).setNegativeButton("放弃并保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = getIntent();
//                        String currentname = intent.getStringExtra("login_id");
//                        intent.putExtra("login_id",currentname);
                        String currentname = Delivery.getDeli_id();
                        String currentlevel = level.getText().toString();
                        int endLevel = Integer.parseInt(currentlevel);
                        endLevel = endLevel-1;
                        String endTime = date();
                        baocunZhanji(currentname,endLevel,endTime);
                    }
                }).show();
            }
        });

        playgame_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(wanjia_playgame.this,after_login.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected void onPause(){
        super.onPause();
        gamepintu.pause();
    }

    protected void onResume(){
        super.onResume();
        gamepintu.resume();
    }

    //保存战绩
    public void baocunZhanji(final String login_id, final int level, final String playTime) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/saveServlet";

        String tag = "saveZhanji";

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
                            if (result.equals("savesuccess")) {
                                Toast.makeText(wanjia_playgame.this,"保存成功！",Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(wanjia_playgame.this, "亲，保存失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(wanjia_playgame.this, "哎呀怎么没有网络呢？", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(wanjia_playgame.this, "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", login_id);
                params.put("level",level+"");
                params.put("playTime",playTime);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);

        //将请求添加到队列中
        requestQueue.add(request);

    }

    //获取年月日
    public String date(){
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return mYear + "-" +mMonth + "-" + mDay;

    }

}
