package com.person.xuan.encouragement.activity;

import android.app.Activity;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.util.ColorPickGradient;
import com.person.xuan.encouragement.widget.EncouragementWidgetProvider;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

/**
 * Created by chenxiaoxuan1 on 16/1/28.
 */
public class SettingActivity extends Activity implements SeekBar.OnSeekBarChangeListener , View.OnClickListener{
    private LinearLayout mLlShow;
    private SeekBar mSbTextSize;
    private SeekBar mSbHeight;
    private SeekBar mSbColor;
    private Button mBtCancel;
    private Button mBtSave;
//    private View mUp;
//    private View mDown;
//    private TextView mTvUp;
//    private TextView mTvDown;
    private int mTextSize;
    private int mHeight;
    private int mColor;
    private Person mPerson;
    private ColorPickGradient mColorPickGradient = new ColorPickGradient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        initData();
        initView();
        initEvent();
        refreshView();
        super.onCreate(savedInstanceState);
    }

    private void initData(){
        mPerson = EncouragementWrapper.getPerson(this);
        mHeight = mPerson.getPadingHeight();
        mTextSize = mPerson.getTextSize();
        mColor = mPerson.getTextColor();
    }

    private void initView(){
        mLlShow = (LinearLayout) findViewById(R.id.ll_show);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.item_reward_plan, null);
        mLlShow.addView(view);
        view = inflater.inflate(R.layout.item_reward_plan, null);
        mLlShow.addView(view);
        mSbTextSize = (SeekBar) findViewById(R.id.sb_text_size);
        mSbHeight = (SeekBar) findViewById(R.id.sb_height);
        mSbTextSize.setProgress(mTextSize);
        mSbHeight.setProgress(mHeight);
        mBtCancel = (Button) findViewById(R.id.bt_cancel);
        mBtSave = (Button) findViewById(R.id.bt_save);
        mSbColor = (SeekBar) findViewById(R.id.sb_color);
        initColorBar();
    }

    private void initColorBar(){
        ShapeDrawable.ShaderFactory shaderFactory = new ShapeDrawable.ShaderFactory() {
            @Override
            public Shader resize(int width, int height) {
                LinearGradient linearGradient = new LinearGradient(0, 0, width, height,
                        ColorPickGradient.PICKCOLORBAR_COLORS, ColorPickGradient.PICKCOLORBAR_POSITIONS, Shader.TileMode.REPEAT);
                return linearGradient;
            }
        };
        PaintDrawable paint = new PaintDrawable();
        paint.setShape(new RectShape());
        paint.setCornerRadius(10);
        paint.setShaderFactory(shaderFactory);
        mSbColor.setProgressDrawable(paint);
    }

    private void initEvent(){
        mSbTextSize.setOnSeekBarChangeListener(this);
        mSbHeight.setOnSeekBarChangeListener(this);
        mSbColor.setOnSeekBarChangeListener(this);
        mBtCancel.setOnClickListener(this);
        mBtSave.setOnClickListener(this);
    }

    private void refreshView(){
        RelativeLayout view1 = (RelativeLayout) mLlShow.getChildAt(0);
        RelativeLayout view2 = (RelativeLayout) mLlShow.getChildAt(1);
        TextView textView1 = (TextView) view1.findViewById(R.id.tv_plan);
        TextView textView2 = (TextView) view2.findViewById(R.id.tv_plan);
        TextView tvFinish1 = (TextView) view1.findViewById(R.id.tv_finish);
        TextView tvFinish2 = (TextView) view2.findViewById(R.id.tv_finish);
        textView1.setTextSize(mTextSize);
        textView2.setTextSize(mTextSize);
        tvFinish1.setTextSize(mTextSize);
        tvFinish2.setTextSize(mTextSize);
        textView1.setTextColor(mColor);
        textView2.setTextColor(mColor);
        tvFinish1.setTextColor(mColor);
        tvFinish2.setTextColor(mColor);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.height = mHeight;
        view1.setPadding(0,mHeight,0,mHeight);
        view2.setPadding(0, mHeight, 0, mHeight);
//        view1.setLayoutParams(params);
//        view2.setLayoutParams(params);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()){
            case R.id.sb_text_size:
                mTextSize = progress;
                break;
            case R.id.sb_height:
                mHeight = progress;
                break;
            case R.id.sb_color:
                float radio = (float)progress / mSbColor.getMax();
                mColor = mColorPickGradient.getColor(radio);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        refreshView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_cancel:
                onBackPressed();
                break;
            case R.id.bt_save:
                Person person = EncouragementWrapper.getPerson(this);
                person.setPadingHeight(mHeight);
                person.setTextSize(mTextSize);
                person.setTextColor(mColor);
                EncouragementWrapper.writePerson(this, person);
                EncouragementWidgetProvider.notifyDataSetChange(this);
                onBackPressed();
                break;
        }
    }
}
