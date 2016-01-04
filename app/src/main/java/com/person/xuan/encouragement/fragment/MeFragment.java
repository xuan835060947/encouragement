package com.person.xuan.encouragement.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.person.xuan.encouragement.R;
import com.person.xuan.encouragement.entity.Person;
import com.person.xuan.encouragement.entity.Plan;
import com.person.xuan.encouragement.util.ShareValueUtil;
import com.person.xuan.encouragement.widget.EncouragementWrapper;

/**
 * Created by chenxiaoxuan1 on 15/12/22.
 */
public class MeFragment extends BaseFragment implements View.OnClickListener {
    private TextView mTvAllEncouragement;
    private TextView mTvHasNotUsedEncouragement;
    private TextView mTvHasUsedEncouragement;
    private TextView mTvPlanTitle;
    private ListView mLvPlans;
    private PlansListAdapter mAdapter;

    private Person mPerson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            refreshView();
        }
    }

    private void initView() {
        mTvAllEncouragement = findViewById(R.id.tv_all_encouragement);
        mTvHasNotUsedEncouragement = findViewById(R.id.tv_cur_encouragement);
        mTvHasNotUsedEncouragement.setOnClickListener(this);
        mTvHasUsedEncouragement = findViewById(R.id.tv_used_encouragement);
        mTvPlanTitle = findViewById(R.id.tv_doing_plan);
        mLvPlans = findViewById(R.id.lv_plans);
        mAdapter = new PlansListAdapter();
        mAdapter.setPerson(new Person());
        mLvPlans.setAdapter(mAdapter);
    }

    private void refreshView() {
        mPerson = EncouragementWrapper.getPerson(this.getActivity());
        Log.e("xxxx", "refresh View "+ShareValueUtil.GSON.toJson(mPerson));
        mAdapter.setPerson(mPerson);
        mAdapter.notifyDataSetChanged();
        mTvAllEncouragement.setText(String.valueOf(mPerson.getCurEncouragements() + mPerson.getUsedEncouragements()));
        mTvHasUsedEncouragement.setText(String.valueOf(mPerson.getUsedEncouragements()));
        mTvHasNotUsedEncouragement.setText(String.valueOf(mPerson.getCurEncouragements()));
        if (mPerson.getPlans().size() != 0) {
            mTvPlanTitle.setText(R.string.doing);
        } else {
            mTvPlanTitle.setText("");
        }
    }

    private class PlansListAdapter extends BaseAdapter {
        private Person mPerson;
        private LayoutInflater inflater = LayoutInflater.from(MeFragment.this.getActivity());

        public void setPerson(Person person) {
            this.mPerson = person;
        }

        @Override
        public int getCount() {
            return mPerson.getPlans().size();
        }

        @Override
        public Object getItem(int position) {
            return mPerson.getPlans().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                View view = inflater.inflate(R.layout.item_doing_plan, null);
                ViewHolder viewHolder = new ViewHolder();
                viewHolder.tvContent = (TextView) view.findViewById(R.id.tv_plan);
                viewHolder.ivReward = (ImageView) view.findViewById(R.id.iv_reward);
                viewHolder.btFinish = (Button) view.findViewById(R.id.bt_finish);
                refreshItem(viewHolder, position);
                view.setTag(viewHolder);
                return view;
            } else {
                ViewHolder viewHolder = (ViewHolder) convertView.getTag();
                refreshItem(viewHolder, position);
                return convertView;
            }
        }

        private void refreshItem(ViewHolder viewHolder, int position) {
            Plan plan = mPerson.getPlans().get(position);
            viewHolder.position = position;
            viewHolder.tvContent.setText(plan.getContent());
            viewHolder.btFinish.setTag(viewHolder);
            viewHolder.btFinish.setOnClickListener(onClickListener);
            if (plan.getEncouragement() != 0) {
                viewHolder.ivReward.setVisibility(View.VISIBLE);
                viewHolder.ivReward.setImageResource(R.drawable.reward);
            } else {
                viewHolder.ivReward.setVisibility(View.INVISIBLE);
            }
        }

        private View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder viewHolder = (ViewHolder) v.getTag();
                EncouragementWrapper.addHistoryPlan(mPerson.getPlans().get(viewHolder.position));
                mPerson.finish(viewHolder.position);
                EncouragementWrapper.writePerson(getActivity(), mPerson);
                refreshView();
            }
        };

    }


    static class ViewHolder {
        int position;
        TextView tvContent;
        ImageView ivReward;
        Button btFinish;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cur_encouragement:
                if(!mPerson.usedOneEncouragements()){
                    Toast.makeText(getActivity(), "星星不足,快完成点计划吧!", Toast.LENGTH_SHORT).show();
                }else {
                    EncouragementWrapper.writePerson(getActivity(), mPerson);
                    refreshView();
                }
                break;
        }
    }

}
