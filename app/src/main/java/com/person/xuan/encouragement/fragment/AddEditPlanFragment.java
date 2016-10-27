package com.person.xuan.encouragement.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.activity.HomeActivity;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.util.ShareValueUtil;
import com.person.xuan.encouragement.widget.EncouragementWidgetProvider;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class AddEditPlanFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtFinish;
    private EditText mEtPlanContent;
    private ImageView mIvHasReward;

    private Plan mPlan = new Plan();
    private boolean mIsEditable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_plan, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
        initAddEditFragment();
        refreshView();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        mBtFinish = findViewById(R.id.bt_finish);
        mEtPlanContent = findViewById(R.id.et_plan_content);
        mIvHasReward = findViewById(R.id.iv_has_reward);

    }

    public boolean isAdd() {
        return mIsEditable;
    }

    private void initEvent() {
        mBtFinish.setOnClickListener(this);
        mIvHasReward.setOnClickListener(this);
    }

    public void refreshView() {
        mEtPlanContent.setText(mPlan.getContent());
        mEtPlanContent.setSelection(mPlan.getContent().length());
        if (mPlan.getEncouragement() == 0) {
            mIvHasReward.setImageResource(R.drawable.collection_normal);
        } else {
            mIvHasReward.setImageResource(R.drawable.collection_choose);
        }
        if (mIsEditable) {
            mEtPlanContent.setEnabled(true);
            mBtFinish.setText(getString(R.string.save));
            ((HomeActivity) getActivity()).setTitleString(R.string.add_plan);
        } else {
            mEtPlanContent.setEnabled(false);
            mBtFinish.setText(getString(R.string.edit));
            ((HomeActivity) getActivity()).setTitleString(R.string.watch_plan);
        }
        if (mPlan.getEncouragement() == 1) {
            mIvHasReward.setImageResource(R.drawable.collection_choose);
        } else {
            mIvHasReward.setImageResource(R.drawable.collection_normal);
        }
    }

    private void initAddEditFragment() {
        int action = getActivity().getIntent().getIntExtra(ShareValueUtil.KEY_ACTION, 0);
        final long id = getActivity().getIntent().getLongExtra(ShareValueUtil.KEY_ID, -1);
        if (action == ShareValueUtil.ACTION_WATCH_PLAN && id > 0) {
            final Person person = EncouragementWrapper.getPerson(getActivity());
            mPlan = person.getPlan(id);
            mIsEditable = false;
        } else {
            mPlan = new Plan();
            mIsEditable = true;
        }
    }

    public void setEditMode() {
        mIsEditable = true;
    }

    public void setWatchMode(Context context, long id) {
        final Person person = EncouragementWrapper.getPerson(context);
        mPlan = person.getPlan(id);
        mIsEditable = false;
        if (mPlan == null) {
            throw new NullPointerException();
        }
    }

    private void savePlan(Plan plan) {
        Person person = EncouragementWrapper.getPerson(this.getActivity());
        person.updatePlan(plan);
        EncouragementWrapper.writePerson(this.getActivity(), person);
        EncouragementWidgetProvider.notifyDataSetChange(getActivity());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finish:
                if (mIsEditable) {
                    if (mEtPlanContent.getText() != null && mEtPlanContent.getText().length() > 1) {
                        String str = mEtPlanContent.getText().toString();
                        mPlan.setContent(str);
                        savePlan(mPlan);
                        Toast.makeText(getActivity(), getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                        setWatchMode(getActivity(), mPlan.getId());
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.add_more_words_tips), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    setEditMode();
                }
                break;
            case R.id.iv_has_reward:
                if (mPlan.getEncouragement() == 0) {
                    mPlan.setEncouragement(1);
                } else {
                    mPlan.setEncouragement(0);
                }
                break;
        }
        refreshView();
    }

}
