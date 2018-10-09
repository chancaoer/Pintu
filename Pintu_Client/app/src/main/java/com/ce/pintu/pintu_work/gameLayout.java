package com.ce.pintu.pintu_work;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.ce.pintu.wanjia.wanjia_play.wanjia_playgame;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class gameLayout extends RelativeLayout implements View.OnClickListener {

    private static final String TAG = "gameLayout";

    private int mColumn = 3;
    private int mWidth;//游戏面板的宽度
    private int mPadding;
    private int mMargin = 3;//dp
    private ImageView[] mGamepintuItems;
    private int mItemWidth;
    private Bitmap mBitmap;//存放的是游戏的图片
    private List<imagePiece> mItemBitmaps;//存储切完后的碎片
    private boolean once;

    private boolean isGameOver;
    private boolean isGameSuccess;

    //声明一个接口，功能为下一关、改变时间、游戏结束
    public interface GamepintuListener {
        void nextLevel(int nextlevel);
        void timechanged(int currentTime);
        void gameover();
    }

    public GamepintuListener mListener;

    //设置接口回调
    public void setOnGamepintuListener(GamepintuListener mListener) {
        this.mListener = mListener;
    }

    private int level=1;
    private int mtime;// 定义剩余时间
    private static final int TIME_CHANGED = 0x110;
    private static final int NEXT_LEVEL = 0x111;

    private Handler mhandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case TIME_CHANGED:
                    if (isGameSuccess || isGameOver || isPause){
                        return;
                    }
                    if (mListener != null) {
                        mListener.timechanged(mtime);
                        if (mtime == 0) {
                            isGameOver = true;
                            mListener.gameover();
                            return;
                        }
                    }
                    mtime--;
                    mhandler.sendEmptyMessageDelayed(TIME_CHANGED, 1000);
                    break;
                case NEXT_LEVEL:
                    level = level + 1;
                    if (mListener != null) {
                        mListener.nextLevel(level);
                    } else {
                        nextLevel();
                    }
                    break;
                default:
                    break;
            }
        };
    };

    //默认不启动时间
    private boolean isTimeEnabled = false;
    //设置是否启动时间
    public void setTimeEnabled(boolean isTimeEnabled) {
        this.isTimeEnabled = isTimeEnabled;
    }

    private void CheckTimeEnabled() {
        if (isTimeEnabled) {
            // 根据当前等级计算时间
            countTimeBaseLevel();
            mhandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    public boolean nextLevel() {
        if(level<7){
            this.removeAllViews();
            mAnimLayout = null;
            mColumn++;
            isGameSuccess = false;
            CheckTimeEnabled();
            initBitmap();
            initItem();
           return true;
        }else{
            Toast.makeText(getContext(),"您咋这么厉害呢？都通关啦！",Toast.LENGTH_LONG).show();
            String currentname = Delivery.getDeli_id();
            String currentlevel = level+"";
            int endLevel = Integer.parseInt(currentlevel);
            endLevel = endLevel-1;
            String endTime = date();
            tgbcZhanji(currentname,endLevel,endTime);
            return false;
        }
    }

    public void restart() {
        isGameOver=false;
        mColumn--;
        nextLevel();
    }

    //暂停与恢复
    private boolean isPause;
    public void pause() {
        isPause=true;
        mhandler.removeMessages(TIME_CHANGED);
    }

    public void resume() {
        if(isPause)
        {
            isPause=false;
            mhandler.sendEmptyMessage(TIME_CHANGED);
        }
    }

    //判断每次移动下是否成功
    private void CheckSuccess() {

        boolean isSuccess = true;
        for (int i=0;i<mGamepintuItems.length;i++) {
            ImageView imageView = mGamepintuItems[i];
            if (getImageIndexByTag((String) imageView.getTag()) != i) {
                isSuccess = false;
            }
        }

        if (isSuccess) {
            isGameSuccess=true;
            mhandler.removeMessages(TIME_CHANGED);
//            Toast.makeText(getContext(), "闯关成功！下一关马上就来！", Toast.LENGTH_SHORT).show();
            mhandler.sendEmptyMessage(NEXT_LEVEL);
        }
    }

    //构造方法，第一个调用第二个，第二个调用第三个，第三个进行初始化的代码
    public gameLayout(Context context) {
        this(context,null);
    }

    public gameLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public gameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //由单位转换TypedValue将mMargin由dp转为px，
        mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics());

        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());
    }

    //将当前布局设置成正方形
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 取宽和高中的最小值作为面板的宽度
        mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            // 进行切图,排序
            initBitmap();
            // 设置item的宽高属性
            initItem();
            //判断是否开启时间
            CheckTimeEnabled();
            once = true;
        }
        // 设置为正方形
        setMeasuredDimension(mWidth, mWidth);
    }

    private void countTimeBaseLevel() {
        mtime = (int) (Math.pow(2, level) * 15);
    }

    // 进行切图和排序
    private void initBitmap() {
//            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);//寻找资源
        switch (level){
            case 1:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cat);
                break;
            case 2:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pika);
                break;
            case 3:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bear);
                break;
            case 4:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.cutebaby);
                break;
            case 5:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dog);
                break;
            case 6:
                mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.meinv);
            default:
                break;

        }

        //调用cutImage方法切找到的资源，结果返回切割后的碎片的集合，存放到mItemBitmaps
        mItemBitmaps = imageCutter.cutImage(mBitmap, mColumn);

        // sort可以排序，new Comparator后sort乱序
        Collections.sort(mItemBitmaps, new Comparator<imagePiece>() {
            @Override
            public int compare(imagePiece a, imagePiece b) {
                return Math.random() > 0.5 ? 1 : -1;
            }
        });

