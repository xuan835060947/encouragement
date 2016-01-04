package com.person.xuan.encouragement.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.util.ShareValueUtil;

/**
 * Created by chenxiaoxuan1 on 15/12/17.
 */
public class AddPlanFragment extends BaseFragment implements View.OnClickListener {
    private Button mBtFinish;
    private EditText mEtPlanContent;
    private ImageView mIvHasReward;

    private Plan mPlan = new Plan();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_plan, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        findEtFocus();
    }

    private void initView() {
        mBtFinish = findViewById(R.id.bt_finish);
        mEtPlanContent = findViewById(R.id.et_plan_content);
        mIvHasReward = findViewById(R.id.iv_has_reward);
    }

    private void initEvent() {
        mBtFinish.setOnClickListener(this);
        mIvHasReward.setOnClickListener(this);
    }

    private void findEtFocus() {
        mEtPlanContent.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_finish:
                if (mEtPlanContent.getText() != null && mEtPlanContent.getText().length() > 1) {
                    String str = mEtPlanContent.getText().toString();
                    Person person = getPerson();
                    mPlan.setContent(str);
                    person.addPlan(mPlan);
                    writePerson(person);
                    Intent intent = new Intent();
                    intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
                    getActivity().sendBroadcast(intent);
                    Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                    mEtPlanContent.setText("");
                } else {
                    Toast.makeText(getActivity(), "写多几个字吧,主人", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_has_reward:
                if (mPlan.getEncouragement() == 0) {
                    mPlan.setEncouragement(1);
                    mIvHasReward.setImageResource(R.drawable.collection_choose);
                }else {
                    mPlan.setEncouragement(0);
                    mIvHasReward.setImageResource(R.drawable.collection_normal);
                }
                break;
        }
    }

    private void writePerson(Person person) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(ShareValueUtil.PERSON_FILE, Context.MODE_PRIVATE).edit();
        editor.putString(ShareValueUtil.PERSON_STRING, ShareValueUtil.GSON.toJson(person));
        editor.apply();
    }

    private Person getPerson() {
        SharedPreferences preferences = getActivity().getSharedPreferences(ShareValueUtil.PERSON_FILE, Context.MODE_PRIVATE);
        String personStr = preferences.getString(ShareValueUtil.PERSON_STRING, null);
        if (personStr != null) {
            return ShareValueUtil.GSON.fromJson(personStr, Person.class);
        } else {
            return new Person();
        }
    }
}
