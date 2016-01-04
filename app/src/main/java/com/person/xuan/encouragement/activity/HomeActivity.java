package com.person.xuan.encouragement.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.fragment.AddPlanFragment;
import com.person.xuan.encouragement.fragment.HistoryFragment;
import com.person.xuan.encouragement.fragment.MeFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "xxxx";
    private static final String ADD_PLAN_FRAMENT = "ADD_PLAN_FRAMENT";
    private static final String HISTORY_PLAN_FRAMENT = "HISTORY_PLAN_FRAMENT";
    private static final String ME_FRAMENT = "ME_FRAMENT";

    private LinearLayout mLlAddPlan;
    private LinearLayout mLlHistory;
    private LinearLayout mLlMe;
    private TextView mTvTitle;

    private FragmentManager mFragmentManager;
    private Fragment mAddPlanFragment;
    private Fragment mHistoryFragment;
    private Fragment mMeFragment;

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initEvent();
        initFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initView() {
        mLlAddPlan = (LinearLayout) findViewById(R.id.ll_add_plan);
        mLlHistory = (LinearLayout) findViewById(R.id.ll_history);
        mLlMe = (LinearLayout) findViewById(R.id.ll_me);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void setTitleString(int res) {
        mTvTitle.setText(getString(res));
    }

    private void initEvent() {
        mLlAddPlan.setOnClickListener(this);
        mLlHistory.setOnClickListener(this);
        mLlMe.setOnClickListener(this);
    }

    public void initFragment() {
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        mAddPlanFragment = new AddPlanFragment();
        mHistoryFragment = new HistoryFragment();
        mMeFragment = new MeFragment();
        transaction.add(R.id.fl_content, mAddPlanFragment, ADD_PLAN_FRAMENT);
        transaction.add(R.id.fl_content, mHistoryFragment, HISTORY_PLAN_FRAMENT);
        transaction.add(R.id.fl_content, mMeFragment, ME_FRAMENT);
        hideAllFragment(transaction);
        setTitleString(R.string.add_plan);
        transaction.show(mAddPlanFragment);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick!!");
        switch (v.getId()) {
            case R.id.ll_add_plan:
                showFragment(ADD_PLAN_FRAMENT);
                break;
            case R.id.ll_history:
                showFragment(HISTORY_PLAN_FRAMENT);
                break;
            case R.id.ll_me:
                showFragment(ME_FRAMENT);
                break;
        }
    }

    private void showFragment(String fragmentTag) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFragment(transaction);
        if (fragmentTag == ADD_PLAN_FRAMENT) {
            if (mAddPlanFragment == null) {
                Log.e(TAG, "mAddPlanFragment == null!!");
                mAddPlanFragment = new AddPlanFragment();
                transaction.add(mAddPlanFragment, ADD_PLAN_FRAMENT);
            }
            setTitleString(R.string.add_plan);
            transaction.show(mAddPlanFragment);
        } else if (fragmentTag == HISTORY_PLAN_FRAMENT) {
            if (mHistoryFragment == null) {
                Log.e(TAG, "mHistoryFragment == null!!");
                mHistoryFragment = new HistoryFragment();
                transaction.add(mHistoryFragment, HISTORY_PLAN_FRAMENT);
            }
            setTitleString(R.string.had_finish_plan);
            transaction.show(mHistoryFragment);
        } else if (fragmentTag == ME_FRAMENT) {
            if (mMeFragment == null) {
                Log.e(TAG, "mMeFragment == null!!");
                mMeFragment = new MeFragment();
                transaction.add(mMeFragment, ME_FRAMENT);
            }
            setTitleString(R.string.me);
            transaction.show(mMeFragment);
        }
        transaction.commit();
    }


    private void hideAllFragment(FragmentTransaction transaction) {
        transaction.hide(mAddPlanFragment);
        transaction.hide(mHistoryFragment);
        transaction.hide(mMeFragment);
    }

}