//        mBitmap.recycle();

    }

    // 设置ImageView(item)的宽高属性，将碎片排列位置
    private void initItem() {
        mItemWidth = (mWidth - mPadding * 2 - mMargin * (mColumn - 1)) / mColumn;//每个碎片的宽度
//        mItemWidth = (mWidth - mMargin * (mColumn - 1))/mColumn;
        mGamepintuItems = new ImageView[mColumn * mColumn];//设置关卡产生的碎片数

        //生成item，设置rule：item建立的关系
        for (int i = 0; i < mGamepintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mItemBitmaps.get(i).getBitmap());//此时已经乱序
            mGamepintuItems[i] = item;
            //设置rule的时候需要用到id
            item.setId(i + 1);
            // 在item的tag中存储了i和index,index是真正的顺序,利用index判断是否拼图成功
            item.setTag(i + "_" + mItemBitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);

            //设置rule，即设置右边距和上边距
            // 设置item间横向间隙，通过rightMargin
            // 不是最后一列
            if ((i + 1) % mColumn != 0) {
                lp.rightMargin = mMargin;
            }
            // 不是第一列
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mGamepintuItems[i - 1].getId());
            }
            // 如果不是第一行，设置topMargin和rule
            if ((i + 1) > mColumn) {
                lp.topMargin = mMargin;
                lp.addRule(RelativeLayout.BELOW, mGamepintuItems[i - mColumn].getId());
            }
            addView(item, lp);
        }
    }

    private ImageView mFirst;//点击第一个item
    private ImageView mSecond;//点击第二个item
    private boolean isAniming;
    @Override
    public void onClick(View v) {

        //防止正在动画的时候用户疯狂点
        if (isAniming)
            return;

        // 两次点击同一个item
        if (mFirst == v) {
            mFirst.setColorFilter(null);
            mFirst = null;
            return;
        }

        if (mFirst == null) {
            mFirst = (ImageView) v;
            mFirst.setColorFilter(Color.parseColor("#55FF0000"));//前面两个数是表示透明度
        } else {
            //交换item
            mSecond = (ImageView) v;
            exchangeView();
        }
    }

    private RelativeLayout mAnimLayout;//定义一个动画层

    //实现交换item的方法
    private void exchangeView()  {
        mFirst.setColorFilter(null);

        setUpAnimaLayout();

        ImageView first = new ImageView(getContext());
        final Bitmap firstBitmap = mItemBitmaps.get(getImageIdByTag((String) mFirst.getTag())).getBitmap();
        first.setImageBitmap(firstBitmap);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        //设置动画层的rule
        lp1.leftMargin = mFirst.getLeft() - mPadding;
        lp1.topMargin = mFirst.getTop() - mPadding;
        first.setLayoutParams(lp1);
        mAnimLayout.addView(first);

        ImageView second = new ImageView(getContext());
        final Bitmap secondBitmap = mItemBitmaps.get(getImageIdByTag((String) mSecond.getTag())).getBitmap();
        second.setImageBitmap(secondBitmap);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = mSecond.getLeft() - mPadding;
        lp2.topMargin = mSecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        mAnimLayout.addView(second);

        //设置动画
        TranslateAnimation anim1 = new TranslateAnimation(0, mSecond.getLeft() - mFirst.getLeft(), 0, mSecond.getTop() - mFirst.getTop());
        anim1.setDuration(300);
        anim1.setFillAfter(true);
        first.startAnimation(anim1);

//        TranslateAnimation anim2 = new TranslateAnimation(0,mFirst.getLeft() - mSecond.getLeft(),0,mFirst.getTop() - mSecond.getTop());
        TranslateAnimation anim2 = new TranslateAnimation(0, -mSecond.getLeft() + mFirst.getLeft(), 0, -mSecond.getTop() + mFirst.getTop());
        anim2.setDuration(300);
        anim2.setFillAfter(true);
        second.startAnimation(anim2);

        //动画监听
        anim1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub
                //隐藏图片
                mFirst.setVisibility(View.INVISIBLE);
                mSecond.setVisibility(View.INVISIBLE);
                isAniming = true;
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                // TODO Auto-generated method stub
                String firstTag = (String) mFirst.getTag();
                String secondTag = (String) mSecond.getTag();
                mFirst.setImageBitmap(secondBitmap);
                mSecond.setImageBitmap(firstBitmap);
                mFirst.setTag(secondTag);
                mSecond.setTag(firstTag);
                //显示图片
                mFirst.setVisibility(View.VISIBLE);
                mSecond.setVisibility(View.VISIBLE);

                mAnimLayout.removeAllViews();
                mFirst = mSecond = null;
                CheckSuccess();
                isAniming = false;

            }

        });
    }

    //根据tag获取id
    public int getImageIdByTag(String tag) {
        String[] spilt = tag.split("_");
        return Integer.parseInt(spilt[0]);
    }

    public int getImageIndexByTag(String tag) {
        String[] spilt = tag.split("_");
        return Integer.parseInt(spilt[1]);
    }

    //构造动画层
    private void setUpAnimaLayout() {
        if (mAnimLayout == null) {
            mAnimLayout = new RelativeLayout(getContext());
            addView(mAnimLayout);
        }
    }

    //获取多个参数的最小值
    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (param < min) {
                min = param;
            }
        }
        return min;
    }

    public void tgbcZhanji(final String login_id, final int level, final String playTime) {

        //请求地址
        String url = "http://10.130.142.72:8080/Pintu_Chenyinru/saveServlet";

        String tag = "saveZhanji";

        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

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
                                Toast.makeText(getContext(),"保存成功！",Toast.LENGTH_SHORT).show();

                            } else {
                                Toast.makeText(getContext(), "亲，保存失败！", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "哎呀怎么没有网络呢？", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "操作失败啦，请稍候重试！", Toast.LENGTH_SHORT).show();
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