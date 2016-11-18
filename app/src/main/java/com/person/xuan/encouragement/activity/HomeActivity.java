package com.person.xuan.encouragement.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.fragment.AddEditPlanFragment;
import com.person.xuan.encouragement.fragment.HistoryFragment;
import com.person.xuan.encouragement.fragment.MeFragment;
import com.person.xuan.encouragement.util.ShareValueUtil;

public class HomeActivity extends Activity implements View.OnClickListener {
    private String TAG = "xxxx";
    private static final String ADD_EDIT_PLAN_FRAMENT = "ADD_EDIT_PLAN_FRAMENT";
    private static final String HISTORY_PLAN_FRAMENT = "HISTORY_PLAN_FRAMENT";
    private static final String ME_FRAMENT = "ME_FRAMENT";

    private LinearLayout mLlAddPlan;
    private LinearLayout mLlHistory;
    private LinearLayout mLlMe;
    private TextView mTvTitle;
    private ImageView mIvFunction;

    private FragmentManager mFragmentManager;
    private AddEditPlanFragment mAddEditPlanFragment;
    private Fragment mHistoryFragment;
    private Fragment mMeFragment;
    private String mCurFragmentTag;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        initEvent();
        initFragment();
    }

    private void initView() {
        mLlAddPlan = (LinearLayout) findViewById(R.id.ll_add_plan);
        mLlHistory = (LinearLayout) findViewById(R.id.ll_history);
        mLlMe = (LinearLayout) findViewById(R.id.ll_me);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvFunction = (ImageView) findViewById(R.id.iv_function);
    }

    public void setTitleString(int res) {
        mTvTitle.setText(getString(res));
    }

    private void initEvent() {
        mLlAddPlan.setOnClickListener(this);
        mLlHistory.setOnClickListener(this);
        mLlMe.setOnClickListener(this);
        mIvFunction.setOnClickListener(mAddPlanListener);
    }

    public void initFragment() {
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        initAddEditPlanFragment();
        mHistoryFragment = new HistoryFragment();
        mMeFragment = new MeFragment();
        transaction.add(R.id.fl_content, mAddEditPlanFragment, ADD_EDIT_PLAN_FRAMENT);
        transaction.add(R.id.fl_content, mHistoryFragment, HISTORY_PLAN_FRAMENT);
        transaction.add(R.id.fl_content, mMeFragment, ME_FRAMENT);
        hideAllFragment(transaction);
        mCurFragmentTag = ADD_EDIT_PLAN_FRAMENT;
        transaction.show(mAddEditPlanFragment);
        transaction.commit();
    }

    private void initAddEditPlanFragment() {
        mAddEditPlanFragment = new AddEditPlanFragment();
        int action = getIntent().getIntExtra(ShareValueUtil.KEY_ACTION, 0);
        final long id = getIntent().getLongExtra(ShareValueUtil.KEY_ID, -1);
        if (action == ShareValueUtil.ACTION_WATCH_PLAN && id > 0) {
            mAddEditPlanFragment.setWatchMode(this, id);
        }
    }


    @Override
    public void onClick(View v) {
        Log.e(TAG, "onClick!!");
        switch (v.getId()) {
            case R.id.ll_add_plan:
                showFragment(ADD_EDIT_PLAN_FRAMENT);
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
        if (fragmentTag == ADD_EDIT_PLAN_FRAMENT) {
            if (mAddEditPlanFragment == null) {
                Log.e(TAG, "mAdd PlanFragment == null!!");
                mAddEditPlanFragment = new AddEditPlanFragment();
                transaction.add(mAddEditPlanFragment, ADD_EDIT_PLAN_FRAMENT);
            }
            if (mAddEditPlanFragment.isAdd()) {
                setTitleString(R.string.add_plan);
            } else {
                setTitleString(R.string.watch_plan);
            }
            mAddEditPlanFragment.refreshView();
            transaction.show(mAddEditPlanFragment);
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
        mCurFragmentTag = fragmentTag;
        changeIvFunction(mCurFragmentTag);
        transaction.commit();
    }

    private void changeIvFunction(String fragmentTag) {
        switch (fragmentTag) {
            case ADD_EDIT_PLAN_FRAMENT:
                mIvFunction.setVisibility(View.VISIBLE);
                mIvFunction.setImageResource(R.drawable.ic_add);
                mIvFunction.setOnClickListener(mAddPlanListener);
                break;
            case HISTORY_PLAN_FRAMENT:
                mIvFunction.setVisibility(View.INVISIBLE);
                break;
            case ME_FRAMENT:
                mIvFunction.setVisibility(View.VISIBLE);
                mIvFunction.setImageResource(R.drawable.setting);
                mIvFunction.setOnClickListener(mSettingListener);
                break;
        }
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        transaction.hide(mAddEditPlanFragment);
        transaction.hide(mHistoryFragment);
        transaction.hide(mMeFragment);
    }

    private View.OnClickListener mAddPlanListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddEditPlanFragment.setEditMode();
            showFragment(ADD_EDIT_PLAN_FRAMENT);
        }
    };
    private View.OnClickListener mSettingListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(HomeActivity.this, SettingActivity.class);
            startActivity(intent);
        }
    };

}
